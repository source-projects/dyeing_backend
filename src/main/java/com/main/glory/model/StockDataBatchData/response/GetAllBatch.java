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
    Long partyId;
    String partyName;
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    String qualityType;

}
