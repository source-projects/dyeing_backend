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
    Long companyId;
    Long departmentId;
    Long designationId;
    Long userHeadId;
    Long updatedBy;
    String password;
    private UserPermission userPermissionData;

}
