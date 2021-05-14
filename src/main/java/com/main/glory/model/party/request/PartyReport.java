package com.main.glory.model.party.request;

import com.main.glory.model.StockDataBatchData.request.BatchDetail;
import com.main.glory.model.party.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyReport {
    Long id;
    String partyName;
    String gst;
    String address;
    String partCode;
    List<BatchDetail> batchDetailList;



    public PartyReport(Party party) {
        this.id=party.getId();
        this.partyName=party.getPartyName();
        this.gst=party.getGSTIN();
        this.address=party.getPartyAddress1();
        this.partCode=party.getPartyCode();
    }
}
