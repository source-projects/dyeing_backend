package com.main.glory.model.admin.request;

import com.main.glory.model.admin.Authorize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorizeWithDepartment extends Authorize {

    String departmentName;

    public AuthorizeWithDepartment(Authorize authorize,String departmentName)
    {
        super(authorize);
        this.departmentName = departmentName;
    }
}
