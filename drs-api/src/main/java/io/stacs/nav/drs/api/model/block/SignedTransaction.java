package io.stacs.nav.drs.api.model.block;

import io.stacs.nav.drs.api.model.BaseBO;
import io.stacs.nav.drs.api.model.callback.TransactionReceipt;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Signed transaction.
 *
 * @Description: signed transaction class
 * @author: pengdi
 */
@Getter @Setter public class SignedTransaction implements BaseBO {

    private static final long serialVersionUID = -7372870730463030762L;

    /**
     * service transaction
     */
    @NotNull @Valid private CoreTransaction coreTx;

    /**
     * the list that store signatures
     */
    private List<SignInfo> signatureList;

    private TransactionReceipt receipt;

    @Override public int hashCode() {
        if (coreTx != null && coreTx.getTxId() != null) {
            return coreTx.getTxId().hashCode();
        }
        return super.hashCode();
    }

    @Override public boolean equals(Object obj) {
        if (obj != null && obj instanceof SignedTransaction) {
            SignedTransaction signedTx = (SignedTransaction)obj;
            if (this.coreTx != null && signedTx.coreTx != null) {
                return StringUtils.equals(this.coreTx.getTxId(), signedTx.getCoreTx().getTxId());
            }
        }
        return super.equals(obj);
    }

}
