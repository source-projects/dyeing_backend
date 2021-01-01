package com.main.glory.model.jet.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToJet {
    Long jetId;
    Long productionId;
    Long sequence;

}
