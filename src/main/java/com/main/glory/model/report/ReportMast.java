package com.main.glory.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String type;
    String urlForExcel;
    String urlForReport;

    public ReportMast(ReportMast reportMast) {
        this.id=reportMast.getId();
        this.name = reportMast.getName();
        this.urlForExcel = reportMast.getUrlForExcel();
        this.urlForReport = reportMast.getUrlForReport();
        this.type = reportMast.getType();
    }

    public ReportMast(String name, String type, String urlForExcel,String urlForReport) {
        this.name = name;
        this.type = type;
        this.urlForExcel = urlForExcel;
        this.urlForReport = urlForReport;
    }
}
