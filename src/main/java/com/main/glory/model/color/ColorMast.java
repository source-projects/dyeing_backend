package com.main.glory.model.color;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    Long supplierId;
    String billNo;
    Date billDate;
    String chlNo;
    Date chlDate;
    Double billAmount;
    Long createdBy;
    Long updatedBy;
    Long userHeadId;
    String remark;
    @ApiModelProperty(hidden = true)
    Date createdDate;
    @ApiModelProperty(hidden = true)
    Date updatedDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId",referencedColumnName = "id")
    List<ColorData> colorDataList;

    public ColorMast(Long id, Long supplierId, String billNo, Date billDate, String chlNo, Date chlDate, Double billAmount, Long createdBy, Long updatedBy, Long userHeadId, String remark, List<ColorData> colorDataList) {
        this.id = id;
        this.supplierId = supplierId;
        this.billNo = billNo;
        this.billDate = billDate;
        this.chlNo = chlNo;
        this.chlDate = chlDate;
        this.billAmount = billAmount;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.userHeadId = userHeadId;
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

    public ColorMast(ColorMast other) {
        this.id = other.id;
        this.supplierId = other.supplierId;
        this.billNo = other.billNo;
        this.billDate = other.billDate;
        this.chlNo = other.chlNo;
        this.chlDate = other.chlDate;
        this.billAmount = other.billAmount;
        this.createdBy = other.createdBy;
        this.updatedBy = other.updatedBy;
        this.userHeadId = other.userHeadId;
        this.remark = other.remark;
        this.createdDate = other.createdDate;
        this.colorDataList = other.colorDataList;
    }
}
