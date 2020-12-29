package com.main.glory.model.machine.response;

import com.main.glory.model.machine.BoilerMachineRecord;
import com.main.glory.model.machine.Thermopack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ThermopackFilterRecord <T>{
    Long controlId;
    Date date;
    Long time;
    T value;

    public ThermopackFilterRecord(Thermopack record, T data) {
        this.controlId=record.getControlId();
        this.date=record.getDateToEnter();
        this.time=record.getTimeOf();
        this.value=data;

    }


}
