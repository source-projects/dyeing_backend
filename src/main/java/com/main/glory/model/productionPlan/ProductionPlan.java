package com.main.glory.model.productionPlan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class ProductionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String batchId;
    Long stockId;
    Long partyId;
    Long qualityEntryId;
    Long shadeId;
}
