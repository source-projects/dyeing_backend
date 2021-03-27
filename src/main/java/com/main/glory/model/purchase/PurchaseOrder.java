package com.main.glory.model.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
@Getter
@Setter
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long supplierId;
    String supplierName;
    Long itemId;
    String itemName;
    Long userHeadId;
    Long createdBy;
    Long status;

    public PurchaseOrder(PurchaseOrder p){
        this.id = p.id;
        this.supplierId = p.supplierId;
        this.status = p.status;
        this.supplierName = p.supplierName;
        this.itemId = p.itemId;
        this.itemName = p.itemName;
        this.userHeadId = p.userHeadId;
        this.createdBy = p.createdBy;
    }
}
