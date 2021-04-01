package com.main.glory.model.dispatch.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class GetBatchByInvoice {

    @JsonIgnore
    Long batchEntryId;
    String batchId;
    Long stockId;
    String mergeBatchId;

    public GetBatchByInvoice(Long batchEntryId, String batchId, Long stockId) {
        this.batchEntryId = batchEntryId;
        this.batchId = batchId;
        this.stockId = stockId;
    }
    public GetBatchByInvoice(Long batchEntryId, String batchId, Long stockId,String mergeBatchId) {
        this.batchEntryId = batchEntryId;
        this.batchId = batchId;
        this.stockId = stockId;
        this.mergeBatchId=mergeBatchId;
    }
}
