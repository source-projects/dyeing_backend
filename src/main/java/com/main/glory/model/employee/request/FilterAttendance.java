package com.main.glory.model.employee.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterAttendance {
    Long controlId;
    Date fromDate;
    Date toDate;
}
