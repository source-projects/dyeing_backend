package com.main.glory.model.dyeingSlip.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrintDyeingSlipData {
    Long productionId;
    String batchId;
}
