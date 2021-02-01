package com.main.glory.model.supplier;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Time;
import java.util.Calendar;
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
    Long supplierId;
    String itemName;
    String itemType;
    Double rate;
    @ApiModelProperty(hidden = true)
    Double discountedRate;

    @ApiModelProperty(hidden = true)
    Double gstRate;
    Long userHeadId;

    @ApiModelProperty(hidden = true)
    Date createdDate;
    Long createdBy;
    Long updatedBy;
    Boolean isColor;

    @ApiModelProperty(hidden = true)
    Date updatedDate;


    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

    public SupplierRate(SupplierRate other) {
        this.id = other.id;
        this.supplierId = other.supplierId;
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

