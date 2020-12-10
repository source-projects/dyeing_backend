package com.main.glory.servicesImpl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.main.glory.Dao.PartyDao;
import com.main.glory.model.document.request.GetDocumentModel;
import com.main.glory.model.party.Party;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;

@Service("documentImpl")
public class DocumentImpl {


    @Autowired
    PartyDao partyDao;



    public void getParty(GetDocumentModel documentModel) throws Exception {


        String fileName = "party.pdf";
        File f=new File(fileName);
        f.createNewFile();
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));//file is created, where the project filder is
        document.open();

        //Add the data
        PdfPTable table = new PdfPTable(5);
        PdfPCell partyName = new PdfPCell(new Phrase("Party Name"));
        table.addCell(partyName);
        PdfPCell partyAddress = new PdfPCell(new Phrase("Address"));
        table.addCell(partyAddress);
        PdfPCell partyContact = new PdfPCell(new Phrase("Contact no"));
        table.addCell(partyContact);
        PdfPCell partyCity = new PdfPCell(new Phrase("City"));
        table.addCell(partyCity);
        PdfPCell partyState = new PdfPCell(new Phrase("State"));
        table.addCell(partyState);
        table.setHeaderRows(1);
        List<Party> party = partyDao.findAll();

        if(documentModel.getToRow()>party.size())
           throw new Exception("Party size is :"+party.size());

        for (int i =0 ; i <= (documentModel.getToRow() - documentModel.getFromRow()); i++) {


             System.out.println(party.get(i).getPartyName());
             table.addCell(party.get(i).getPartyName());
             table.addCell(party.get(i).getPartyAddress1());
             table.addCell(party.get(i).getContactNo());
             table.addCell(party.get(i).getCity());
             table.addCell(party.get(i).getCity());

        }

        document.add(table);

        document.close();
        //______Document created successfully

    }
}
