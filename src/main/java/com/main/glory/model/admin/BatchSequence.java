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
public class BatchSequence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long sequence;

    public BatchSequence(BatchSequence batchSequence, BatchSequence record) {
        this.id= batchSequence.getId();
        this.sequence=record.getSequence();
    }
}
