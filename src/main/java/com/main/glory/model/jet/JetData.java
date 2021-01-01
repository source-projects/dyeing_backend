package com.main.glory.model.jet;

import com.main.glory.model.jet.request.AddJetData;
import com.main.glory.model.jet.request.ToJet;
import com.main.glory.model.productionPlan.ProductionPlan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class JetData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long controlId;
    @Enumerated(EnumType.STRING)
    JetStatus status=JetStatus.inQueue;
    Long sequence;
    Long productionId;




    public JetData(AddJetData jetData, ProductionPlan productionPlanExist) {

        this.controlId=jetData.getControlId();
        this.sequence=jetData.getSequence();
        this.productionId=productionPlanExist.getId();
        this.status=JetStatus.inQueue;

    }

    public JetData(ToJet to) {
        this.controlId=to.getJetId();
        this.sequence= to.getSequence();
        this.productionId=to.getProductionId();
        this.status=JetStatus.inQueue;
    }
}
