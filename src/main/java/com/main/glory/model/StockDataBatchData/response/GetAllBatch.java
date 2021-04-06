package com.main.glory.model.StockDataBatchData.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.response.GetQualityResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class GetAllBatch {

    @ApiModelProperty(hidden = true)
    Double totalWt;
    Long controlId;
    String batchId;
    Boolean productionPlanned;
    Boolean isBillGenerated;
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


    public GetAllBatch(Party party, GetQualityResponse quality, GetBatchWithControlId batch) {
        this.controlId=batch.getControlId();
        this.batchId=batch.getBatchId();
        this.partyId=party.getId();
        this.partyName=party.getPartyName();
        this.qualityEntryId=quality.getId();
        this.qualityId=quality.getQualityId();
        this.qualityName=quality.getQualityName();
        this.qualityType=quality.getQualityType();

    }

    public GetAllBatch(GetBatchByInvoice g, Party party, Optional<Quality> quality) {
        this.controlId=g.getStockId();
        this.batchId=g.getBatchId();
        this.partyId=party.getId();
        this.partyName=party.getPartyName();
        this.qualityEntryId=quality.get().getId();
        this.qualityId=quality.get().getQualityId();
        this.qualityName=quality.get().getQualityName();
        this.qualityType=quality.get().getQualityType();
        this.productionPlanned=true;//because it is already getting the data who;s flag is true
        this.isBillGenerated=false;
    }

    public GetAllBatch(GetAllBatch getAllBatch) {
        this.totalWt=getAllBatch.getTotalWt();
        this.controlId=getAllBatch.getControlId();
        this.batchId=getAllBatch.getBatchId();
        this.productionPlanned=getAllBatch.getProductionPlanned();
        this.isBillGenerated=getAllBatch.getIsBillGenerated();
        this.partyId=getAllBatch.getPartyId();
        this.partyName=getAllBatch.getPartyName();
        this.qualityEntryId=getAllBatch.getQualityEntryId();
        this.qualityId=getAllBatch.getQualityId();
        this.qualityName=getAllBatch.qualityName;
        this.qualityType=getAllBatch.qualityType;

    }
    public GetAllBatch(GetBatchWithControlId getAllBatch) {
        this.totalWt=getAllBatch.getWT();
        this.batchId=getAllBatch.getBatchId();


    }

    public GetAllBatch(Double totalWt, Long controlId, String batchId, Long partyId, String partyName, Long qualityEntryId, String qualityId, String qualityName, String qualityType) {
        this.totalWt = totalWt;
        this.controlId = controlId;
        this.batchId = batchId;
        this.partyId = partyId;
        this.partyName = partyName;
        this.qualityEntryId = qualityEntryId;
        this.qualityId = qualityId;
        this.qualityName = qualityName;
        this.qualityType = qualityType;
    }

    public GetAllBatch(GetBatchByInvoice g) {
        this.productionPlanned=true;//because it is already getting the data who;s flag is true
        this.isBillGenerated=false;
        this.batchId = g.getBatchId();

    }

    public GetAllBatch(Double totalWt, Long controlId, String batchId, Boolean productionPlanned, Long partyId, String partyName, Long qualityEntryId, String qualityId, String qualityName, String qualityType) {
        this.totalWt = totalWt;
        this.controlId = controlId;
        this.batchId = batchId;
        this.productionPlanned = productionPlanned;
        this.partyId = partyId;
        this.partyName = partyName;
        this.qualityEntryId = qualityEntryId;
        this.qualityId = qualityId;
        this.qualityName = qualityName;
        this.qualityType = qualityType;
    }
}
