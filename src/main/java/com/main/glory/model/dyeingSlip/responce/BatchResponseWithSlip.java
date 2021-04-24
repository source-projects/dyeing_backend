package com.main.glory.model.dyeingSlip.responce;


import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.shade.ShadeMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchResponseWithSlip {
    DyeingSlipData dyeingSlipData;
    Double totalWt;
    String qualityId;
    Long qualityEntryId;
    String partyShadeNo;
    Long batchCount;
    String colorTone;
    String colorName;



    public BatchResponseWithSlip(DyeingSlipData data, ShadeMast shadeMast, Double totalWt, String qualityId,Long batchCount) {
        this.dyeingSlipData = data;
        this.totalWt = totalWt;
        this.qualityId=qualityId;
        if(shadeMast!=null) {
            this.colorName = shadeMast.getColorName() == null ? "" : shadeMast.getColorName();
            this.colorTone = shadeMast.getColorTone() == null ? "" : shadeMast.getColorTone();
            this.partyShadeNo = shadeMast.getPartyShadeNo() == null ? "" : shadeMast.getPartyShadeNo();
        }
        this.batchCount = batchCount;
    }
}
