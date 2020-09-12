package com.main.glory.model.supplier;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "supplierRate")
@Getter
@Setter
@AllArgsConstructor
public class SupplierRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
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

    @ApiModelProperty(hidden = true)
    Boolean isActive;

    String updatedBy;

    @ApiModelProperty(hidden = true)
    Date updatedDate;

    public SupplierRate() {
        this.isActive = true;
    }


}

