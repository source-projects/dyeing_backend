package com.main.glory.model.admin.request;

import com.main.glory.model.admin.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class DepartmentResponse extends Department {
    Long userId;

    public DepartmentResponse(Long userId) {
        this.userId = userId;
    }

    public DepartmentResponse(Department c, Long userId) {
        super(c);
        this.userId = userId;
    }
}
