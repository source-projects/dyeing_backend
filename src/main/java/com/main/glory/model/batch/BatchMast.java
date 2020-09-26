package com.main.glory.model.batch;

import com.main.glory.model.batch.BatchData;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long qualityId;
    private Date date;
    private String remark;
    private String createdBy;
    private Date createDate;
    private Date updatedDate;
    private String updatedName;
    private Integer userHeadId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<BatchData> batchData;

}
