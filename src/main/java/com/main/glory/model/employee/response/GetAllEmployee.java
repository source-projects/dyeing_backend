package com.main.glory.model.employee.response;

import com.main.glory.model.employee.EmployeeMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllEmployee extends EmployeeMast {
    String departmentName;

    public GetAllEmployee(EmployeeMast e,String departmentName) {
        super(e);
        this.departmentName = departmentName;
    }
}
