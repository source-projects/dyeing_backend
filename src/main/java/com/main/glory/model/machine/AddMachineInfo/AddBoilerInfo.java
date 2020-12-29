package com.main.glory.model.machine.AddMachineInfo;

import com.main.glory.model.machine.BoilerMachineRecord;
import jdk.jfr.StackTrace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AddBoilerInfo {
    Long jetRunning;
    List<AddBoilerMachineRecord> boilerMachineRecord;
}
