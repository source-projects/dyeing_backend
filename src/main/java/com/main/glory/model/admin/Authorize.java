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
public class Authorize {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String contact;
    String email;
    String type;//receive by and approved
    Long departmentId;

    public Authorize(Authorize authorize) {
        this.id=authorize.getId();
        this.name= authorize.getName();
        this.contact = authorize.getContact();
        this.email = authorize.getEmail();
        this.type = authorize.getType();
        this.departmentId = authorize.getDepartmentId();
    }
}
