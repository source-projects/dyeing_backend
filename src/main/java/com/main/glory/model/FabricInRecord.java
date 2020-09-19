package com.main.glory.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "fabstock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FabricInRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	private Long id;
    private Long controlId;
    private String gr;
    private Long qualityId;
    private Long qualityEntryId;
    private String qualityName;
    private String qualityType;
    private Double mtr;
    private Double wt;
    private Long noOfCones;
    private Long noOfBoxes;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
//    @Column(columnDefinition = "bit(1) default 1")
    private String isActive;
//
//    public FabricInRecord(Long controlId, String gr, Long quality_id, Long quality_entry_id, String quality_name, String quality_type, Double mtr, Double wt, Long no_of_cones, Long no_of_boxes) {
//
//        this.controlId = controlId;
//        this.gr = gr;
//        this.qualityId = qualityId;
//        this.quality_entry_id = quality_entry_id;
//        this.quality_name = quality_name;
//        this.quality_type = quality_type;
//        this.mtr = mtr;
//        this.wt = wt;
//        this.no_of_cones = no_of_cones;
//        this.no_of_boxes = no_of_boxes;
//        this.is_active = is_active;
//    }
}
