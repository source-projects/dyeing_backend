package com.main.glory.model.machine.response;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetAllMachineRecord {
    Long id;
    Double speed;
    Double mtr;
    Date createdDate;
    Date updatedDate;
    Long controlId;

}
