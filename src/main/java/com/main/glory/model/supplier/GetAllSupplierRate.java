package com.main.glory.model.supplier;

import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.supplier.requestmodals.SupplierRateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllSupplierRate extends SupplierRateDTO {
    String supplierName;

    public GetAllSupplierRate(SupplierRate other,String supplierName)
    {
        super(other);
        this.supplierName=supplierName;
    }

}
