package com.main.glory.model.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
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


    public UserPermission(Long id, Long pa, Long qu, Long u, Long sb, Long prg, Long sh, Long su, Long sr, Long cs, Long pr, Long pp, Long jp) {
        this.id = id;
        this.pa = pa;
        this.qu = qu;
        this.u = u;
        this.sb=sb;
        this.prg = prg;
        this.sh = sh;
        this.su = su;
        this.sr = sr;
        this.cs = cs;
        this.pr = pr;
        this.pp = pp;
        this.jp = jp;
    }

    @PrePersist
    protected void onCreate() {}

}
