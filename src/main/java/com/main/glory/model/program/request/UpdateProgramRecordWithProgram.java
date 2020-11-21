package com.main.glory.model.program.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProgramRecordWithProgram {

    private Long id;
    private Long shadeNo;
    private String partyShadeNo;
    private Long quantity;
    private String colourTone;
    private String remark;
    private Long programControlId;

}
