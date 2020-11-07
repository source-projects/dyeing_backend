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
    Long userId;
    String remark;
    @ApiModelProperty(hidden = true)
    Date createdDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchaseId",referencedColumnName = "id")
    List<ColorData> colorDataList;

    public ColorMast() {
       this.createdDate = new Date(System.currentTimeMillis());
    }

    public ColorMast(ColorMast other) {
        this.id = other.id;
        this.supplierId = other.supplierId;
        this.billNo = other.billNo;
        this.billDate = other.billDate;
        this.chlNo = other.chlNo;
        this.chlDate = other.chlDate;
        this.billAmount = other.billAmount;
        this.userId = other.userId;
        this.remark = other.remark;
        this.createdDate = other.createdDate;
        this.colorDataList = other.colorDataList;
    }
}
