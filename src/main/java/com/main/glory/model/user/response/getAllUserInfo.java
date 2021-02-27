package com.main.glory.model.user.response;


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
    String company;
    String designation;
    Long createdBy;

    public getAllUserInfo(UserData e) {
        this.id=e.getId();
        this.userName=e.getUserName();
        this.firstName=e.getFirstName();
        this.lastName=e.getLastName();
        this.userHeadId=e.getUserHeadId();
        this.company=e.getCompany();
        this.designation=e.getDesignationId().getDesignation();
        this.createdBy=e.getCreatedBy();
    }
}
