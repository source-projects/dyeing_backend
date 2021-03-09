package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.shade.ShadeMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

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
    Long userHeadId;
    Long createdBy;
    String apcNo;
    Boolean pending;


    public GetAllShade(ShadeMast e, Optional<Party> party, Optional<Quality> qualityName,DyeingProcessMast dyeingProcessMast) {
        this.id=e.getId();
        this.partyShadeNo=e.getPartyShadeNo();
        this.processId=e.getProcessId();
        this.qualityEntryId=qualityName.get().getId();
        this.qualityName=qualityName.get().getQualityName();
        this.qualityId=qualityName.get().getQualityId();
        this.partyName=party.get().getPartyName();
        this.partyId=party.get().getId();
        this.colorTone=e.getColorTone();
        this.userHeadId=e.getUserHeadId();
        this.createdBy=e.getCreatedBy();
        this.apcNo=e.getApcNo();
        this.pending=e.getPending();
        this.processName=dyeingProcessMast.getProcessName();
    }


    public GetAllShade(ShadeMast e, Optional<Party> party, Optional<Quality> qualityName, DyeingProcessMast dyeingProcessMast, QualityName qualityName1) {
        this.id=e.getId();
        this.partyShadeNo=e.getPartyShadeNo();
        this.processId=e.getProcessId();
        this.qualityEntryId=qualityName.get().getId();
        this.qualityName=qualityName.get().getQualityName();
        this.qualityId=qualityName.get().getQualityId();
        this.partyName=party.get().getPartyName();
        this.partyId=party.get().getId();
        this.colorTone=e.getColorTone();
        this.userHeadId=e.getUserHeadId();
        this.createdBy=e.getCreatedBy();
        this.apcNo=e.getApcNo();
        this.pending=e.getPending();
        this.processName=dyeingProcessMast.getProcessName();
        this.qualityName=qualityName1.getQualityName();
    }
}
