package com.main.glory.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fabricMaster")
public class Fabric {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
    private Long id;
	private Long stock_id;
	private String stock_in_type;
	private Long batch;
	private Long party_id;
	private String party_name;
	private Date date;
	private String bill_no;
	private String chl_no;
	private Date bill_date;
	private Date chl_date;
	private Long lot_no;
	private String remark;
	private Long bill_id;
//	private Long record_count;
	private Date created_date;
	private Date updated_date;
	private String created_by;
	private String updated_by;
	private Long user_head_id;
	
	 @OneToMany(cascade = CascadeType.ALL)
	 @JoinColumn(name = "control_id", referencedColumnName = "id")
	 List <FabricInRecord> fabricInRecord;

	public Fabric(Long id, Long stock_id, String stock_in_type, Long batch, Long party_id, String party_name, Date date, String bill_no, String chl_no, Date bill_date, Date chl_date, Long lot_no, String remark, Long bill_id, Date created_date, Date updated_date, String created_by, String updated_by, Long user_head_id) {
		this.id = id;
		this.stock_id = stock_id;
		this.stock_in_type = stock_in_type;
		this.batch = batch;
		this.party_id = party_id;
		this.party_name = party_name;
		this.date = date;
		this.bill_no = bill_no;
		this.chl_no = chl_no;
		this.bill_date = bill_date;
		this.chl_date = chl_date;
		this.lot_no = lot_no;
		this.remark = remark;
		this.bill_id = bill_id;
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.created_by = created_by;
		this.updated_by = updated_by;
		this.user_head_id = user_head_id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStock_id(Long stock_id) {
		this.stock_id = stock_id;
	}

	public void setStock_in_type(String stock_in_type) {
		this.stock_in_type = stock_in_type;
	}

	public void setBatch(Long batch) {
		this.batch = batch;
	}

	public void setParty_id(Long party_id) {
		this.party_id = party_id;
	}

	public void setParty_name(String party_name) {
		this.party_name = party_name;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public void setChl_no(String chl_no) {
		this.chl_no = chl_no;
	}

	public void setBill_date(Date bill_date) {
		this.bill_date = bill_date;
	}

	public void setChl_date(Date chl_date) {
		this.chl_date = chl_date;
	}

	public void setLot_no(Long lot_no) {
		this.lot_no = lot_no;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setBill_id(Long bill_id) {
		this.bill_id = bill_id;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public void setUser_head_id(Long user_head_id) {
		this.user_head_id = user_head_id;
	}
}
