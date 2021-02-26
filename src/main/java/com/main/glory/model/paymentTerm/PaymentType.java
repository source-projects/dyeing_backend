package com.main.glory.model.paymentTerm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dispatch.DispatchMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    Long id;
    String paymentType;

//    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "payTypeId", referencedColumnName = "id")
    private List<PaymentData> paymentData;

//    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "payTypeId", referencedColumnName = "id")
    private List<AdvancePayment> advancePaymentList;

    public PaymentType(String type) {
        this.paymentType=type;

    }
}
