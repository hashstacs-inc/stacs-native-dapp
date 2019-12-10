package io.stacs.nav.drs.api.model.block;

import io.stacs.nav.drs.api.model.BaseBO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Block.
 *
 * @Description: block object
 * @author: pengdi
 */
@Getter @Setter public class Block implements BaseBO {

    private static final long serialVersionUID = -1313232933424709877L;
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
    private List<SignedTransaction> signedTxList;
}
