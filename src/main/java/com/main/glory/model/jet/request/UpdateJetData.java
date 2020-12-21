package com.main.glory.model.jet.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateJetData {
    Long id;
    Long controlId;
    Long sequence;
    Long productionId;
}
