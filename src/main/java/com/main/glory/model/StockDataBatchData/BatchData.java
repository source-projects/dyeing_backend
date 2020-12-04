package com.main.glory.model.StockDataBatchData;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.jobcard.JobData;
import com.main.glory.model.jobcard.JobMast;
import com.main.glory.model.shade.ShadeData;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "batchData")
public class BatchData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Double mtr;
    Double wt;
    String batchId;
    Long controlId;
    Boolean isProductionPlanned;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "batchEntryId", referencedColumnName = "id")
    private List<JobData> jobData;


    @PrePersist
    protected void onCreate() {
        this.isProductionPlanned = false;
    }
}
