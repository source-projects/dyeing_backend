package com.main.glory.model.machine.AddMachineInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AddBoilerMachineRecord {
    Double streamPressusre;
    Long drumWaterLevel;
    Long feedPump;
    Long flueGasTemp;
    Long bedTemp;
    Double draftPressure;
    Long idFan;
    Long jetRunning;
    Long daOne;
    Long daTwo;
    Long daThree;
    Long screwFeeder;
    Long waterMeter;
    Long loadData;
    String timeOf;
    String dateToEnter;
    Date createdDate;
    Long controlId;
    Long userHeadId;

}
