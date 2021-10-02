package com.main.glory.model.color;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

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

}
