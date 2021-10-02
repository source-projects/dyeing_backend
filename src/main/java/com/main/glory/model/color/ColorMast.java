package com.main.glory.model.color;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.user.UserData;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "color_mast")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="supplierId", referencedColumnName = "id", insertable = true, updatable = true)    
    Supplier supplier;
    
    String billNo;
    Date billDate;
    String chlNo;
    Date chlDate;
    Double billAmount;
    
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="createdBy", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData createdBy;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="updatedBy", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData updatedBy;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userHeadId", referencedColumnName = "id", insertable = true, updatable = true)
    private UserData userHeadData;

    String remark;
    @ApiModelProperty(hidden = true)
    Date createdDate;
    @ApiModelProperty(hidden = true)
    Date updatedDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId",referencedColumnName = "id")
    List<ColorData> colorDataList;

    public ColorMast(Long id, Supplier supplier, String billNo, Date billDate, String chlNo, Date chlDate, Double billAmount, UserData userData, UserData userData2, UserData userData3, String remark, List<ColorData> colorDataList) {
        this.id = id;
        this.supplier = supplier;
        this.billNo = billNo;
        this.billDate = billDate;
        this.chlNo = chlNo;
        this.chlDate = chlDate;
        this.billAmount = billAmount;
        this.createdBy = userData;
        this.updatedBy = userData2;
        this.userHeadData = userHeadData;
        this.remark = remark;
        this.colorDataList = colorDataList;
    }


    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

    public ColorMast(AddColorMast other,UserData createdBy,UserData updatedBy,UserData userHeadData,Supplier supplier) {
        this.id = other.id;
        this.supplier = supplier;
        this.billNo = other.billNo;
        this.billDate = other.billDate;
        this.chlNo = other.chlNo;
        this.chlDate = other.chlDate;
        this.billAmount = other.billAmount;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.userHeadData = userHeadData;
        this.remark = other.remark;
        this.createdDate = other.createdDate;
        this.colorDataList = other.colorDataList;
    }
}
