package com.main.glory.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fabstock")
public class FabricInRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
    private boolean is_active;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getControl_id() {
		return control_id;
	}
	public void setControl_id(Long control_id) {
		this.control_id = control_id;
	}
	public String getGr() {
		return gr;
	}
	public void setGr(String gr) {
		this.gr = gr;
	}
	public Long getQuality_id() {
		return quality_id;
	}
	public void setQuality_id(Long quality_id) {
		this.quality_id = quality_id;
	}
	public Long getQuality_entry_id() {
		return quality_entry_id;
	}
	public void setQuality_entry_id(Long quality_entry_id) {
		this.quality_entry_id = quality_entry_id;
	}
	public String getQuality_name() {
		return quality_name;
	}
	public void setQuality_name(String quality_name) {
		this.quality_name = quality_name;
	}
	public String getQuality_type() {
		return quality_type;
	}
	public void setQuality_type(String quality_type) {
		this.quality_type = quality_type;
	}
	public Double getMtr() {
		return mtr;
	}
	public void setMtr(Double mtr) {
		this.mtr = mtr;
	}
	public Double getWt() {
		return wt;
	}
	public void setWt(Double wt) {
		this.wt = wt;
	}
	public Long getNo_of_cones() {
		return no_of_cones;
	}
	public void setNo_of_cones(Long no_of_cones) {
		this.no_of_cones = no_of_cones;
	}
	public Long getNo_of_boxes() {
		return no_of_boxes;
	}
	public void setNo_of_boxes(Long no_of_boxes) {
		this.no_of_boxes = no_of_boxes;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public Date getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public FabricInRecord(long id, Long control_id, String gr, Long quality_id, Long quality_entry_id,
			String quality_name, String quality_type, Double mtr, Double wt, Long no_of_cones, Long no_of_boxes,
			Date created_date, Date updated_date, String created_by, String updated_by, boolean is_active,
			List<FabricInRecord> comments) {
		super();
		this.id = id;
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
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.created_by = created_by;
		this.updated_by = updated_by;
		this.is_active = is_active;
	}
	public FabricInRecord() {
	}
}
