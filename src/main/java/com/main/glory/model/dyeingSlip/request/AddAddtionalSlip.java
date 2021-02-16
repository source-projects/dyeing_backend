package com.main.glory.model.dyeingSlip.request;

import com.main.glory.model.dyeingSlip.DyeingSlipData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddAddtionalSlip {

    String batchId;
    Long productionId;
    DyeingSlipData dyeingSlipData;
}
