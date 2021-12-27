package com.main.glory.model.jet.responce;

import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.hmi.HMIMast;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetStatus;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.shade.ShadeMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetJetData {
    Long id;
    Long controlId;
    String status;
    Long sequence;
    Long productionId;
    String batchId;
    String colorTone;

    String partyId;
    String partyName;
    String qualityEntryId;
    String qualityId;
    String qualityName;
    Double totalWt;
    Double totalMtr;
    String processName;
    String partyShadeNo;
    Long stockId;
    Boolean sco;
    Boolean doseNylone;
    Boolean isDirect;
    Long shadeId;




    public GetJetData(JetData jetData) {
        this.id=jetData.getId();
        this.controlId=jetData.getControlId();
        this.status=jetData.getStatus().toString();
        this.sequence=jetData.getSequence();
        this.productionId=jetData.getProductionId();

    }
    public GetJetData(JetData jetData, ShadeMast color) {
        this.id=jetData.getId();
        this.controlId=jetData.getControlId();
        this.status=jetData.getStatus().toString();
        this.sequence=jetData.getSequence();
        this.productionId=jetData.getProductionId();
        this.colorTone=(color.getColorTone()==null)?"not mention":color.getColorTone();
    }


    public GetJetData(JetData jetData, ShadeMast color, Party party, Quality quality, QualityName qualityName, Double totalMtr, Double totalWt, ProductionPlan productionPlan) {
        this.id=jetData.getId();
        this.controlId=jetData.getControlId();
        this.status=jetData.getStatus().toString();
        this.sequence=jetData.getSequence();
        this.productionId=jetData.getProductionId();
        this.partyId = party.getId().toString();
        this.partyName=party.getPartyName();
        this.qualityEntryId = quality.getId().toString();
        this.qualityId=quality.getQualityId();
        this.qualityName=qualityName.getQualityName();
        this.totalMtr=totalMtr;
        this.totalWt=totalWt;
        //this.stockId=productionPlan.getStockId();
        this.colorTone=(color.getColorTone()==null)?"not mention":color.getColorTone();
    }

    public GetJetData(JetData jetData, ShadeMast colorTone, Party party, Quality quality, QualityName qualityName, Double totalMtr, Double totalWt, ProductionPlan productionPlan, DyeingProcessMast dyeingProcessMast) {
        this.id=jetData.getId();
        this.controlId=jetData.getControlId();
        this.status=jetData.getStatus().toString();
        this.sequence=jetData.getSequence();
        this.productionId=jetData.getProductionId();
        this.partyId = party.getId().toString();
        this.partyName=party.getPartyName();
        this.qualityEntryId = quality.getId().toString();
        this.qualityId=quality.getQualityId();
        this.qualityName=qualityName.getQualityName();
        this.totalMtr=totalMtr;
        this.totalWt=totalWt;
        //this.stockId=productionPlan.getStockId();
        this.processName=dyeingProcessMast.getProcessName();
        this.batchId=productionPlan.getBatchId();
        this.colorTone=colorTone.getColorTone();
        this.partyShadeNo=colorTone.getPartyShadeNo();

    }

    public GetJetData(JetData jetData, Party party, Quality quality, QualityName qualityName, Double totalMtr, Double totalWt, ProductionPlan productionPlan) {
        this.id=jetData.getId();
        this.controlId=jetData.getControlId();
        this.status=jetData.getStatus().toString();
        this.sequence=jetData.getSequence();
        this.productionId=jetData.getProductionId();
        this.batchId = productionPlan.getBatchId();
        this.partyId = party.getId().toString();
        this.partyName=party.getPartyName();
        this.qualityEntryId = quality.getId().toString();
        this.qualityId=quality.getQualityId();
        this.qualityName=qualityName.getQualityName();
        this.totalMtr=totalMtr;
        this.totalWt=totalWt;
        //this.stockId=productionPlan.getStockId();

    }



    public GetJetData(GetAllProductionWithShadeData data, JetData jetData, ShadeMast colorTone, DyeingProcessMast dyeingProcessMast) {
        this.id=jetData.getId();
        this.batchId=data.getBatchId();
        this.controlId=jetData.getControlId();
        this.status=jetData.getStatus().toString();
        this.sequence=jetData.getSequence();
        this.productionId=jetData.getProductionId();
        this.colorTone=colorTone.getColorTone()==null?null:colorTone.getColorTone();
        this.partyId=data.getPartyId();
        this.partyName=data.getPartyName();
        this.qualityEntryId=data.getQualityEntryId();
        this.qualityId=data.getQualityId();
        this.qualityName=data.getQualityName();
        this.totalWt=data.getTotalWt();
        this.totalMtr=data.getTotalMtr();
        this.processName=dyeingProcessMast.getProcessName()==null?null:dyeingProcessMast.getProcessName();
        this.partyShadeNo=colorTone.getPartyShadeNo()==null?null:colorTone.getPartyShadeNo();
        //Long stockId;
    }

    public GetJetData(JetData jetData, GetAllProductionWithShadeData data) {
        this.id=jetData.getId();
        this.controlId=jetData.getControlId();
        this.status=jetData.getStatus().toString();
        this.sequence=jetData.getSequence();
        this.productionId=jetData.getProductionId();
        this.batchId=data.getBatchId();
        //this.colorTone=colorTone.getColorTone()==null?null:colorTone.getColorTone();
        this.partyId=data.getPartyId();
        this.partyName=data.getPartyName();
        this.qualityEntryId=data.getQualityEntryId();
        this. qualityId=data.getQualityId();
        this. qualityName=data.getQualityName();
        this. totalWt=data.getTotalWt();
        this. totalMtr=data.getTotalMtr();
        //this. processName=dyeingProcessMast.getProcessName()==null?null:dyeingProcessMast.getProcessName();
        //this.partyShadeNo=colorTone.getPartyShadeNo()==null?null:colorTone.getPartyShadeNo();
    }

    public GetJetData(GetAllProductionWithShadeData data, JetData jetData, ShadeMast colorTone, DyeingProcessMast dyeingProcessMast, HMIMast hmiMastExist) {
        this.id=jetData.getId();
        this.batchId=data.getBatchId();
        this.controlId=jetData.getControlId();
        this.status=jetData.getStatus().toString();
        this.sequence=jetData.getSequence();
        this.productionId=jetData.getProductionId();
        this.colorTone=colorTone==null?null:colorTone.getColorTone();
        this.partyId=data.getPartyId();
        this.partyName=data.getPartyName();
        this.qualityEntryId=data.getQualityEntryId();
        this.qualityId=data.getQualityId();
        this.qualityName=data.getQualityName();
        this.totalWt=data.getTotalWt();
        this.totalMtr=data.getTotalMtr();
        this.processName=dyeingProcessMast==null?null:dyeingProcessMast.getProcessName();
        this.partyShadeNo=colorTone==null?null:colorTone.getPartyShadeNo();
        this.sco = hmiMastExist!=null? hmiMastExist.getSco() : false;
        this.doseNylone= hmiMastExist!=null?hmiMastExist.getDoseNylon():false;
        this.isDirect = data.getIsDirect();
        this.shadeId = data.getShadeId();
    }

    public GetJetData(JetData jetData, GetAllProductionWithShadeData data, HMIMast hmiMastExist) {
        this.id=jetData.getId();
        this.isDirect = data.getIsDirect();
        this.shadeId = data.getShadeId();
        this.controlId=jetData.getControlId();
        this.status=jetData.getStatus().toString();
        this.sequence=jetData.getSequence();
        this.productionId=jetData.getProductionId();
        this.batchId=data.getBatchId();
        //this.colorTone=colorTone.getColorTone()==null?null:colorTone.getColorTone();
        this.partyId=data.getPartyId();
        this.partyName=data.getPartyName();
        this.qualityEntryId=data.getQualityEntryId();
        this. qualityId=data.getQualityId();
        this. qualityName=data.getQualityName();
        this. totalWt=data.getTotalWt();
        this. totalMtr=data.getTotalMtr();
        this.sco = hmiMastExist!=null?hmiMastExist.getSco():false;
        this.doseNylone=hmiMastExist!=null?hmiMastExist.getDoseNylon():false;
    }
}
