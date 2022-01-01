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
@Table(name = "rfsequence")
public class RFSequence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long sequence;
}
