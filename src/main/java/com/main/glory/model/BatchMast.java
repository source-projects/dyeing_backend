package com.main.glory.model;

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

   //@Column(name = "entry_id")
    private Long id;
    private Long quality_id;
    private Date date;
    private String remark;
    private String created_by;
    private Date create_date;
    private Boolean is_active;
    private Date updated_date;
    private String updated_name;
    private Integer batch_id;
    private Integer bunch_id;
    private Integer user_head_id;

    @Transient
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "control_id", referencedColumnName = "id")
    private List<BatchData> batchData;

}
