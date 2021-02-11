package com.main.glory.model.shade;

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
public class ACP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String preFix="ACP";
    Long postFix;
    Boolean status;

    public ACP(Long i) {
        this.postFix=i;
    }
}
