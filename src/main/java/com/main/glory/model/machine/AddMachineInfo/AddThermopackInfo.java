package com.main.glory.model.machine.AddMachineInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddThermopackInfo {
    Long forwardTemp;
    Long returnTemp;
    Long stackTemp;
    Long furnaceTemp;
    Long pumpData;
    Long idFan;
    String dateToEnter;
    Long fdFan;
    Long screwFeeder;
    Long userHeadId;
    Date createdDate;
    Long timeOf;
    Long controlId;
}
