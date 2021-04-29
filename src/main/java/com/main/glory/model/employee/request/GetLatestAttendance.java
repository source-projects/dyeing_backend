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
public class GetLatestAttendance {
    Long empId;
    Date date;
    Boolean saveFlag;
    String url;

}
