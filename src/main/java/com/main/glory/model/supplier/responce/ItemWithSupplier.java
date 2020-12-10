package com.main.glory.model.supplier.responce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemWithSupplier {
    Long id;
    String itemName;
    Long supplierId;
    Double rate;
}
