package com.main.glory.model.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="userPermission")
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long pa;
    Long qu;
    Long u;
    Long sb;
    Long prg;
    Long sh;
    Long su;
    Long sr;
    Long cs;
    Long pr;
    Long pp;
    Long jp;
    Long pt;
    Long d;
    Long bf;
    Long ip;
    Long wt;
    Long ad;
    Long ds;



}
