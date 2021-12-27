package com.main.glory.model.dyeingSlip.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrintDyeingSlipMast {
    String type;//{"all","Color","Chemical"}
    List<PrintDyeingSlipData> list;
}
