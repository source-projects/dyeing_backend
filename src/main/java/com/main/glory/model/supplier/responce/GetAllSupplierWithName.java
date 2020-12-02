package com.main.glory.model.supplier.responce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class GetAllSupplierWithName {
    Long id;
    String supplierName;
}
