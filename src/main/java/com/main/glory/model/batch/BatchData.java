package com.main.glory.model.batch;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "OldBatchData")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BatchData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long controlId;
    private Long gr;
    private Double mtr;
    private Double wt;
    private String unit;
    private String detail;
    private Long fabInId;
    private Date createdDate;
    private Date updatedDate;

    public BatchData(BatchData other) {
        this.id = other.id;
        this.controlId = other.controlId;
        this.gr = other.gr;
        this.mtr = other.mtr;
        this.wt = other.wt;
        this.unit = other.unit;
        this.detail = other.detail;
        this.fabInId = other.fabInId;
        this.createdDate = other.createdDate;
        this.updatedDate = other.updatedDate;
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }
}
