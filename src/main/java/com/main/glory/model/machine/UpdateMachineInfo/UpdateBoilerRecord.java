package com.main.glory.model.machine.UpdateMachineInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateBoilerRecord {
    Long jetRunning;
    List<UpdateBoilerRecordList> recordList;
}
