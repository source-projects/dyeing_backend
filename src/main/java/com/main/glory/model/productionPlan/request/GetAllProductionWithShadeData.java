package com.main.glory.model.productionPlan.request;

import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeMast;
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
    String qualityName;
    String qualityId;
    String processName;
    String partyShadeNo;
    Double totalWt;
    Double totalMtr;


    public GetAllProductionWithShadeData(ProductionPlan productionPlan,String colorTone,String qualityName,String qualityId,String processName,String partyShadeNo,Double WT,Double MTR)
    {
        super(productionPlan);
        this.colorTone=colorTone;
        this.qualityName=qualityName;
        this.qualityId=qualityId;
        this.processName=processName;
        this.partyShadeNo=partyShadeNo;
        this.totalMtr=MTR;
        this.totalWt=WT;
    }
    public GetAllProductionWithShadeData(ProductionPlan productionPlan,String colorTone)
    {
        super(productionPlan);
        this.colorTone=colorTone;
    }

    public GetAllProductionWithShadeData(GetAllProductionWithShadeData g)
    {
        super(g);
        this.colorTone=g.getColorTone();
    }

    public GetAllProductionWithShadeData(ProductionPlan data, ShadeMast shadeMast, GetQualityResponse quality, DyeingProcessMast dyeingProcessMast, Double wt, Double mtr) {
        super(data);
        this.colorTone=shadeMast.getColorTone();
        this.qualityName=quality.getQualityName();
        this.qualityId=quality.getQualityId();
        this.processName=dyeingProcessMast.getProcessName();
        this.partyShadeNo=shadeMast.getPartyShadeNo();
        this.totalMtr=mtr;
        this.totalWt=wt;

    }
}
