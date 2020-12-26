package com.main.glory.model.user.response;

import com.main.glory.model.user.UserData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllOperator {

    Long id;
    String name;
    String lastName;
    String userName;

    public GetAllOperator(UserData operator) {
        this.id=operator.getId();
        this.lastName=operator.getLastName();
        this.userName=operator.getUserName();
        this.name=operator.getFirstName();

    }
}
