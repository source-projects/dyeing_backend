package com.main.glory.model.quality;

import lombok.*;

import java.util.Date;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="quality")
@ToString
public class Quality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String  qualityId;
	private String  qualityName;
	private String qualityType;
	private String qualitySubType;
	private Long  partyId;
	private Double wtPer100m;
	private String remark;
	private  Date createdDate;
	private String createdBy;
	private String updatedBy;
	private Date updatedDate;
	private Date qualityDate;
	private Long userHeadId;

	public Quality(Quality other) {
		this.id = other.id;
		this.qualityId = other.qualityId;
		this.qualityName = other.qualityName;
		this.qualityType = other.qualityType;
		this.qualitySubType = other.qualitySubType;
		this.partyId = other.partyId;
		this.wtPer100m = other.wtPer100m;
		this.remark = other.remark;
		this.createdDate = other.createdDate;
		this.createdBy = other.createdBy;
		this.updatedBy = other.updatedBy;
		this.updatedDate = other.updatedDate;
//		this.qualityDate = other.qualityDate;
		this.userHeadId = other.userHeadId;
	}

	@PrePersist
	protected void onCreate() {
		this.createdDate = new Date(System.currentTimeMillis());
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = new Date(System.currentTimeMillis());
	}
}
