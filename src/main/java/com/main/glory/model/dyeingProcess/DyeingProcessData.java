package com.main.glory.model.dyeingProcess;


import com.main.glory.model.jet.JetData;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
public class DyeingProcessData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long controlId;
    String processType;
    Double temp;
    Double holdTime;
    Long sequence;
    Boolean isColor;
    Double liquerRation;



    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<DyeingChemicalData> dyeingChemicalData;



}