package com.main.glory.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="program")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Program {

 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
// @ApiModelProperty(hidden = true)
    private Long id;
    private Long party_id;
    private String quality_id;
    private Long quality_entry_id;
    private String program_given_by;
    private String remark;
    private Date created_date;
    private Date updated_date;
    private String created_by;
    private String updated_by;
    private Long user_head_id;
    private String priority;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "program_control_id", referencedColumnName = "id")
    private List<ProgramRecord> program_record;

//    @OneToOne
//    @JoinColumn(name="entry_id", referencedColumnName = "party_id")
//     Party party;
}
