package com.main.glory.model.qualityProcess;

import com.main.glory.model.user.UserPermission;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
@Table
@NoArgsConstructor
public class QualityProcessData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    Long id;
    @ApiModelProperty(hidden = true)
    Long controlId;
    String stepName;
    Long stepPosotion;
    String functionName;
    String functionValue;
    Long functionPosotion;

    //WaterControlFunction..
    Boolean isWaterControl;
    String waterType;
    String drainType;
    String fabricRatio;
    Boolean jetLevel;

    //PumpControlFunction..
    Boolean isPumpControl;
    Long pumpSpeed;

    //TempControlFunction..
    Boolean isTempControl;
    String setValue;
    String rateOfRise;
    String holdTime;
    String pressure;

    //DoasingControlFunction..
    Boolean isDosingControl;
    Boolean haveDose;
    String doseAtTemp;
    String fillType;
    String dosingPercentage;
    String doseWhileHeating;
    String doseType;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "qualityProcessControlId", referencedColumnName = "id")
    private List<Chemical> dosingChemical;

    //OperatorMessageFunction..
    Boolean isOperatorMessage;
    String operatorCode;
    String operatorMessage;
    String startAtTemp;


}
