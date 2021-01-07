package com.main.glory.model.machine.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThermopackRecordBasedOnShift {
    Long thermopackId;
    String date;
    String shift;
}
