package com.main.glory.model.employee.request;

import com.main.glory.model.employee.EmployeeData;
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
public class AddEmployee {

    Long id;
    String name;
    String contact;
    String aadhaar;
    String remark;
    String remark2;
    String remark3;
    Long createdBy;
    Long updatedBy;
    List<EmployeeData> employeeDocumentList;


}
