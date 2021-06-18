package com.main.glory.model.employee.response;

import com.main.glory.model.employee.EmployeeData;
import com.main.glory.model.employee.EmployeeMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MonthlyAttendanceResponse {
    Long id;
    Long empId;
    String name;
    EmployeeData employeeData;
    String profile;
    Long approved;
    Long notApproved;
    Long totalPresent;
    Long totalAbsent;
    Date fromDate;
    Date toDate;

    public MonthlyAttendanceResponse(EmployeeMast e, Long approved, Long notApproved) {
        this.id = e.getId();
        this.empId = e.getEmpId();
        this.name = e.getName();
        //this.employeeData =e.getEmployeeDocumentList().stream().filter(profileImage ->"profile".equalsIgnoreCase(profileImage.getType())).findAny().orElse(null);
        //this.profile = employeeData==null?null:employeeData.getUrl();
        this.approved = approved;
        this.notApproved = notApproved;
    }

    public MonthlyAttendanceResponse(Long id, String name, Long empId, EmployeeData employeeDocumentList) {
        this.id = id;
        this.name = name;
        this.empId = empId;
        //this.employeeData =employeeDocumentList.stream().filter(profileImage ->"profile".equalsIgnoreCase(profileImage.getType())).findAny().orElse(null);
        this.profile = employeeDocumentList==null?null:employeeDocumentList.getUrl();

    }
}
