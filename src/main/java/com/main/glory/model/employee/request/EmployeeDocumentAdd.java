package com.main.glory.model.employee.request;

import com.main.glory.model.employee.EmployeeData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDocumentAdd {
    Long controlId;
    EmployeeData employeeDocumentList;
}
