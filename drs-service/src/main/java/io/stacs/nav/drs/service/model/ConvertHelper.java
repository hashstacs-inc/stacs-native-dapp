package io.stacs.nav.drs.service.model;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.model.block.BlockHeader;
import io.stacs.nav.drs.api.model.block.BlockVO;
import io.stacs.nav.drs.service.dao.po.BlockCallbackPO;
import io.stacs.nav.drs.service.dao.po.BlockPO;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import org.springframework.beans.BeanUtils;

import java.util.function.Function;

/**
 * @author dekuofa <br>
 * @date 2019-12-09 <br>
 */
public interface ConvertHelper {

    Function<BlockVO, BlockCallbackBO> block2CallbackBOConvert = block -> {
        BlockCallbackBO bo = new BlockCallbackBO();
        bo.setBlockHeight(block.getBlockHeader().getHeight());
        bo.setBlockData(JSON.toJSONString(block));
        return bo;
    };

    Function<BlockCallbackBO, BlockCallbackPO> block2CallbackPOConvert = bo -> {
        BlockCallbackPO po = new BlockCallbackPO();
        BeanUtils.copyProperties(bo, po);
        return po;
    };

    Function<BlockCallbackPO, BlockCallbackPO> setPOInitState = po -> {
        po.setStatus("INIT");
        return po;
    };

    // 块信息
    Function<BlockHeader, BlockPO> blockHeader2BlockPO = header -> {
        BlockPO po = BeanConvertor.convertBean(header, BlockPO.class);
        BeanUtils.copyProperties(header.getStateRootHash(), po);
        return po;
    };

}
