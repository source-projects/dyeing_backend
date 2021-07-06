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



    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<DyeingProcessData> dyeingProcessData;

    public DyeingProcessMast(DyeingProcessMast dyeingProcessMast) {
        this.id = dyeingProcessMast.getId();
        this.userHeadId = dyeingProcessMast.getUserHeadId()==null?null:dyeingProcessMast.getUserHeadId();
        this.createdBy = dyeingProcessMast.getCreatedBy();
        this.updatedBy = dyeingProcessMast.getUpdatedBy();
        this.createdDate = dyeingProcessMast.getCreatedDate();
        this.updatedDate= dyeingProcessMast.getUpdatedDate()==null?null:dyeingProcessMast.getUpdatedDate();
        this.processName = dyeingProcessMast.getProcessName();
        this.dyeingProcessData =dyeingProcessMast.getDyeingProcessData();
    }


    @PrePersist
    protected void onCreate(){this.createdDate=new Date(System.currentTimeMillis());}
    @PreUpdate
    protected void onUpdate()
    {

        this.updatedDate = this.getUpdatedDate()==null?new Date(System.currentTimeMillis()):this.getUpdatedDate();
    }




}
