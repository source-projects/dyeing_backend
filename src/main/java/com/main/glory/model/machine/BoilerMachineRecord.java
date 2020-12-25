package com.main.glory.model.machine;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table

public class BoilerMachineRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Double streamPressusre;
    Long drumWaterLevel;
    Long feedPump;
    Long flueGasTemp;
    Long bedTemp;
    Double draftPressure;
    Long idFan;
    Long daOne;
    Long daTwo;
    Long daThree;
    Long screwFeeder;
    Long waterMeter;
    Long loadData;
    Long jetRunning;
    String timeOf;
    String dateToEnter;
    Date createdDate;
    Long controlId;
    Long userHeadId;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis()+19800000);
    }


}
