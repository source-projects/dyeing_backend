package com.main.glory.model.supplier;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@AllArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    Long id;
    String supplierName;
    Double discountPercentage;
    Double gstPercentage;
    Long userId;
    String remark;
    @ApiModelProperty(hidden = true)
    Date createdDate;
    Long paymentTerms;

    @ApiModelProperty(hidden = true)
    Boolean isActive;

    String updatedBy;

    @ApiModelProperty(hidden = true)
    Date updatedDate;

    @Transient
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "supplierId", referencedColumnName = "id")
    @ApiModelProperty(hidden = true)
    List<SupplierRate> supplierRates;

    public Supplier() {
        isActive = true;
    }
}




