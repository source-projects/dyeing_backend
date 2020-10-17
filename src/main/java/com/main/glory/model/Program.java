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
 @GeneratedValue(strategy = GenerationType.IDENTITY)
// @ApiModelProperty(hidden = true)
    private Long id;
    private Long partyId;
    private String qualityId;
    private Long qualityEntryId;
    private String qualityType;
    private String qualityName;
    private String programGivenBy;
    private String remark;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
    private Long userHeadId;
    private String priority;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "program_control_id", referencedColumnName = "id")
    private List<ProgramRecord> program_record;

//    @OneToOne
//    @JoinColumn(name="entry_id", referencedColumnName = "party_id")
//     Party party;
}
