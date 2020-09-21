package com.main.glory.model.supplier.requestmodals;


import com.main.glory.model.supplier.SupplierRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSupplierRatesRequest {
    Long supplierId;
    List<SupplierRate> supplierRates;
}
