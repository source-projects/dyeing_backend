package com.main.glory.model.dyeingSlip;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dyeingProcess.DyeingProcessData;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class DyeingSlipData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long controlId;
    String processType;
    Double temp;
    Double holdTime;
    Long sequence;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    Boolean isColor;
    Double liquerRation;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<DyeingSlipItemData> dyeingSlipItemData;


    public DyeingSlipData(DyeingProcessData dyeingProcessData) {
        this.processType = dyeingProcessData.getProcessType();
        this.temp=dyeingProcessData.getTemp();
        this.holdTime=dyeingProcessData.getHoldTime();
        this.sequence=dyeingProcessData.getSequence();
        this.isColor=dyeingProcessData.getIsColor();
        this.liquerRation=dyeingProcessData.getLiquerRation();

    }

    public DyeingSlipData(DyeingSlipData dyeingSlipData) {
        this.controlId=dyeingSlipData.getControlId();
        this.processType=dyeingSlipData.getProcessType();
        this.temp=dyeingSlipData.getTemp();
        this.holdTime= dyeingSlipData.getHoldTime();
        this.sequence = dyeingSlipData.getSequence();
        this.isColor = dyeingSlipData.getIsColor();
        this.liquerRation= dyeingSlipData.getLiquerRation();
        this.dyeingSlipItemData=dyeingSlipData.getDyeingSlipItemData();
    }

}
