package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.user.UserData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddShadeMast {

    String partyShadeNo;
    
    Long processId;
    String processName;
    String colorTone;
    String qualityId;
    String qualityName;
    String qualityType;
    Long partyId;
    Long cuttingId;
    String labColorNo;
    String category;
    String remark;
    String apcNo;
    Boolean pending;
    Double extraRate;
    String colorName;
    List<ShadeData> shadeDataList;

    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="createdBy", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData createdBy;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="updatedById", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData updatedBy;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userHeadId", referencedColumnName = "id", insertable = true, updatable = true)
    private UserData userHeadData;



}
