package io.stacs.nav.drs.service.model.block;

import lombok.Getter;
import lombok.Setter;

/**
 * The type State root hash.
 *
 * @author Tangfashuang
 * @desc state root hash object
 * @date 2018 /4/8 20:40
 */
@Getter @Setter public class StateRootHash {
    /**
     * calculate merkle tree root hash by transaction list of current package
     */
    private String txRootHash;

    /**
     * After all transactions in the current package are executed，
     * then calculate merkle tree root hash by account state
     */
    private String accountRootHash;

    /**
     * After one transaction is executed，
     * calculate merkle tree root hash by current transaction state
     */
    private String txReceiptRootHash;

    /**
     * calculate merkle tree root hash by the rs list of current node maintenance
     */
    private String rsRootHash;

    /**
     * calculate merkle tree root hash by the policy list of current node maintenance
     */
    private String policyRootHash;

    /**
     * After all transactions in the current package are executed，
     * then calculate merkle tree root hash by contract state
     */
    private String contractRootHash;

    /**
     * calculate merkle tree root hash by action with type of CA_AUTH
     */
    private String caRootHash;

    private String stateRoot;
}
