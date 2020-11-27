package com.main.glory.model.machine.response;

import com.main.glory.model.machine.MachineRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllMachine {
    Long id;
    String machineName;
    List<GetAllMachineRecord> getAllMachineRecords;

}
