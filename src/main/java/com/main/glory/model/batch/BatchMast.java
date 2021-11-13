package com.main.glory.model.batch;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "batchMast")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BatchMast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long qualityId;

    private String remark;
    private String createdBy;
    private Date createdDate;
    private Date updatedDate;
    private String updatedBy;
    private Integer userHeadId;
    private Boolean isProductionPlaned;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<OldBatchData> oldBatchData;

    public BatchMast(Long id, Long qualityId, String remark, String createdBy, Date createdDate, Date updatedDate, String updatedBy, Integer userHeadId, Boolean isProductionPlaned) {
        this.id = id;
        this.qualityId = qualityId;

        this.remark = remark;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
        this.userHeadId = userHeadId;
        this.isProductionPlaned = isProductionPlaned;
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
        isProductionPlaned = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }
}
