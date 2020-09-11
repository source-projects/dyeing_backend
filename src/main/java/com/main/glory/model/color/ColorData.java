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
@Table(name = "color_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorData {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	Long id;
	@Column(nullable = false)
	Long purchase_id;

	@Column(nullable = false)
	String item_name;

	@Column(nullable = false)
	Integer quantity;

	@Column(nullable = false)
	Double rate;

	@Column(nullable = false)
	String quantity_unit;

	@Column(nullable = false)
	Integer quantity_per_box;

	@Column(nullable = false)
	Integer no_of_box;

	@ApiModelProperty(hidden = true)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "control_id", referencedColumnName = "id")
	List<ColorBox> colorBoxes;
//	@Column(nullable = false)
//	Boolean issued;
//
//	Date issued_date;

}
