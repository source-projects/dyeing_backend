/*
package com.main.glory.model.dyeingSlip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


public class AdditionDyeingProcessSlip {

    Long id;
    Long userHeadId;
    Long createdBy;
    Long updatedBy;
    Date createdDate;
    Date updatedDate;

    String name;
    Long stockId;
    Long productionId;
    String batchId;
    Long processId;


    @PrePersist
    public void onCreate()
    {
        this.createdDate = new Date(System.currentTimeMillis());
    }


}
*/
