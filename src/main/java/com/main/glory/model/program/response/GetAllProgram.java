package com.main.glory.model.program.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class GetAllProgram {

    Long partyId;
    String partName;
    String programGivenBy;
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    String qualityType;
    String priority;
    String remark;



}
