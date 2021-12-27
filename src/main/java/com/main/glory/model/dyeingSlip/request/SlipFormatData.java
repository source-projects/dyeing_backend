package com.main.glory.model.dyeingSlip.request;

import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SlipFormatData extends DyeingSlipMast{

    Double totalWt;
    Double totalMeter;
    String qualityId;
    Long qualityEntryId;
    String partyShadeNo;
    Long batchCount;
    String colorTone;
    String colorName;
    String jetName;
    String partyName;

    public SlipFormatData(SlipFormatData slipFormatData) {
        super(slipFormatData);
        this.totalWt = slipFormatData.getTotalWt();
        this.totalMeter = slipFormatData.getTotalMeter();
        this.qualityId = slipFormatData.getQualityId();
        this.qualityEntryId = slipFormatData.getQualityEntryId();
        this.partyShadeNo = slipFormatData.getPartyShadeNo();
        this.batchCount = slipFormatData.getBatchCount();
        this.colorName=slipFormatData.getColorName();
        this.colorTone =slipFormatData.getColorTone();
        this.jetName =slipFormatData.getJetName();
        this.partyName= slipFormatData.getPartyName();
    }


    public SlipFormatData(DyeingSlipMast dyeingSlipMastExist) {
        super(dyeingSlipMastExist);
    }

}
