package com.main.glory.model.productionPlan;

import com.main.glory.model.productionPlan.ProductionPlan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllProductionWithShadeData extends ProductionPlan {

    String colorTone;

    public GetAllProductionWithShadeData(ProductionPlan productionPlan,String colorTone)
    {
        super(productionPlan);
        this.colorTone=colorTone;
    }
}
