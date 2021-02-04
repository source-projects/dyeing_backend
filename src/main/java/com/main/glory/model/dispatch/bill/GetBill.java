package com.main.glory.model.dispatch.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetBill {
    String invoiceNo;
    String partyName;
    Long partyId;
    String headName;
    Long userHeadId;
    List<QualityList> qualityList;
}
