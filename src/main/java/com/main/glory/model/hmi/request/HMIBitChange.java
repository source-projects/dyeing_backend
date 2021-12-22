package com.main.glory.model.hmi.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HMIBitChange {
    Boolean sco;
    Boolean doseNylon;
    Long jetId;
    Long productionId;
}
