package com.main.glory.model.employee;


import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.employee.request.AddEmployee;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class EmployeeMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String contact;
    String aadhaar;
    String remark;
    String remark2;
    String remark3;
    Long createdBy;
    Long updatedBy;
    Date createdDate;
    Date updatedDate;
    Long empId;
    Long departmentId;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<EmployeeData> employeeDocumentList;

    public EmployeeMast(EmployeeMast e) {

        this.id=e.getId();
        this.name=e.getName();
        this.contact=e.getContact();
        this.aadhaar=e.getAadhaar();
        this.remark=e.getRemark();
        this. remark2=e.getRemark2();
        this.remark3=e.getRemark3();
        this.createdBy=e.getCreatedBy();
        this.updatedBy=e.getUpdatedBy();
        this.createdDate=e.getCreatedDate();
        this.updatedDate=e.getUpdatedDate();
        this.empId=e.getEmpId();
        this.departmentId=e.getDepartmentId();
        this.employeeDocumentList = e.getEmployeeDocumentList();
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
