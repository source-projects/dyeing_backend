package com.main.glory.model.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ReportType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String formName;
    String url;


    public ReportType(ReportType record) {
        this.id=record.id;
        this.formName=record.formName;
        this.url=record.url;
    }


}
