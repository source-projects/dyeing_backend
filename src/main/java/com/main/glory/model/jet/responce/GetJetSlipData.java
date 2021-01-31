package com.main.glory.model.jet.responce;

import com.main.glory.model.dyeingProcess.DyeingProcessData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetJetSlipData {

    String processName;
    Double temp;
    Double holdTime;
    List<SlipItemList> itemList;


    public GetJetSlipData(DyeingProcessData dyeingProcessData, List<SlipItemList> itemLists) {

        this.processName=dyeingProcessData.getProcessType();
        this.temp=dyeingProcessData.getTemp();
        this.holdTime = dyeingProcessData.getHoldTime();

        this.itemList=itemLists;
    }
}
