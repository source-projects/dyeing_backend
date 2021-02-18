package com.main.glory.model.jet.request;

import com.main.glory.model.jet.JetMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddJet {

    Long id;
    String name;
    Double capacity;
    Double liquorRatio;


    public AddJet(JetMast jetMast) {
        this.id=jetMast.getId();
        this.name=jetMast.getName();
        this.capacity=jetMast.getCapacity();
        this.liquorRatio=jetMast.getLiquorRatio();
    }
}
