package com.main.glory.model.dispatch.request;

import com.main.glory.model.party.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.jdbc.batch.spi.Batch;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PartyDataByInvoiceNumber {
    String partyName;
    String address;
    String GST;
    String invoiceNuber;
    List<QualityBillByInvoiceNumber> qualityList;
    List<BatchWithGr> batchWithGrList;


    public PartyDataByInvoiceNumber(Party party, List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList, List<BatchWithGr> batchWithGrList) {
        this.partyName=party.getPartyName();
        this.address=party.getPartyAddress1();
        this.GST=party.getGSTIN();
        this.qualityList=qualityBillByInvoiceNumberList;
        this.batchWithGrList=batchWithGrList;
    }
}
