package com.main.glory.model.supplier.responce;

import com.main.glory.model.supplier.SupplierRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierRateResponse extends SupplierRateDTO {

    String supplierName;

    public SupplierRateResponse(SupplierRate s,String supplierName)
    {
        super(s);
        this.supplierName = supplierName;
    }


}
