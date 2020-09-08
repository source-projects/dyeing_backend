package model;

import java.util.ArrayList;
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
@Entity
@Table(name = "fabricMaster")
public class Fabric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
	private Long record_count;
	private Date created_date;
	private Date updated_date;
	private String created_by;
	private String updated_by;
	private Long user_head_id;
	
	 @OneToMany(cascade = CascadeType.ALL)
	 @JoinColumn(name = "control_id", referencedColumnName = "id")
	
	 List < FabricInRecord > fabricInRecord;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getStock_id() {
		return stock_id;
	}
	public void setStock_id(Long stock_id) {
		this.stock_id = stock_id;
	}
	public String getStock_in_type() {
		return stock_in_type;
	}
	public void setStock_in_type(String stock_in_type) {
		this.stock_in_type = stock_in_type;
	}
	public Long getBatch() {
		return batch;
	}
	public void setBatch(Long batch) {
		this.batch = batch;
	}
	public Long getParty_id() {
		return party_id;
	}
	public void setParty_id(Long party_id) {
		this.party_id = party_id;
	}
	public String getParty_name() {
		return party_name;
	}
	public void setParty_name(String party_name) {
		this.party_name = party_name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getBill_no() {
		return bill_no;
	}
	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}
	public String getChl_no() {
		return chl_no;
	}
	public void setChl_no(String chl_no) {
		this.chl_no = chl_no;
	}
	public Date getBill_date() {
		return bill_date;
	}
	public void setBill_date(Date bill_date) {
		this.bill_date = bill_date;
	}
	public Date getChl_date() {
		return chl_date;
	}
	public void setChl_date(Date chl_date) {
		this.chl_date = chl_date;
	}
	public Long getLot_no() {
		return lot_no;
	}
	public void setLot_no(Long lot_no) {
		this.lot_no = lot_no;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getBill_id() {
		return bill_id;
	}
	public void setBill_id(Long bill_id) {
		this.bill_id = bill_id;
	}
	public Long getRecord_count() {
		return record_count;
	}
	public void setRecord_count(Long record_count) {
		this.record_count = record_count;
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
	public Long getUser_head_id() {
		return user_head_id;
	}
	public void setUser_head_id(Long user_head_id) {
		this.user_head_id = user_head_id;
	}
	public List<FabricInRecord> getComments() {
		return fabricInRecord;
	}
	public void setComments(List<FabricInRecord> comments) {
		this.fabricInRecord = fabricInRecord;
	}
	public Fabric(long id, Long stock_id, String stock_in_type, Long batch, Long party_id, String party_name, Date date,
			String bill_no, String chl_no, Date bill_date, Date chl_date, Long lot_no, String remark, Long bill_id,
			Long record_count, Date created_date, Date updated_date, String created_by, String updated_by,
			Long user_head_id, List<FabricInRecord> comments) {
		super();
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
		this.record_count = record_count;
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.created_by = created_by;
		this.updated_by = updated_by;
		this.user_head_id = user_head_id;
		this.fabricInRecord = fabricInRecord;
	}
	public Fabric() {
	}
	
}
