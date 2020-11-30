package com.main.glory.model.machine.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetMachineByIdWithFilter {

    Long id;
    String toDate;
    String fromDate;
    String toTime;
    String fromTime;
    String shift;
}
