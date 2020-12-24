package com.main.glory.model.jet;

import com.main.glory.model.jet.request.AddJetData;
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
        this.id=jetData.getId();
        this.controlId=jetData.getControlId();
        this.sequence=jetData.getSequence();
        this.productionId=productionPlanExist.getId();
    }
}
