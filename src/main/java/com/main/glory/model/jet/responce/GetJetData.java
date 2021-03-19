package com.main.glory.model.jet.responce;

import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetStatus;
import com.main.glory.model.productionPlan.ProductionPlan;
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
}
