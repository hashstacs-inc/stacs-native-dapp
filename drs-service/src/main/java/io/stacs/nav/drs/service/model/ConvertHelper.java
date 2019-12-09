package io.stacs.nav.drs.service.model;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.model.bo.Block;
import io.stacs.nav.drs.service.dao.po.BlockCallbackPO;
import org.springframework.beans.BeanUtils;

import java.util.function.Function;

/**
 * @author dekuofa <br>
 * @date 2019-12-09 <br>
 */
public class ConvertHelper {

    public static Function<Block, BlockCallbackBO> block2CallbackBOConvert = block -> {
        BlockCallbackBO bo = new BlockCallbackBO();
        bo.setBlockHeight(block.getBlockHeader().getHeight());
        bo.setBlockData(JSON.toJSONString(block));
        return bo;
    };

    public static Function<BlockCallbackBO, BlockCallbackPO> block2CallbackPOConvert = bo -> {
        BlockCallbackPO po = new BlockCallbackPO();
        BeanUtils.copyProperties(bo, po);
        return po;
    };
}
