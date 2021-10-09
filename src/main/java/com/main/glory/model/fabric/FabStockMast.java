package com.main.glory.model.fabric;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FabStockMast {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private String stockInType;
	private Boolean batch;
	@Column(nullable = false)
	private Long partyId;
	//@Column(nullable = false)
	//private Date date;
	@Column(nullable = false)
	private String billNo;
	@Column(nullable = false)
	private String chlNo;
	@Column(nullable = false)
	private Date billDate;
	@Column(nullable = false)
	private Date chlDate;
	@Column(nullable = false)
	private Long lotNo;
	private String remark;
//	private Long billId;
	@ApiModelProperty(hidden = true)
	private Date createdDate;
	@ApiModelProperty(hidden = true)
	private Date updatedDate;
	private String createdBy;
	private String updatedBy;
	@Column(nullable = false)
	private Long userHeadId;


	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(referencedColumnName = "id", name = "controlId")
	Set<FabStockData> fabStockData;



	@PrePersist
	protected void onCreate() {
		this.createdDate = new Date(System.currentTimeMillis());
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = new Date(System.currentTimeMillis());
	}

	public FabStockMast(FabStockMast other) {
		this.id = other.id;
		this.stockInType = other.stockInType;
		this.batch = other.batch;
		this.partyId = other.partyId;
		this.billNo = other.billNo;
		this.chlNo = other.chlNo;
		this.billDate = other.billDate;
		this.chlDate = other.chlDate;
		this.lotNo = other.lotNo;
		this.remark = other.remark;
		this.createdDate = other.createdDate;
		this.updatedDate = other.updatedDate;
		this.createdBy = other.createdBy;
		this.updatedBy = other.updatedBy;
		this.userHeadId = other.userHeadId;
		this.fabStockData = other.fabStockData;
	}
}
