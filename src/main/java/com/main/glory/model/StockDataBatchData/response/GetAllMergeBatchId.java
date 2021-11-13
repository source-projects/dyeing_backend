package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.SecondaryTable;


@NoArgsConstructor
@Getter
@Setter
public class GetAllMergeBatchId {
    Long id;
    String mergeBatchId;

    public GetAllMergeBatchId(Long id, String mergeBatchId) {
        this.id = id;
        this.mergeBatchId = mergeBatchId;
    }
}
