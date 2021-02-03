package com.main.glory.model.dispatch.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetConsolidatedBill {
    String invoiceNo;
    String partyName;
    Long partyId;
    String headName;
    Long userHeadId;
    Double amt;
    Double totalFinishMtr;

}
