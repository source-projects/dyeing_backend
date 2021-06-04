package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@Getter
@Setter
public class ReturnBatchDetail {
    Long id;
    Long partyId;
    String partyName;
    String address;
    String partyCode;
    Long qualityEntryId;
    String qualityName;
    String qualityId;
    Double mtr;
    Long chlNo;
    Date challanDate;

    public ReturnBatchDetail(Long id, Long partyId, String partyName, String address, String partyCode, Long qualityEntryId, String qualityName, String qualityId, Double mtr, Long chlNo, Date challanDate) {
        this.id = id;
        this.partyId = partyId;
        this.partyName = partyName;
        this.address = address;
        this.partyCode = partyCode;
        this.qualityEntryId = qualityEntryId;
        this.qualityName = qualityName;
        this.qualityId = qualityId;
        this.mtr = mtr;
        this.chlNo = chlNo;
        this.challanDate = challanDate;
    }
}
