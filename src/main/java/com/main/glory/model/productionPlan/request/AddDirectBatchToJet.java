package com.main.glory.model.productionPlan.request;

import com.main.glory.model.dyeingProcess.DyeingProcessData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddDirectBatchToJet {
    Long partyId;
    Long qualityEntryId;
    Long shadeId;
    //String colorName;
    String batchId;
    Long stockId;
    Long jetId;
    Long createdBy;
    Long updatedBy;
    DyeingProcessData dyeingProcessData;

}
