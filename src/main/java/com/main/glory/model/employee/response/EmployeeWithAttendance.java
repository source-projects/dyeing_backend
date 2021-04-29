package com.main.glory.model.employee.response;

import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.EmployeeMast;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeWithAttendance {
    EmployeeMast employeeMast;
    Attendance attendanceLatest;

    public EmployeeWithAttendance(EmployeeMast employeeMastExist, Attendance attendance) {
        this.employeeMast=employeeMastExist;
        this.attendanceLatest=attendance;

    }

    public EmployeeWithAttendance(EmployeeMast employeeMast) {
        this.employeeMast = employeeMast;
    }
}
