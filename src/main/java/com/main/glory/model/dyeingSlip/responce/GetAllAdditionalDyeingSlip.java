package com.main.glory.model.dyeingSlip.responce;

import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class GetAllAdditionalDyeingSlip {

    Long id;
    Long stockId;
    Long jetId;
    Long productionId;
    String batchId;
    DyeingSlipData dyeingSlipData;

    public GetAllAdditionalDyeingSlip(Long id, Long stockId, Long jetId, Long productionId, String batchId) {
        this.id = id;
        this.stockId = stockId;
        this.jetId = jetId;
        this.productionId = productionId;
        this.batchId = batchId;

    }

    public GetAllAdditionalDyeingSlip(GetAllAdditionalDyeingSlip ad, DyeingSlipData additionalDyeingSlip) {
        this.id=ad.getId();
        this.batchId=ad.getBatchId();
        this.jetId=ad.getJetId();
        this.stockId=ad.getStockId();
        this.productionId=ad.getProductionId();
        this.dyeingSlipData=additionalDyeingSlip;
    }

    public GetAllAdditionalDyeingSlip(DyeingSlipMast dyeingSlipMastExist) {
        this.id=dyeingSlipMastExist.getId();
        this.batchId=dyeingSlipMastExist.getBatchId();
        this.jetId=dyeingSlipMastExist.getJetId();
        this.stockId=dyeingSlipMastExist.getStockId();
        this.productionId=dyeingSlipMastExist.getProductionId();
       // this.dyeingSlipData=dyeingSlipMastExist;
    }
}
