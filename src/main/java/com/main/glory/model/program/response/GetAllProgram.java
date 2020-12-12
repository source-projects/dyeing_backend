package com.main.glory.model.program.response;


import com.main.glory.model.party.Party;
import com.main.glory.model.program.Program;
import com.main.glory.model.quality.Quality;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class GetAllProgram {

    Long id;
    Long partyId;
    String partyName;
    String programGivenBy;
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    String qualityType;
    Long userHeadId;
    Long createdBy;
    Long updatedBy;
    String priority;
    String remark;

    public GetAllProgram(Program e, Party party, Quality quality) {
        this.id=e.getId();
        this.partyId=e.getPartyId();
        this.partyName=party.getPartyName();
        this.programGivenBy=e.getProgramGivenBy();
        this.qualityEntryId=e.getQualityEntryId();
        this.qualityId=quality.getQualityId();
        this.qualityName=quality.getQualityName();
        this.qualityType=quality.getQualityType();
        this.userHeadId=e.getUserHeadId();
        this.createdBy=e.getCreatedBy();
        this.updatedBy=e.getUpdatedBy();
        this.priority=e.getPriority();
        this.remark=e.getRemark();

    }
}
