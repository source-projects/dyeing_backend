package com.main.glory.model.dyeingSlip.responce;

import com.main.glory.model.dyeingSlip.DyeingSlipData;
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
}
