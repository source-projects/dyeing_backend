package com.main.glory.model.employee.responce;

import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.EmployeeMast;
import lombok.AllArgsConstructor;
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
}
