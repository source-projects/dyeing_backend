package com.main.glory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="quality")
public class Quality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String  qualityId;
	private String  qualityName;
	private String qualityType;
	private String qualitySubType;
	private Long  partyId;
	private String  partyName;
	private Double wtPer100m;
	private String remark;
	private  Date createddDate;
	private String createdBy;
	private String updatedBy;
	private Date updatedDate;
	private Date qualityDate;
	private Long userHeadId;

}
