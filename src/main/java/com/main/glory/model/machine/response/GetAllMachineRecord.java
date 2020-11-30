package com.main.glory.model.machine.response;


import com.main.glory.model.machine.MachineRecord;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetAllMachineRecord {
    Long id;
    Double speed;
    Double mtr;
    Date createdDate;
    Date updatedDate;
    Long controlId;

    public GetAllMachineRecord(MachineRecord machineRecord) {
        this.id = machineRecord.getId();
        this.speed=machineRecord.getSpeed();
        this.mtr=machineRecord.getSpeed()/6;
        this.createdDate = machineRecord.getCreatedDate();
        this.updatedDate=machineRecord.getUpdatedDate();
        this.controlId=machineRecord.getControlId();
    }
}
