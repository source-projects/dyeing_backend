package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.StockDataBatchData.BatchReturn;
import com.main.glory.model.StockDataBatchData.BatchReturnData;
import com.main.glory.model.StockDataBatchData.BatchReturnMast;
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
public class BatchReturnResponse {
    Long id;
    Double totalMtr;
    Long totalPcs;
    Long chlNo;
    String partyName;
    String qualityName;
    String qualityId;
    Date challanDate;
    String address;
    String gst;
    String broker;
    String tempoNo;
    String pchallanNo;
    String diffPartyName;
    String diffPartyAddress;
    String diffGst;
    Boolean diffDeliveryParty;
    List<BatchReturnData> batchReturnList;

    public BatchReturnResponse(BatchReturnMast batchReturnExist, Long pcs, List<BatchReturnData> batchReturnList, Double totalMtr) {
        this.id = batchReturnExist.getId();
        this.chlNo = batchReturnExist.getChlNo();
        this.pchallanNo = batchReturnExist.getPchallanNo()==null?null:batchReturnExist.getPchallanNo();
        this.totalMtr=totalMtr;
        this.totalPcs = pcs;
        this.partyName=batchReturnExist.getPartyName();
        this.batchReturnList = batchReturnList;
        this.qualityName = batchReturnList.get(0).getQualityName();
        this.qualityId = batchReturnList.get(0).getQualityId();
        this.gst = batchReturnExist.getGst();
        this.challanDate = batchReturnExist.getChallanDate();
        this.address= batchReturnExist.getAddress();
        this.tempoNo = batchReturnExist.getTempoNo()==null?null:batchReturnExist.getTempoNo();
        this.broker = batchReturnExist.getBroker()==null?null:batchReturnExist.getBroker();
        this.diffGst = batchReturnExist.getDiffGst()==null?(batchReturnExist.getGst()==null?null:batchReturnExist.getGst()):batchReturnExist.getDiffGst();
        this.diffPartyAddress =batchReturnExist.getDiffPartyAddress()==null?batchReturnExist.getAddress():batchReturnExist.getDiffPartyAddress();
        this.diffPartyName = batchReturnExist.getDiffPartyName()==null?batchReturnExist.getPartyName():batchReturnExist.getDiffPartyName();
        this.diffDeliveryParty = batchReturnExist.getDiffDeliveryParty();
    }

 /*   public BatchReturnResponse(ReturnBatchDetail e, List<BatchReturnData> batchReturnList) {
        this.id = e.getId();
        this.totalMtr = e.getMtr();
        this.totalPcs = Long.valueOf(batchReturnList.size());
        this.chlNo = e.getChlNo();
        this.partyName = e.getPartyName();
        this.qualityName = e.getQualityName();
        this.qualityId = e.getQualityId();
        this.batchReturnList = batchReturnList;
    }
    public BatchReturnResponse(ReturnBatchDetail e,Long size) {
        this.id = e.getId();
        this.totalMtr = e.getMtr();
        this.totalPcs = size;
        this.chlNo = e.getChlNo();
        this.partyName = e.getPartyName();
        this.qualityName = e.getQualityName();
        this.qualityId = e.getQualityId();

    }*/


}
