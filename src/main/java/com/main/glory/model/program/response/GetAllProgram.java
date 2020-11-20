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

    Long id;
    Long party_id;
    String partName;
    String program_given_by;
    Long quality_entry_id;
    String quality_id;
    String qualityName;
    String qualityType;
    String priority;
    String remark;



}
