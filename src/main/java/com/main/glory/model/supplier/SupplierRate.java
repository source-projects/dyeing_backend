package com.main.glory.model.supplier;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "supplierRate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="supplierId", referencedColumnName = "id", insertable = true, updatable = true)    
    Supplier supplier;
    String itemName;
    String itemType;
    Double rate;

    Double discountedRate;

    Double gstRate;
    Long userHeadId;

    @ApiModelProperty(hidden = true)
    Date createdDate;
    Long createdBy;
    Long updatedBy;
//    Boolean isColor=true;

    @ApiModelProperty(hidden = true)
    Date updatedDate;

    public SupplierRate(SupplierRate supplierRate){
        this.id=supplierRate.id;
        this.supplier=supplierRate.supplier;
        this.itemName=supplierRate.itemName;
        this.itemType=supplierRate.itemType;
        this.rate=supplierRate.rate;
        this.discountedRate=supplierRate.discountedRate;
        this.gstRate=supplierRate.gstRate;
        this.userHeadId=supplierRate.userHeadId;
        this.createdDate=supplierRate.createdDate;
        this.createdBy=supplierRate.createdBy;
        this.updatedBy=supplierRate.updatedBy;
        this.updatedDate=supplierRate.updatedDate;

    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

    public SupplierRate(AddSupplierRate other,Supplier Supplier) {
        this.id = other.id;
        this.supplier = supplier;
        this.itemName = other.itemName;
        this.itemType = other.itemType;
        this.rate = other.rate;
        this.discountedRate = other.discountedRate;
        this.gstRate = other.gstRate;
        this.userHeadId = other.userHeadId;
        this.createdDate = other.createdDate;
        this.createdBy = other.createdBy;
        this.updatedBy = other.updatedBy;
        this.updatedDate = other.updatedDate;
    }
}

