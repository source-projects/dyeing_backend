package com.main.glory.model.machine.UpdateMachineInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateBoilerRecordList {
    Long id;
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
    Long timeOf;
    String dateToEnter;
    Long controlId;
    Long userHeadId;
}
