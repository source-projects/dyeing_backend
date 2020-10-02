package com.main.glory.model.batch;

import lombok.*;

import javax.persistence.*;
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
    private Long id;
    private Long controlId;
    private Long gr;
    private Long lot_no;
    private Long noOfConesTaka;
    private Double mtr;
    private Double wt;
    private String unit;
    private String detail;
    private Long fabInId;

    public BatchData(BatchData other) {
        this.id = other.id;
        this.controlId = other.controlId;
        this.gr = other.gr;
        this.lot_no = other.lot_no;
        this.noOfConesTaka = other.noOfConesTaka;
        this.mtr = other.mtr;
        this.wt = other.wt;
        this.unit = other.unit;
        this.detail = other.detail;
        this.batchGrDetails = other.batchGrDetails;
        this.fabInId = other.fabInId;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<BatchGrDetail> batchGrDetails;

}
