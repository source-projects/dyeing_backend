package com.main.glory.model.machine.response;

import com.main.glory.model.machine.BoilerMachineRecord;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BoilerFilter <T>{
    Long controlId;
    Date date;
    Long time;
    T value;

    public BoilerFilter(BoilerMachineRecord boilerMachineRecord1, T data) {
        this.controlId=boilerMachineRecord1.getControlId();
        this.date=boilerMachineRecord1.getDateToEnter();
        this.time=boilerMachineRecord1.getTimeOf();
        this.value=data;

    }
}
