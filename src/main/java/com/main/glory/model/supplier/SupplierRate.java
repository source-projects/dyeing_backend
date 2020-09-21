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
    Double rate;
    @ApiModelProperty(hidden = true)
    Double discountedRate;

    @ApiModelProperty(hidden = true)
    Double gstRate;
    Long userId;

    @ApiModelProperty(hidden = true)
    Date createdDate;
    String createdBy;
    String updatedBy;

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
}

