package io.stacs.nav.drs.boot.service;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.boot.dao.SysConfDao;
import io.stacs.nav.drs.boot.dao.po.SysConfPO;
import io.stacs.nav.drs.service.config.DomainConfig;
import io.stacs.nav.drs.service.config.DrsConfig;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import io.stacs.nav.drs.service.utils.config.ConfigurationManager;
import io.stacs.nav.drs.service.vo.ConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuyu
 * @description
 * @date 2020-01-19
 */
@Service @Slf4j public class PropertiesService implements InitializingBean{
    private final static String _KEY = "PROPERTIES_KEY";
    @Autowired SysConfDao sysConfDao;
    @Autowired ConfigurationManager manager;

    @Override public void afterPropertiesSet() throws Exception {
        ConfigVO vo = queryConfigVO();
        if(vo != null){
            //update for memory
            manager.updateConfig(BeanConvertor.convertBean(vo.getDomainConfig(), DomainConfig.class));
            manager.updateConfig(BeanConvertor.convertBean(vo.getDrsConfig(), DrsConfig.class));
        }
    }
    /**
     * save or update
     *
     * @param vo
     */
    public void saveOrUpdate(ConfigVO vo) {
        SysConfPO po = sysConfDao.queryByKey(_KEY);
        if (po == null) {
            po = new SysConfPO();
            po.setKey(_KEY);
            po.setValue(JSON.toJSONString(vo));
            po.setRemark("system properties config");
            sysConfDao.save(po);
        } else {
            sysConfDao.updateByKey(_KEY, JSON.toJSONString(vo));
        }
    }

    /**
     * query
     */
    public ConfigVO queryConfigVO() {
        SysConfPO po = sysConfDao.queryByKey(_KEY);
        if (po == null) {
            return null;
        }
        return JSON.parseObject(po.getValue(), ConfigVO.class);
    }
}
