package com.main.glory.model.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class EmployeeSequence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long empId;

    public EmployeeSequence(long l) {
        this.empId = l;
    }
}
