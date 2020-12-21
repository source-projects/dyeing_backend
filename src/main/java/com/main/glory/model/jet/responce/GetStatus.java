package com.main.glory.model.jet.responce;

import com.main.glory.model.jet.JetStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetStatus {
    String name="name";
    String status;

    public GetStatus(JetStatus jetStatus) {
        this.status=jetStatus.toString();
    }
}
