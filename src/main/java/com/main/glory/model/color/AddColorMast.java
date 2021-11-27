package com.main.glory.model.color;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddColorMast {
    
    Long id;
    Long supplierId;
    
    String billNo;
    Date billDate;
    String chlNo;
    Date chlDate;
    Double billAmount;
    
    Long createdBy;
    Long updatedBy;
    Long userHeadData;

    String remark;
    Date createdDate;
    Date updatedDate;

    List<ColorData> colorDataList;

    public AddColorMast(ColorMast colorMast) {

        this.id=colorMast.getId();
        this.supplierId=colorMast.getSupplier()==null?null:colorMast.getSupplier().getId();
        this.billNo=colorMast.billNo;
        this.billDate = colorMast.billDate;
        this. chlNo=colorMast.chlNo;
        this.chlDate=colorMast.chlDate;
        this.billAmount=colorMast.billAmount;
        this. createdBy=colorMast.getCreatedBy()==null?null:colorMast.getCreatedBy().getId();
        this.updatedBy=colorMast.getUpdatedBy()==null?null:colorMast.getUpdatedBy().getId();
        this.userHeadData= colorMast.getUserHeadData()==null?null:colorMast.getUserHeadData().getId();
        this. remark=colorMast.remark;
        this.createdDate=colorMast.createdDate;
        this.updatedDate=colorMast.updatedDate;
        this.colorDataList=colorMast.colorDataList;

    }
}
