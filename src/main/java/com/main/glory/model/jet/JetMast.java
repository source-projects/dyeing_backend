package com.main.glory.model.jet;

import com.main.glory.model.jet.request.AddJet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class JetMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    Double capacity;
    Double liquorRatio;


    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<JetData> jetDataList;


    public JetMast(AddJet jetMast) {
        this.capacity=jetMast.getCapacity();
        this.name=jetMast.getName();
        this.liquorRatio =jetMast.getLiquorRatio();
    }
}
