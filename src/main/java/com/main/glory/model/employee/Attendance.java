package com.main.glory.model.employee;

import com.main.glory.model.employee.request.GetLatestAttendance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    Long controlId;
    Date date;
    Date inTime;
    Date outTime;
    Date createdDate;
    Long createdBy;
    Long updatedBy;
    Date updatedDate;
    Boolean shift; //true:morning
    String url;
    String outUrl;
    @ColumnDefault("false")
    Boolean approved;

    public Attendance(GetLatestAttendance record, EmployeeMast employeeMast) {
        this.inTime = record.getDate();
        this.controlId = employeeMast.getId();
    }

    @PrePersist
    public void onCreate()
    {
        this.createdDate = new Date(System.currentTimeMillis());
    }
    @PreUpdate
    public void onUpdate()
    {
        this.updatedDate = new Date(System.currentTimeMillis());
    }


}
