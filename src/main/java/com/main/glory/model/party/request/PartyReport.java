package com.main.glory.model.party.request;

import com.main.glory.model.StockDataBatchData.request.BatchDetail;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.response.QualityWithDetail;
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
    Double pendingAmt;
    DispatchMast lastDispatch;
    Double availableStockValue;
    List<QualityWithDetail> qualityWithDetailList;
    List<BatchDetail> batchDetailList;



    public PartyReport(Party party) {
        this.id=party.getId();
        this.partyName=party.getPartyName();
        this.gst=party.getGSTIN();
        this.address=party.getPartyAddress1();
        this.partCode=party.getPartyCode();
    }
}
