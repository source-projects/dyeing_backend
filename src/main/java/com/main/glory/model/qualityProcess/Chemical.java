package com.main.glory.model.qualityProcess;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
@Table
@NoArgsConstructor
public class Chemical {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    Long id;
    Long qualityProcessControlId;
    String DynamicProcessRecordId;
    Long itemId;
    String itemName;
    Long supplierId;
    String concentration;
    String lrOrFabricWt;
}
