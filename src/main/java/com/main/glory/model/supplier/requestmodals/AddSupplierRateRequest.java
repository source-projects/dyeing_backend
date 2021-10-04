package com.main.glory.model.supplier.requestmodals;

import com.main.glory.model.supplier.AddSupplierRate;
import com.main.glory.model.supplier.SupplierRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddSupplierRateRequest{
    Long id;
    List<AddSupplierRate> addSupplierRate;
}
