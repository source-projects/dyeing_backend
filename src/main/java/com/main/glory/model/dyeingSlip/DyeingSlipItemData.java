package com.main.glory.model.dyeingSlip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class DyeingSlipItemData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String itemName;
    Long itemId;
    Long supplierId;
    String supplierName;
    Double qty;
    Long controlId;
    Boolean isColor;

}
