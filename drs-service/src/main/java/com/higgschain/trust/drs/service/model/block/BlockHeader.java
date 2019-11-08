package com.higgschain.trust.drs.service.model.block;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * The type Block header.
 *
 * @Description: block p2p information
 * @author: pengdi
 */
@Setter @Getter public class BlockHeader{
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
}
