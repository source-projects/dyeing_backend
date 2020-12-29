package com.main.glory.model.machine.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetRecordBasedOnFilter {

    Long toTime;
    Long fromTime;
    String toDate;
    String fromDate;
    String attribute;
    Long controlId;
}
