package com.main.glory.model.machine;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Entity
@Setter
@Getter
@Table
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MachineRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Double speed;
    Double totalMtr;
    Date createdDate;
    Date updatedDate;
    Long controlId;



    public MachineRecord(MachineMast machineMast, MachineRecord machineRecordExist, Double speed) {
        this.speed=speed;
        this.controlId=machineMast.getId();
        this.totalMtr= machineRecordExist.getTotalMtr()+(speed/6);
    }

    public MachineRecord(MachineMast machineMast, Double speed) {
        this.speed=speed;
        this.controlId= machineMast.getId();
        this.totalMtr=speed/6;

    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }



}
