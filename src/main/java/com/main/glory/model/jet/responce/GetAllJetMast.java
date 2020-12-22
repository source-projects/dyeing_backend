package com.main.glory.model.jet.responce;


import com.main.glory.model.jet.JetMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllJetMast {
    Long id;
    String name;
    Double capacity;

    public GetAllJetMast(JetMast jetMast) {
        this.id=jetMast.getId();
        this.name=jetMast.getName();
        this.capacity=jetMast.getCapacity();
    }
}
