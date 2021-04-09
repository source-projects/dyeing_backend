package com.main.glory.model.productionPlan.request;

import com.main.glory.model.dyeingProcess.DyeingProcessData;
import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddDirectBatchToJet {
    String partyId;
    String qualityEntryId;
    Long shadeId;
    //String colorName;
    String batchId;
    Long stockId;
    Long jetId;
    Long createdBy;
    Long updatedBy;
    DyeingSlipData dyeingSlipData;

}
