package com.main.glory.model.StockDataBatchData.response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class GetAllBatch {

    Long controlId;
    String batchId;
    Boolean prodctionPlanned;

}
