package com.main.glory.model.waterJet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class WaterJet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double mtr;
    private Double efficieny;

}
