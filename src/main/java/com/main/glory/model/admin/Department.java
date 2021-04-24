package com.main.glory.model.admin;

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
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    @Column(columnDefinition = "boolean default false")
    Boolean isMaster;

    public Department(Department c) {
        this.id=c.getId();
        this.name=c.getName();
        this.isMaster = c.getIsMaster()==null?false:c.getIsMaster();
    }
}
