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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fabricMaster")
public class Fabric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
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
	 List <FabricInRecord> fabricInRecord;

	
}
