package com.main.glory.model.program.request;

import com.main.glory.model.ProgramRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddProgramWithProgramRecord {


    private Long partyId;
    private String qualityId;
    private Long qualityEntryId;
    private String programGivenBy;
    private String remark;
    private String priority;
    List<ProgramRecord> programRecords;

}
