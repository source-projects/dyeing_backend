package com.main.glory.model.dyeingProcess;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DyeingChemicalData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long controlId;
    Long itemId;
    String itemName;
    String byChemical;
    Double concentration;
    @ColumnDefault("DEFAULT")
    String shadeType;


}
