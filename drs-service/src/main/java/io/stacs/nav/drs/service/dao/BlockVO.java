package io.stacs.nav.drs.service.dao;

import io.stacs.nav.drs.api.model.TransactionPO;
import io.stacs.nav.drs.api.model.block.BlockHeader;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@ToString @NoArgsConstructor @AllArgsConstructor @Getter @Setter public class BlockVO {
    /**
     * is genesis of block
     */
    private boolean genesis;
    /**
     * block p2p
     */
    @Valid @NotNull private BlockHeader blockHeader;

    /**
     * the list that store signed transaction
     */
    private List<TransactionPO> transactionList;
}
