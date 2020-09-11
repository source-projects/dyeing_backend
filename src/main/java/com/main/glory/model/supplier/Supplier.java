package com.main.glory.model.supplier;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    Long id;
    String supplier_name;
    Double discount_percentage;
    Double gst_percentage;
    Long user_id;
    String remark;
    @ApiModelProperty(hidden = true)
    Date created_date;
    Long payment_terms;

    String updated_by;

    @ApiModelProperty(hidden = true)
    Date updated_date;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ApiModelProperty(hidden = true)
    List<SupplierRate> supplierRates;
}




