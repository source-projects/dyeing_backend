package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.shade.ShadeMast;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateShadeMastRequest {
	Long id;
	ShadeMast shadeMast;
}
