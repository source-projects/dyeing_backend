package com.main.glory.model.paymentTerm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class AdvancePayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    Long id;
    Date createdDate;
    Long createdBy;
    Long updatedBy;
    Double amt;
    Long partyId;
    Long creditId;
    Long paymentBunchId;
    String remark;

    @PrePersist
    protected void onCreate(){ this.createdDate=new Date(System.currentTimeMillis());}

}
