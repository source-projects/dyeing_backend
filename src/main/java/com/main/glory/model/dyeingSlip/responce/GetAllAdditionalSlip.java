/*
package com.main.glory.model.dyeingSlip.responce;

import com.main.glory.model.dyeingSlip.AdditionDyeingProcessSlip;
import com.main.glory.model.dyeingSlip.request.AddAdditionDyeingSlipModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllAdditionalSlip {

    Long id;
    String batchId;
    Long stockId;
    Long productionId;
    String name;
    Date date;



    public GetAllAdditionalSlip(AdditionDyeingProcessSlip slip) {
        this.id=slip.getId();
        this.batchId =slip.getBatchId();
        this.name=slip.getName();
        this.productionId= slip.getStockId();
        this.stockId=slip.getStockId();
        this.date=slip.getCreatedDate();
    }
}
*/
