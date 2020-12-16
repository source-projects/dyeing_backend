package com.main.glory.model.StockDataBatchData.response;


import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
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

    public GetAllBatch(Party party, Quality quality) {
        this.partyId=party.getId();
        this.partyName=party.getPartyName();
        this.qualityEntryId=quality.getId();
        this.qualityName=quality.getQualityName();
        this.qualityType=quality.getQualityType();
        this.qualityId=quality.getQualityId();
    }
}
