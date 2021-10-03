package com.main.glory.model.supplier;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.user.UserData;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.util.ArrayList;
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
    Long id;
    String supplierName;
    Double discountPercentage;
    Double gstPercentage;
    String remark;
    @ApiModelProperty(hidden = true)
    Date createdDate;
    @ApiModelProperty(hidden = true)
    Date updatedDate;
    Long paymentTerms;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="qualityNameId", referencedColumnName = "id", insertable = true, updatable = true)    
	QualityName qualityName;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="createdBy", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData createdBy;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="updatedBy", referencedColumnName = "id", insertable = true, updatable = true)
    private UserData updatedBy;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userHeadId", referencedColumnName = "id", insertable = true, updatable = true)
    private UserData userHeadData;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "supplierId", referencedColumnName = "id")
    @ApiModelProperty(hidden = true)
    List<SupplierRate> supplierRates;

    public void addSupplierRates(SupplierRate supplierRate){
        this.supplierRates.add(supplierRate);
    }

    public Supplier(Long id, String supplierName, Double discountPercentage, Double gstPercentage, String remark, UserData createdBy, Date createdDate, Date updatedDate, Long paymentTerms, UserData updatedBy, UserData userHeadData) {
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
        this.userHeadData = userHeadData;
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

    public Supplier(AddSupplier addSupplier,UserData createdBy, UserData updatedBy, UserData userHeadData,QualityName qualityName) {
        this.id = addSupplier.getId();
        this.supplierName = addSupplier.getSupplierName();
        this.discountPercentage = addSupplier.getDiscountPercentage();
        this.gstPercentage = addSupplier.getGstPercentage();
        this.remark = addSupplier.getRemark();
        this.createdBy = createdBy;
        this.createdDate = addSupplier.getCreatedDate();
        this.updatedDate = addSupplier.getUpdatedDate();
        this.paymentTerms = addSupplier.getPaymentTerms();
        this.updatedBy = updatedBy;
        this.userHeadData = userHeadData;
        this.qualityName=qualityName;
        this.supplierRates=new ArrayList<SupplierRate>();
        


    }
}




