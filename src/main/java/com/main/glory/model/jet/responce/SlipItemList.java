package com.main.glory.model.jet.responce;

import com.main.glory.model.dyeingProcess.DyeingChemicalData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SlipItemList {

    String itemName;
    Double qty;
    String supplierName;
    String itemId;

}
