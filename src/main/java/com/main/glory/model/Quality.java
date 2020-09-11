package com.main.glory.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="quality")
public class Quality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String  quality_id;
	private String  quality_name;
	private String quality_type;
	private String quality_sub_type;
	private Long  party_id;
	private String  party_name;
	private Double wt_per100m;
	private String remark;
	private  Date created_date;
	private String created_by;
	private String updated_by;
	private Date updated_date;
	private Date quality_date;
	private Long user_head_id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getQuality_id() {
		return quality_id;
	}

	public Long getParty_id() {
		return party_id;
	}
	public void setParty_id(Long party_id) {
		this.party_id = party_id;
	}
	public void setQuality_id(String quality_id) {
		this.quality_id = quality_id;
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
	public String getQuality_sub_type() {
		return quality_sub_type;
	}
	public void setQuality_sub_type(String quality_sub_type) {
		this.quality_sub_type = quality_sub_type;
	}

	public String getParty_name() {
		return party_name;
	}
	public void setParty_name(String party_name) {
		this.party_name = party_name;
	}
	public Double getWt_per100m() {
		return wt_per100m;
	}
	public void setWt_per100m(Double wt_per100m) {
		this.wt_per100m = wt_per100m;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
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
	public Date getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
	public Date getQuality_date() {
		return quality_date;
	}
	public void setQuality_date(Date quality_date) {
		this.quality_date = quality_date;
	}
	public Long getUser_head_id() {
		return user_head_id;
	}
	public void setUser_head_id(Long user_head_id) {
		this.user_head_id = user_head_id;
	}
	@Override
	public String toString() {
		return "Quality [id=" + id + ", quality_id=" + quality_id + ", quality_name=" + quality_name + ", quality_type="
				+ quality_type + ", quality_sub_type=" + quality_sub_type + ", party_id=" + party_id + ", party_name="
				+ party_name + ", wt_per100m=" + wt_per100m + ", remark=" + remark + ", created_date=" + created_date
				+ ", created_by=" + created_by + ", updated_by=" + updated_by + ", updated_date=" + updated_date
				+ ", quality_date=" + quality_date + ", user_head_id=" + user_head_id + "]";
	}
	
}
