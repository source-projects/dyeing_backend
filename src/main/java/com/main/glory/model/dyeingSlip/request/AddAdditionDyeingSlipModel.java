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
public class AddAdditionDyeingSlipModel {


    Long userHeadId;
    Long createdBy;
    Long updatedBy;

    String batchId;
    Long productionId;
    String name;
    DyeingSlipMast dyeingSlipMast;

}
