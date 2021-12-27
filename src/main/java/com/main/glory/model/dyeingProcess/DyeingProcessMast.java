package com.main.glory.model.dyeingProcess;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dyeingProcess.DyeingPLC.DyeingplcMast;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.shade.ShadeMast;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
public class DyeingProcessMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long userHeadId;
    Long createdBy;
    Long updatedBy;
    Date createdDate;
    Date updatedDate;
    String processName;
    Boolean scb;



    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<DyeingProcessData> dyeingProcessData;

    public DyeingProcessMast(DyeingProcessMast dyeingProcessMast) {
        this.id = dyeingProcessMast!=null?dyeingProcessMast.getId():null;
        this.userHeadId = dyeingProcessMast==null?null:dyeingProcessMast.getUserHeadId();
        this.createdBy = dyeingProcessMast!=null?dyeingProcessMast.getCreatedBy():null;
        this.updatedBy = dyeingProcessMast!=null?dyeingProcessMast.getUpdatedBy():null;
        this.createdDate = dyeingProcessMast!=null?dyeingProcessMast.getCreatedDate():null;
        this.updatedDate= dyeingProcessMast==null?null:dyeingProcessMast.getUpdatedDate();
        this.processName = dyeingProcessMast!=null?dyeingProcessMast.getProcessName():null;
        this.dyeingProcessData =dyeingProcessMast!=null?dyeingProcessMast.getDyeingProcessData():null;
    }


    @PrePersist
    protected void onCreate(){this.createdDate=new Date(System.currentTimeMillis());}
    @PreUpdate
    protected void onUpdate()
    {
        this.updatedDate = new Date(System.currentTimeMillis());

    }




}
