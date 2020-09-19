package com.main.glory.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "batchData")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BatchData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "entry_id")
    private Long id;
    private Long control_id;
    private Long gr;
    private Long lot_no;
    private Long no_of_cones_taka;
    private Double mtr;
    private Double wt;
    private Boolean is_active;
    private String state;
    private String unit;
    private String detail;
    private Long referenced_id;

    @Transient
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "control_id", referencedColumnName = "id")
    private List<BatchGrDetail> batchGrDetails;

}
