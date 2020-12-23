package com.main.glory.model.machine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    Long furnaceTemp;
    Long pumpData;
    Long idFan;
    Long fdFan;
    Long screwFeeder;
    Long userHeadId;
    Date createdDate;
    String timeOf;
    Long controlId;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis()+19800000);
    }



}
