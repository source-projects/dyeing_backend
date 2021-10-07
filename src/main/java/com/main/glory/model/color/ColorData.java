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
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	Long controlId;

	@Column(nullable = false)
	Long itemId;

	@Column(nullable = false)
	Double rate;

	@Column(nullable = false)
	String quantityUnit;

	@Column(nullable = false)
	Long quantityPerBox;

	@Column(nullable = false)
	Integer noOfBox;

	@Transient
	@ApiModelProperty(hidden = true)
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(name = "controlId", referencedColumnName = "id")
	List<ColorBox> colorBoxes;
}
