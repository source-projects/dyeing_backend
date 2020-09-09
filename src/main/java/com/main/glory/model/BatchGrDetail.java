package com.main.glory.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "batchGrDetail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BatchGrDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Long id;
    private Double quantity;
    private Long control_id;
    private String state;
    private Boolean is_active;
}
