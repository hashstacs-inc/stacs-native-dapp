package io.stacs.nav.drs.service.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.stacs.nav.drs.api.model.TransactionPO;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.dao.po.ContractPO;
import io.stacs.nav.drs.service.dao.po.PolicyPO;
import io.stacs.nav.drs.service.enums.ActionExecTypeEnum;
import io.stacs.nav.drs.service.enums.ContractStatusEnum;
import io.stacs.nav.drs.service.utils.JSONHelper;
import io.stacs.nav.drs.service.utils.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static io.stacs.nav.drs.service.utils.JSONHelper.parseJSONOArray;

/**
 * @author dekuofa <br>
 * @date 2019-12-10 <br>
 */
public class ActionConverterUtil {

    static final ThreadLocal<TransactionPO> TRANSACTION_LOCAL = new ThreadLocal<>();

    static final Function<JSONObject, Optional<BusinessDefinePO>> BDConverter = json -> JSONHelper.toJavaObject(json,
                                                                                                                BusinessDefinePO.class)
        .map(po -> {
            TransactionPO tx = TRANSACTION_LOCAL.get();
            po.setCreateTime(tx.getBlockTime());
            return po;
        });
    static final Function<JSONObject, Optional<PolicyPO>> PolicyConverter = CommonConvert(PolicyPO.class);
    static final Function<JSONObject, Optional<ContractPO>> ContractConverter = json -> JSONHelper.toJavaObject(json,
                                                                                                                ContractPO.class)
        .map(po -> {
            TransactionPO tx = TRANSACTION_LOCAL.get();
            po.setBdCode(tx.getBdCode());
            po.setTxId(tx.getTxId());
            po.setBlockHeight(tx.getBlockHeight());
            po.setCreateTime(tx.getBlockTime());
            po.setStatus(ContractStatusEnum.valueOfBD(tx.getBdCode()).getCode());
            return po;
        });

    private static final String ACTION_TYPE_KEY = "type";
    private static Map<String, Function<JSONObject, ?>> converterMap = new HashMap<String, Function<JSONObject, ?>>() {
        {
            put("BD_PUBLISH", BDConverter);
            put("CONTRACT_CREATION", ContractConverter);
            put("REGISTER_POLICY", PolicyConverter);
        }
    };

    public static <T> Optional<List<Pair<ActionExecTypeEnum, T>>> doConvert(TransactionPO tx) {
        try {
            TRANSACTION_LOCAL.set(tx);
            return convertTxToActionList(tx.getActionDatas());
        } finally {
            TRANSACTION_LOCAL.remove();
        }
    }

    private static <T> Optional<List<Pair<ActionExecTypeEnum, T>>> convertTxToActionList(String json) {
        // 1. jsonArray 2. actions 3. for(actions) -> action
        return parseJSONOArray(json).flatMap(ActionConverterUtil::arrayToActions);
    }

    //@formatter:off
    @SuppressWarnings("unchecked")
    private static <T> Optional<List<Pair<ActionExecTypeEnum, T>>> arrayToActions(JSONArray array) {
        List<Pair<ActionExecTypeEnum, T>> actions = Lists.newArrayList();
        for (Object obj : array) {
            JSONObject json = (JSONObject)obj;
            String actionType = json.getString(ACTION_TYPE_KEY);
            ActionExecTypeEnum.valueOfActionType(actionType).ifPresent(
                execType -> json2Action(json).ifPresent(action -> actions.add(Pair.of(execType,(T)action))));

        }
        return actions.isEmpty() ? Optional.empty() : Optional.of(actions);
    }
    //@formatter:on

    @SuppressWarnings("unchecked") private static <T> Optional<T> json2Action(JSONObject json) {
        Function<JSONObject, Optional<?>> converter = (Function<JSONObject, Optional<?>>)converterMap.get(
            json.getString(ACTION_TYPE_KEY));
        if (converter == null) {
            return Optional.empty();
        }
        return (Optional<T>)converter.apply(json);
    }

    private static <T> Function<JSONObject, Optional<T>> CommonConvert(Class<T> clazz) {
        return json -> JSONHelper.toJavaObject(json, clazz);
    }
}
