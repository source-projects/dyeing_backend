package com.main.glory.model.batch;

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
    private Long id;
    private Double quantity;
    private Long controlId;
}
