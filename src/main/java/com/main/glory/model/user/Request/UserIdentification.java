package com.main.glory.model.user.Request;

import com.main.glory.model.designation.Designation;
import com.main.glory.model.user.UserData;
import jdk.jfr.StackTrace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserIdentification {
    Long id;
    String name;
    String userDesignation;

    Long userHeadId;
    String userHeadName;
    String userHeadDesignation;
    Long superUserHeadId;
    String superUserHeadName;
    String superUserHeadDesignation;

    public UserIdentification(UserData userData, Designation designation) {
        this.id=userData.getId();
        this.userDesignation=designation.getDesignation();
    }

    public UserIdentification(UserData userData, Designation designation, UserData userHead, Designation designationHead) {
        this.id=userData.getId();
        this.userDesignation=designation.getDesignation();
        this.userHeadId=userHead.getId();
        this.userHeadDesignation = designationHead.getDesignation();
    }

    public UserIdentification(UserData userData, Designation designation, UserData userHead, Designation designationHead, UserData userSuperHead, Designation designationSuperHead) {
        this.id=userData.getId();
        this.userDesignation=designation.getDesignation();
        this.name=userData.getFirstName();
        this.userHeadId=userHead.getId();
        this.userHeadName=userHead.getFirstName();
        this.userHeadDesignation = designationHead.getDesignation();
        this.superUserHeadId=userSuperHead.getId();
        this.superUserHeadName=userSuperHead.getFirstName();
        this.superUserHeadDesignation=designationSuperHead.getDesignation();
    }
}
