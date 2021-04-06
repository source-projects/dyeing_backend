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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<EmployeeData> employeeDocumentList;



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
