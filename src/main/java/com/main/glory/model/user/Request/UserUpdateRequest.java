package com.main.glory.model.user.Request;

import com.main.glory.model.user.UserPermission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateRequest {
    Long id;
    String userName;
    String firstName;
    String lastName;
    String email;
    Long contact;
    String password;
    String company;
    String department;
    Long designationId;
    Long userHeadId;
    private UserPermission userPermissionData;

}
