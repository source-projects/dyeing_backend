package com.main.glory.model.StockDataBatchData.request;

import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.shade.ShadeMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@Getter
@Setter
public class BatchDetail {
    Long controlId;
    String batchId;
    Boolean isProductionPlanned;
    Boolean isBillGenrated;
    Boolean isFinishMtrSave;
    Long qualityEntryId;
    String qualityName;
    String qualityId;
    Long pcs;
    Double totalMtr;
    Double totalFinish;
    Date receiveDate;
    String mergeBatchId;
    String partyShadeNo;
    String colorName;
    String colorTone;



    public BatchDetail(Long controlId, String batchId, Boolean isProductionPlanned, Boolean isBillGenrated, Boolean isFinishMtrSave, Long pcs, Double totalMtr,Double totalFinish, Date receiveDate,String mergeBatchId) {
        this.controlId = controlId;
        this.batchId = batchId;
        this.isProductionPlanned = isProductionPlanned;
        this.isBillGenrated = isBillGenrated;
        this.isFinishMtrSave = isFinishMtrSave;
        this.pcs = pcs;
        this.totalMtr = totalMtr;
        this.totalFinish=totalFinish;
        this.receiveDate = receiveDate;
        this.mergeBatchId = mergeBatchId;
    }

    public BatchDetail(BatchDetail batchDetail, Quality quality, QualityName qualityName) {
        this.controlId = batchDetail.controlId;
        this.batchId = batchDetail.batchId;
        this.isProductionPlanned = batchDetail.isProductionPlanned;
        this.isBillGenrated = batchDetail.isBillGenrated;
        this.isFinishMtrSave = batchDetail.isFinishMtrSave;
        this.pcs = batchDetail.pcs;
        this.totalMtr = batchDetail.totalMtr;
        this.totalFinish=batchDetail.totalFinish;
        this.receiveDate = batchDetail.receiveDate;
        this.qualityName = qualityName.getQualityName();
        this.qualityId=quality.getQualityId();
        this.qualityEntryId=quality.getId();
    }
    public BatchDetail(BatchDetail batchDetail, Quality quality, QualityName qualityName, ShadeMast shadeMast) {
        this.controlId = batchDetail.controlId;
        this.batchId = batchDetail.batchId;
        this.isProductionPlanned = batchDetail.isProductionPlanned;
        this.isBillGenrated = batchDetail.isBillGenrated;
        this.isFinishMtrSave = batchDetail.isFinishMtrSave;
        this.pcs = batchDetail.pcs;
        this.totalMtr = batchDetail.totalMtr;
        this.totalFinish=batchDetail.totalFinish;
        this.receiveDate = batchDetail.receiveDate;
        this.qualityName = qualityName.getQualityName();
        this.qualityId=quality.getQualityId();
        this.qualityEntryId=quality.getId();
        this.partyShadeNo=shadeMast.getPartyShadeNo();
    }
}
