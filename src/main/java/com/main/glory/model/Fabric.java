package com.main.glory.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fabricMaster")
public class Fabric {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	private Long id;
	private Long stockId;
	private String stockInType;
	private Long batch;
	private Long partyId;
	private String partyName;
	private Date date;
	private String billNo;
	private String chlNo;
	private Date billDate;
	private Date chlDate;
	private Long lotNo;
	private String remark;
	private Long billId;
	private Date createdDate;
	private Date updatedDate;
	private String createdBy;
	private String updatedBy;
	private Long userHeadId;
	private Long recordCount;


	@OneToMany(cascade = CascadeType.ALL)
	@Transient
	@JoinColumn(name = "controlId", referencedColumnName = "id")
	List<FabricInRecord> fabricInRecord;

	public Fabric(Long id, Long stockId, String stockInType, Long batch, Long partyId, String partyName, Date date, String billNo, String chlNo, Date billDate, Date chlDate, Long lotNo, String remark, Long billId, Date createdDate, Date updatedDate, String createdBy, String updatedBy, Long userHeadId, Long recordCount) {
		this.id = id;
		this.stockId = stockId;
		this.stockInType = stockInType;
		this.batch = batch;
		this.partyId = partyId;
		this.partyName = partyName;
		this.date = date;
		this.billNo = billNo;
		this.chlNo = chlNo;
		this.billDate = billDate;
		this.chlDate = chlDate;
		this.lotNo = lotNo;
		this.remark = remark;
		this.billId = billId;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.userHeadId = userHeadId;
		this.recordCount = recordCount;
	}
}
