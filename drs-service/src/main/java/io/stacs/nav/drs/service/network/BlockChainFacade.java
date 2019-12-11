package io.stacs.nav.drs.service.network;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.stacs.nav.drs.api.enums.ApiConstants.ApiInterface;
import io.stacs.nav.drs.api.model.Policy;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.RsDomain;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.block.BlockHeaderVO;
import io.stacs.nav.drs.api.model.permission.PermissionInfoVO;
import io.stacs.nav.drs.api.model.query.QueryBlockByHeightVO;
import io.stacs.nav.drs.api.model.query.QueryBlockVO;
import io.stacs.nav.drs.api.model.query.QueryTxListVO;
import io.stacs.nav.drs.api.model.query.QueryTxVO;
import io.stacs.nav.drs.api.model.tx.CoreTransactionVO;
import io.stacs.nav.drs.service.config.DomainConfig;
import io.stacs.nav.drs.service.utils.CasDecryptResponse;
import io.stacs.nav.drs.service.utils.DrsHttpClient;
import io.stacs.nav.drs.service.utils.LambdaExceptionUtil;
import io.stacs.nav.drs.service.utils.Pair;
import io.stacs.nav.drs.service.utils.config.ConfigListener;
import io.stacs.nav.drs.service.vo.PermissionCheckVO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.stacs.nav.drs.api.enums.ApiConstants.ENCRYPT_WHITE_LIST;
import static io.stacs.nav.drs.api.enums.ApiConstants.QueryApiEnum.*;
import static io.stacs.nav.drs.service.utils.HttpHelper.buildGetRequestParam;
import static io.stacs.nav.drs.service.utils.Pair.of;

/**
 * @author liuyu
 * @date 2019-11-07
 */
@Component @Slf4j public class BlockChainFacade implements ConfigListener {

    @Autowired private DrsHttpClient client;
//    private static Predicate<String> encryptSkipFilter = Pattern.compile("[Qq]uery").asPredicate();
    private static Predicate<String> encryptSkipFilter = s -> true;

    private String baseUrl;

    private static Function<CasDecryptResponse, RespData<?>> defaultPostConverter() {
        // todo 通过json 对象先获取 respData.data 再根据 class 转
        return decryptResp -> JSONObject.parseObject(decryptResp.getData().toString(), RespData.class);
    }

    @SuppressWarnings("unchecked") private static <T> Function<ResponseBody, RespData<T>> defaultGetConverter()
        throws IOException {
        return LambdaExceptionUtil.rethrowFunction(
            body -> (RespData<T>)JSONObject.parseObject(body.string(), RespData.class));
    }

    @SuppressWarnings("unchecked") private static <T> Function<RespData<?>, T> getRealData() {
        return respData -> (T)respData.getData();
    }

    @SuppressWarnings("unchecked") private static <T> Function<RespData<?>, Optional<T>> checkSuccessAndGetRealData() {
        return resp -> resp.isSuccessful() ? Optional.of((T)resp.getData()) : Optional.empty();
    }

    public <T> Optional<T> commonGetApi(ApiInterface api, @Nullable List<Pair<String, String>> params) {
        try {
            return sendGet(api.getApi(), params, checkSuccessAndGetRealData());
        } catch (IOException e) {
            log.error("[{}]has unknown error", api.getName(), e);
            return Optional.empty();
        }
    }

    public <T> Optional<T> commonPostApi(ApiInterface api, Object param) {
        return this.commonPostApi(api, param, null);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> commonPostApi(ApiInterface api, Object param, @Nullable Function<RespData<?>, T> converter) {
        if (Objects.isNull(converter)) {
            converter = respData -> (T)respData.getData();
        }
        try {
            return Optional.of(send(api.getApi(), param)).map(converter);
        } catch (IOException e) {
            log.error("[{}]has unknown error", api.getName(), e);
            return Optional.empty();
        }
    }

    public Optional<BusinessDefine> queryBDInfoByCode(String bdCode) {
        try {
            Optional<List<BusinessDefine>> result = queryBDInfo(bdCode);
            return result.map(list -> list.get(0));
        } catch (Exception e) {
            log.error("[queryBDInfoByCode]has unknown error", e);
            return Optional.empty();
        }
    }

    public Optional<List<BusinessDefine>> queryBDInfo(String bdCode) {
        List<Pair<String, String>> params = null;
        if (StringUtils.isNotEmpty(bdCode)) {
            params = Lists.newArrayList(of("bdCode", bdCode));
        }
        return commonGetApi(BD_QUERY, params);
    }

    public Optional<Integer> queryCurrentHeight() {
        return commonGetApi(QUERY_MAX_BLOCK_HEIGHT, null);
    }

    public Optional<List<CoreTransactionVO>> queryCoreTxListByPage(QueryTxListVO vo) {
        return commonPostApi(QUERY_TX_LIST_BY_PAGE, vo);
    }

    public Optional<CoreTransactionVO> queryCoreTxById(QueryTxVO vo) {
        return commonPostApi(QUERY_TX_BY_ID, vo);
    }

    public Optional<BlockHeaderVO> queryBlockByHeight(QueryBlockByHeightVO vo) {
        return commonPostApi(QUERY_BLOCK_BY_HEIGHT, vo);
    }

    public Optional<List<BlockHeaderVO>> queryBlockListByPage(QueryBlockVO vo) {
        return commonPostApi(QUERY_BLOCKS_BY_PAGE, vo);
    }

    /**
     * query domains of block chain
     */
    public Optional<List<RsDomain>> queryAllDomains() {
        return commonGetApi(QUERY_ALL_DOMAIN, null);
    }

    public Optional<List<JSONObject>> queryBlocks(long startHeight, long endHeight) {
        return commonGetApi(QUERY_BLOCK_VO, Lists.newArrayList(Pair.of("startHeight", String.valueOf(startHeight)),
                                                               Pair.of("endHeight", String.valueOf(endHeight))));
    }

    /**
     * query domains of block chain
     */
    public Optional<List<Policy>> queryAllPolicyList() {
        return commonGetApi(QUERY_POLICY_LIST, null);
    }

    /**
     * query all permission of block chain
     */
    public Optional<List<PermissionInfoVO>> queryPermissionList() {
        return commonGetApi(QUERY_PERMISSION_LIST, null);
    }

    public Optional<Boolean> checkPermission(PermissionCheckVO vo) {
        return commonPostApi(CHECK_PERMISSION, vo);
    }

    @Nonnull
    public <T> T sendGet(String api, @Nullable List<Pair<String, String>> params, Function<RespData<?>, T> converter)
        throws IOException {
        String url = baseUrl + api;
        String requestParam = buildGetRequestParam(params);
        if (StringUtils.isNotEmpty(requestParam)) {
            if (url.contains("?")) {
                url = url + "&" + requestParam;
            } else {
                url = url + "?" + requestParam;
            }
        }
        return sendGet(url, converter);
    }

    public <T> T sendGet(String url, Function<RespData<?>, T> converter) throws IOException {
        return DrsHttpClient.get(url, defaultGetConverter().andThen(converter));
    }

    public RespData<?> send(String api, Object txData) throws IOException {
        return send(api, txData, defaultPostConverter());
    }

    public <T> T send(String api, Object txData, Function<CasDecryptResponse, T> converter) throws IOException {
        if (encryptSkipFilter.test(api) || ENCRYPT_WHITE_LIST.stream().anyMatch(url -> url.equals(api))) {
            return client.post(txData, baseUrl + api, converter);
        }
        return client.postWithEncrypt(txData, baseUrl + api, converter);
    }

    @Override public <T> void updateNotify(T config) {
        this.baseUrl = ((DomainConfig)config).getBaseUrl();
    }

    @Nonnull @Override public Predicate<Object> filter() {
        return obj -> obj instanceof DomainConfig;
    }
}
