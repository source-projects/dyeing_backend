package com.main.glory.model.batch;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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
    private Date createdDate;
    private Date updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }
}
