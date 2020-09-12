package com.main.glory.model.supplier.requestmodals;

import com.main.glory.model.supplier.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSupplierRequest extends Supplier {
    Long id;
}
