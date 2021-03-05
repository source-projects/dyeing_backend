package com.main.glory.model.user.response;


import com.main.glory.model.admin.Company;
import com.main.glory.model.admin.Department;
import com.main.glory.model.user.UserData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class getAllUserInfo {

    Long id;
    String userName;
    String firstName;
    String lastName;
    Long userHeadId;
    Long companyId;
    Long departmentId;
    String company;
    String designation;
    Long createdBy;



    public getAllUserInfo(UserData e, Company company, Department department) {
        this.id=e.getId();
        this.userName=e.getUserName();
        this.firstName=e.getFirstName();
        this.lastName=e.getLastName();
        this.userHeadId=e.getUserHeadId();
        this.companyId=e.getCompanyId();
        this.company = company.getName();
        this.designation=e.getDesignationId().getDesignation();
        this.createdBy=e.getCreatedBy();
    }
}
