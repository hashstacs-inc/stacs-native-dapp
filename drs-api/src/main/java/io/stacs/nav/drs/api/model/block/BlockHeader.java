package io.stacs.nav.drs.api.model.block;

import io.stacs.nav.drs.api.model.BaseBO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * The type Block header.
 *
 * @Description: block p2p information
 * @author: pengdi
 */
@Setter @Getter public class BlockHeader implements BaseBO {
    private String version;

    private String previousHash;

    private String blockHash;

    @NotNull private Long height;

    private StateRootHash stateRootHash;

    private Long blockTime;
    /**
     * the number of transactions recorded by the current block
     */
    private Long totalTxNum;
    private Integer txNum;

    private BigDecimal totalBlockSize;
}
