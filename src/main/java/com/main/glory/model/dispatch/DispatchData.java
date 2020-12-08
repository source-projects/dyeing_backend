package com.main.glory.model.dispatch;

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
public class DispatchData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long batchEntryId;
    Long controlId;

}
