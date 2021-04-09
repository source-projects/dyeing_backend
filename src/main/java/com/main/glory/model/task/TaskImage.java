package com.main.glory.model.task;

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
public class TaskImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long controlId;
    String url;
    String type;
}
