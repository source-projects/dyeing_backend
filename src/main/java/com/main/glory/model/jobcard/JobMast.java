package com.main.glory.model.jobcard;

import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.program.ProgramRecord;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class JobMast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long partyId;
    Long qualityEntryId;
    Long createdBy;
    Long updatedBy;
    Date createdDate;
    Date updatedDate;
    Long userHeadId;
    Boolean isDone;
    Boolean isBillGenerated;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<JobData> jobData;

    public JobMast(StockMast x) {
        this.partyId=x.getPartyId();
        this.qualityEntryId=x.getQualityId();
        this.createdBy=x.getCreatedBy();
        this.updatedBy=x.getUpdatedBy();
        this.userHeadId=x.getUserHeadId();
    }


    @PrePersist
    protected void onCreate() {
        this.isDone=false;
        this.isBillGenerated=false;
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }



}
