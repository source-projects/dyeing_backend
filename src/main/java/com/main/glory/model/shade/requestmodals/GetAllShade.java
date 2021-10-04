package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.UserData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetAllShade {

    Long id;
    String partyShadeNo;
    Long processId;
    String processName;
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    String partyName;
    Long partyId;
    String colorTone;
    String apcNo;
    Boolean pending;
    String colorName;
    Double wtPer100m;
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



    public GetAllShade(ShadeMast e, Party party, Optional<Quality> qualityName,DyeingProcessMast dyeingProcessMast) {
        this.id=e.getId();
        this.partyShadeNo=e.getPartyShadeNo();
        this.processId=e.getProcessId();
        this.qualityEntryId=qualityName.get().getId();
        this.qualityName=qualityName.get().getQualityName().getQualityName();
        this.qualityId=qualityName.get().getQualityId();
        this.partyName=party.getPartyName();
        this.partyId=party.getId();
        this.colorTone=e.getColorTone();
        this.userHeadData=e.getUserHeadData();
        this.createdBy=e.getCreatedBy();
        this.apcNo=e.getApcNo();
        this.pending=e.getPending();
        this.colorName=e.getColorName();
        this.processName=dyeingProcessMast.getProcessName();
    }


    public GetAllShade(ShadeMast e, Party party, Optional<Quality> qualityName, DyeingProcessMast dyeingProcessMast, QualityName qualityName1) {
        this.id=e.getId();
        this.partyShadeNo=e.getPartyShadeNo();
        this.processId=e.getProcessId();
        this.qualityEntryId=qualityName.get().getId();
        this.qualityName=qualityName1.getQualityName();
        this.qualityId=qualityName.get().getQualityId();
        this.partyName=party.getPartyName();
        this.partyId=party.getId();
        this.colorTone=e.getColorTone();
        this.userHeadData=e.getUserHeadData();
        this.createdBy=e.getCreatedBy();
        this.apcNo=e.getApcNo();
        this.pending=e.getPending();
        this.processName=dyeingProcessMast.getProcessName();
        this.qualityName=qualityName1.getQualityName();
        this.colorName=e.getColorName();
        this.shadeDataList = e.getShadeDataList();
        this.wtPer100m = qualityName.get().getWtPer100m();
    }
}
