package com.main.glory.model.machine;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@ToString
public class MachineCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;


    public MachineCategory(MachineCategory machineCategory) {
        this.id=machineCategory.id;
        this.name=machineCategory.name;
    }
}
