package com.main.glory.model.jet.responce;


import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllJetMast {
    Long id;
    String name;
    Double capacity;
    List<GetJetData> jetDataList;

    public GetAllJetMast(JetMast jetMast) {
        this.id=jetMast.getId();
        this.name=jetMast.getName();
        this.capacity=jetMast.getCapacity();
    }

    public GetAllJetMast(Long id, String name, Double capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }
}
