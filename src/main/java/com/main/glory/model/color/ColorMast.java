package com.main.glory.model.color;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "color_mast")
@Getter
@Setter
@AllArgsConstructor
public class ColorMast {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;
    Long supplier_id;
    Date ddate;
    String bill_no;
    Date bill_date;
    String chl_no;
    Date chl_date;
    Long lot_no;
    Double bill_amount;
    Long user_id;
    String remark;
    @ApiModelProperty(hidden = true)
    Date created_date;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_id",referencedColumnName = "id")
    List<ColorData> colorDataList;

    public ColorMast() {
       this.created_date = new Date(System.currentTimeMillis());
    }
}
