package com.main.glory.model.program;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="program")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Program {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
    // @ApiModelProperty(hidden = true)
     @NonNull
    private Long id;
    private Long partyId;
    private Long qualityEntryId;
    private String programGivenBy;
    private String remark;

    @ApiModelProperty(hidden = true)
    private Date createdDate;

    @ApiModelProperty(hidden = true)
    private Date updatedDate;

    @ApiModelProperty(hidden = true)
    private String createdBy;

    @ApiModelProperty(hidden = true)
    private String updatedBy;

    private Long userHeadId;
    private String priority;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "programControlId", referencedColumnName = "id")
    private List<ProgramRecord> programRecords;


   @PrePersist
   protected void onCreate() {
      this.createdDate = new Date(System.currentTimeMillis());
   }

   @PreUpdate
   protected void onUpdate() {
      this.updatedDate = new Date(System.currentTimeMillis());
   }


//    @OneToOne
//    @JoinColumn(name="entry_id", referencedColumnName = "party_id")
//     Party party;
}
