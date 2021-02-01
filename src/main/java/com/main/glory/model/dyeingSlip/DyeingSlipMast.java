package com.main.glory.model.dyeingSlip;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dyeingProcess.DyeingProcessData;
import com.main.glory.model.productionPlan.ProductionPlan;
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
@Getter
@Setter
@NoArgsConstructor
@Entity
public class DyeingSlipMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long userHeadId;
    Long createdBy;
    Long updatedBy;
    Date createdDate;
    Date updatedDate;
    Long stockId;
    Long jetId;
    Long productionId;
    String batchId;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<DyeingSlipData> dyeingSlipDataList;


    @PrePersist
    void onCreate(){this.createdDate=new Date(System.currentTimeMillis());}


}
