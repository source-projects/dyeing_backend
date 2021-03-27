package com.main.glory.model.employee.response;


import com.main.glory.model.employee.EmployeeMast;
import com.main.glory.model.employee.request.FilterAttendance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeAttendanceResponse {

    Long controlId;
    String name;
    String aadhaar;
    String contact;
    Boolean shift;
    Long totalDays;
    Date fromDate;
    Date toDate;

    public EmployeeAttendanceResponse(Boolean shift, Long totalDays) {
        this.shift = shift;
        this.totalDays = totalDays;
    }

    public EmployeeAttendanceResponse(EmployeeAttendanceResponse employeeAttendanceResponse, FilterAttendance filterAttendance, EmployeeMast employeeMast) {
        this.controlId = employeeMast.getId();
        this.name=employeeMast.getName() ;
        this.aadhaar=employeeMast.getAadhaar() ;
        this.contact = employeeMast.getContact();
        this.shift=employeeAttendanceResponse.getShift();
        this.totalDays=employeeAttendanceResponse.getTotalDays();
        this.fromDate=filterAttendance.getFromDate();
        this.toDate=filterAttendance.getToDate();
    }
}
