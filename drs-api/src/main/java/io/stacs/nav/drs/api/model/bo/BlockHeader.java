package io.stacs.nav.drs.api.model.bo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.stacs.nav.drs.api.model.BaseBO;
import io.stacs.nav.drs.api.model.TimeStampDeserializer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

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

    @JsonDeserialize(using = TimeStampDeserializer.class) private Date blockTime;
    /**
     * the number of transactions recorded by the current block
     */
    private Long totalTxNum;
}
