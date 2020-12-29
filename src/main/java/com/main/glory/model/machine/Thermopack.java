package com.main.glory.model.machine;

import com.main.glory.model.machine.AddMachineInfo.AddThermopackInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Thermopack {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long forwardTemp;
    Long returnTemp;
    Long stackTemp;
    Long furnaceTemp;
    Long pumpData;
    Long idFan;
    Date dateToEnter;
    Long fdFan;
    Long screwFeeder;
    Long userHeadId;
    Date createdDate;
    Long timeOf;
    Long controlId;

    public Thermopack(AddThermopackInfo thermopack) throws ParseException {
        this.forwardTemp=thermopack.getForwardTemp();
        this.returnTemp=thermopack.getReturnTemp();
        this.stackTemp=thermopack.getStackTemp();
        this.furnaceTemp=thermopack.getFurnaceTemp();
        this.pumpData=thermopack.getPumpData();
        this.idFan=thermopack.getIdFan();
        this.fdFan=thermopack.getFdFan();
        this.screwFeeder=thermopack.getScrewFeeder();
        this.userHeadId=thermopack.getUserHeadId();
        this.timeOf=thermopack.getTimeOf();
        this.controlId=thermopack.getControlId();
        //date to time
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date lFromDate2 = datetimeFormatter1.parse(thermopack.getDateToEnter());

        Timestamp fromTS2 = new Timestamp(lFromDate2.getTime());

        this.dateToEnter=fromTS2;


    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }



}
