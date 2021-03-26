package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.SecondaryTable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllMergeBatchId {
    String mergeId;
}
