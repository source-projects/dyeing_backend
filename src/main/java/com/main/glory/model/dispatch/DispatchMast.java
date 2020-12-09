package com.main.glory.model.dispatch;

import com.main.glory.model.StockDataBatchData.BatchData;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class DispatchMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Date createdDate;
    Long createdBy;
    String batchId;
    Long stockId;
    Boolean isSendToParty=false;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<DispatchData> dispatchData;


    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }


}
