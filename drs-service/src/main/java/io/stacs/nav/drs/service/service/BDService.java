package io.stacs.nav.drs.service.service;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.BaseTxVO;
import io.stacs.nav.drs.service.utils.ReflectionUtil;
import io.stacs.nav.drs.service.vo.BDVO;
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
     * submit bd
     *
     * @param bdvo
     */
    public void submit(BDVO bdvo) {
        log.info("[BDService]param:{}", bdvo);
        String funcName = bdvo.getFunctionName();
        if(!paramMap.containsKey(funcName)){
            log.error("[submit]can`t find functionName:{}",funcName);
            throw new DappException(DappError.FUNCTION_NOT_FIND_ERROR);
        }
        String json = JSON.toJSONString(bdvo.getParam());
        BaseTxVO o = (BaseTxVO)JSON.parseObject(json,paramMap.get(funcName));
        if(StringUtils.isEmpty(o.getTxId())){
            o.setTxId(funcName + "-" + System.currentTimeMillis());
        }
        txRequestService.submitTx(o);
        log.info("[BDService]process end");
    }
}
