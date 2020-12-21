package com.main.glory.model.jet.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddJetData {
    Long id;
    Long controlId;
    Long sequence;
    Long productionId;
}
