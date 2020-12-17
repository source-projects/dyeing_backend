package com.main.glory.model.document.request;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetDocumentModel {

    Long fromRow;
    Long toRow;
    String exportType;
    String moduleName;
    String toEmail;
    String fromEmail;
    String subjectEmail;
    String sendText;


}
