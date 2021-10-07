package com.main.glory.model.quality.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.main.glory.model.party.Party;
import com.main.glory.model.user.UserData;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddQualityRequest {
    Long id;
    private String qualityId;
    private Long qualityNameId;
    private String qualityName;
    private String qualityType;
    private String unit;
    private Double wtPer100m;
    private String remark;
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(name="userHeadId", referencedColumnName = "id", insertable = true, updatable = true)    
	UserData updatedBy;
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(name="createdById", referencedColumnName = "id", insertable = true, updatable = true)    
	UserData userCreatedByData;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(name="userHeadId", referencedColumnName = "id", insertable = true, updatable = true)    
	UserData userHeadData;
    
    private Double rate;
    Double mtrPerKg;
    String billingUnit;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(name="partyId", referencedColumnName = "id", insertable = true, updatable = true)    	
	Party  party;

}
