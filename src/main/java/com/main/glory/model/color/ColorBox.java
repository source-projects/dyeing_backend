package com.main.glory.model.color;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "color_box")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorBox {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long boxNo;
	Long controlId;
	Boolean issued;
	Date issuedDate;
	Boolean finished;
	Double quantityLeft;

	@Column(nullable = false)
	Long quantity;
	Date createdDate;

	public ColorBox(ColorBox colorBox) {
		this.boxNo = colorBox.getBoxNo();
		this.controlId=colorBox.getControlId();
		this.issued=colorBox.getIssued();
		this.issuedDate=colorBox.getIssuedDate();
		this.finished=colorBox.getFinished();
		this.quantityLeft= colorBox.getQuantityLeft();
		this.quantity=colorBox.getQuantity();
		this.createdDate=colorBox.createdDate;
	}

	@PrePersist protected void onCreate()
	{
		this.createdDate=new Date(System.currentTimeMillis());
	}
}
