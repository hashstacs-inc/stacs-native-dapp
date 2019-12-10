package io.stacs.nav.drs.service.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.stacs.nav.drs.api.model.TransactionPO;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.dao.po.ContractPO;
import io.stacs.nav.drs.service.dao.po.PolicyPO;
import io.stacs.nav.drs.service.enums.ContractStatusEnum;
import io.stacs.nav.drs.service.utils.JSONHelper;

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
    static final Function<JSONObject, Optional<?>> BDConverter = CommonConvert(BusinessDefinePO.class);
    static final Function<JSONObject, Optional<?>> PolicyConverter = CommonConvert(PolicyPO.class);
    static final Function<JSONObject, Optional<?>> ContractConverter = json -> JSONHelper.toJavaObject(json,
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
    private static Map<String, Function<JSONObject, Optional<?>>> converterMap =
        new HashMap<String, Function<JSONObject, Optional<?>>>() {
            {
                put("BD_PUBLISH", BDConverter);
                put("CONTRACT_CREATION", ContractConverter);
                put("REGISTER_POLICY", PolicyConverter);
            }
        };

    public static Optional<List<?>> doConvert(TransactionPO tx) {
        try {
            TRANSACTION_LOCAL.set(tx);
            return convertTxToActionList(tx.getActionDatas());
        } finally {
            TRANSACTION_LOCAL.remove();
        }
    }

    private static Optional<List<?>> convertTxToActionList(String json) {
        // 1. jsonArray 2. actions 3. for(actions) -> action
        return parseJSONOArray(json).flatMap(ActionConverterUtil::arrayToActions);
    }

    private static Optional<List<?>> arrayToActions(JSONArray array) {
        List<Object> actions = Lists.newArrayList();
        for (Object obj : array) {
            JSONObject json = (JSONObject)obj;
            json2Action(json).ifPresent(actions::add);
        }
        return actions.isEmpty() ? Optional.empty() : Optional.of(actions);
    }

    private static Optional<?> json2Action(JSONObject json) {
        Function<JSONObject, Optional<?>> converter = converterMap.get(json.getString(ACTION_TYPE_KEY));
        if (converter == null) {
            return Optional.empty();
        }
        return converter.apply(json);
    }

    private static Function<JSONObject, Optional<?>> CommonConvert(Class<?> clazz) {
        return json -> JSONHelper.toJavaObject(json, clazz);
    }
}
