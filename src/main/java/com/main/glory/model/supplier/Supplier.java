package com.main.glory.model.supplier;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    Long id;
    String supplierName;
    Double discountPercentage;
    Double gstPercentage;
    String remark;
    Long createdBy;
    @ApiModelProperty(hidden = true)
    Date createdDate;
    @ApiModelProperty(hidden = true)
    Date updatedDate;
    Long paymentTerms;
    @JsonIgnore
    Long updatedBy;
    Long userHeadId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "supplierId", referencedColumnName = "id")
    @ApiModelProperty(hidden = true)
    List<SupplierRate> supplierRates;

    public Supplier(Long id, String supplierName, Double discountPercentage, Double gstPercentage, String remark, Long createdBy, Date createdDate, Date updatedDate, Long paymentTerms, Long updatedBy, Long userHeadId) {
        this.id = id;
        this.supplierName = supplierName;
        this.discountPercentage = discountPercentage;
        this.gstPercentage = gstPercentage;
        this.remark = remark;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.paymentTerms = paymentTerms;
        this.updatedBy = updatedBy;
        this.userHeadId = userHeadId;
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }
}




