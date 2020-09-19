package com.main.glory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "color_mast")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    Date created_date;
    Time created_time;
}
