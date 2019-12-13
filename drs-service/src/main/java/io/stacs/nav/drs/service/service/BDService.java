package io.stacs.nav.drs.service.service;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.BaseTxVO;
import io.stacs.nav.drs.service.utils.ReflectionUtil;
import io.stacs.nav.drs.service.vo.BDVO;
import io.stacs.nav.drs.service.vo.SignVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author liuyu
 * @description
 * @date 2019-12-12
 */
@Service @Slf4j public class BDService implements InitializingBean {
    @Autowired private TxRequestService txRequestService;
    @Autowired private SignatureService signatureService;
    private Map<String, Class> paramMap = new HashMap<>();

    @Override public void afterPropertiesSet() throws Exception {
        Reflections reflections = ReflectionUtil.getDefaultReflections();
        Set<Class<? extends BaseTxVO>> clazzs = reflections.getSubTypesOf(BaseTxVO.class);
        clazzs.forEach(v -> {
            try {
                BaseTxVO o = v.newInstance();
                String name = o.getFunctionName();
                paramMap.put(name, o.getClass());
            } catch (Exception e) {
                log.error("has error", e);
            }
        });
    }

    /**
     * get base vo from json
     *
     * @param bdvo
     * @return
     */
    private BaseTxVO getBaseTxVo(BDVO bdvo) {
        log.info("[getBaseTxVo]param:{}", bdvo);
        String funcName = bdvo.getFunctionName();
        if (!paramMap.containsKey(funcName)) {
            log.error("[getBaseTxVo]can`t find functionName:{}", funcName);
            throw new DappException(DappError.FUNCTION_NOT_FIND_ERROR);
        }
        String json = JSON.toJSONString(bdvo.getParam());
        log.info("[getBaseTxVo]param.json:{}", json);
        BaseTxVO o = (BaseTxVO)JSON.parseObject(json, paramMap.get(funcName));
        if (StringUtils.isEmpty(o.getTxId())) {
            o.setTxId(funcName + "-" + System.currentTimeMillis());
        }
        log.info("getBaseTxVo success {}", o);
        return o;
    }

    /**
     * get signature bd
     *
     * @param bdvo
     */
    public SignVO generateSignature(BDVO bdvo) {
        BaseTxVO o = getBaseTxVo(bdvo);
        String sign = signatureService.generateSignature(o);
        log.info("[generateSignature] is success,funcName:{},txId:{}", o.getFunctionName(), o.getTxId());
        return new SignVO(o.getTxId(), sign);
    }

    /**
     * submit bd
     *
     * @param bdvo
     */
    public void submit(BDVO bdvo) {
        BaseTxVO o = getBaseTxVo(bdvo);
        txRequestService.submitTx(o);
        log.info("[BDService]process is success, funcName:{},txId:{}", o.getFunctionName(), o.getTxId());
    }
}
