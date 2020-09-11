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
    private Long control_id;
    private String gr;
    private Long quality_id;
    private Long quality_entry_id;
    private String quality_name;
    private String quality_type;
    private Double mtr;
    private Double wt;
    private Long no_of_cones;
    private Long no_of_boxes;
    private Date created_date;
    private Date updated_date;
    private String created_by;
    private String updated_by;
    @Column(columnDefinition = "bit(1) default 1")
    private boolean is_active;

    public FabricInRecord(Long control_id, String gr, Long quality_id, Long quality_entry_id, String quality_name, String quality_type, Double mtr, Double wt, Long no_of_cones, Long no_of_boxes) {

        this.control_id = control_id;
        this.gr = gr;
        this.quality_id = quality_id;
        this.quality_entry_id = quality_entry_id;
        this.quality_name = quality_name;
        this.quality_type = quality_type;
        this.mtr = mtr;
        this.wt = wt;
        this.no_of_cones = no_of_cones;
        this.no_of_boxes = no_of_boxes;
        this.is_active = is_active;
    }
}
