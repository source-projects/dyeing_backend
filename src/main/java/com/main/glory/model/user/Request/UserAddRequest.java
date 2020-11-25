package com.main.glory.model.user.Request;

import com.main.glory.model.user.UserPermission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddRequest {
    String userName;
    String firstName;
    String lastName;
    String email;
    Long contact;
    String password;
    String company;
    String department;
    Long createdBy;
    Long designationId;
    Long userHeadId;
    private UserPermission userPermissionData;



}
