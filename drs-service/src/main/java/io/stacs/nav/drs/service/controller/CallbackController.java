package io.stacs.nav.drs.service.controller;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.service.dao.BlockVO;
import io.stacs.nav.drs.service.model.BlockCallbackBO;
import io.stacs.nav.drs.service.service.BlockCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.stacs.nav.drs.service.model.ConvertHelper.block2CallbackBOConvert;

/**
 * @author suimi
 * @date 2019/10/30
 */
@RestController @Slf4j @RequestMapping("/drs") public class CallbackController {
    @Autowired private BlockCallbackService txCallbackService;

    /**
     * callback
     *
     * @return
     */
    @PostMapping("/callback") public RespData callback(@RequestBody BlockVO vo) {
        log.info("received callback data:{}", JSON.toJSONString(vo));
        BlockCallbackBO bo = block2CallbackBOConvert.apply(vo);
        try {
            txCallbackService.receivedBlock(bo);
            return RespData.success();
        } catch (DappException e) {
            log.error("[callback]has error", e);
            return RespData.fail(e.getCode(), e.getMsg());
        } catch (Throwable e) {
            log.error("[callback]has error", e);
            return RespData.fail(DappError.DAPP_COMMON_ERROR.getCode(), e.getMessage());
        }
    }
}
