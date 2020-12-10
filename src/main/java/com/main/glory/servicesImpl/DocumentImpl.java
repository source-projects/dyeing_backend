package com.main.glory.servicesImpl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.main.glory.Dao.PartyDao;
import com.main.glory.model.document.DocumentConfig;
import com.main.glory.model.document.request.GetDocumentModel;
import com.main.glory.model.party.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileOutputStream;
import java.util.List;

@Service("documentImpl")
public class DocumentImpl {


    @Autowired
    PartyDao partyDao;

    DocumentConfig documentConfig;

    public void getParty(GetDocumentModel documentModel) throws Exception {


        String fileName = documentConfig.getPath()+"party.pdf";
        Document document=new Document();

        PdfWriter.getInstance(document,new FileOutputStream(fileName));
        document.open();

        //Add the data
        PdfPTable table=new PdfPTable(5);
        PdfPCell partyName= new PdfPCell(new Phrase("Party Name"));
        table.addCell(partyName);
        PdfPCell partyAddress= new PdfPCell(new Phrase("Address"));
        table.addCell(partyAddress);
        PdfPCell partyContact= new PdfPCell(new Phrase("Contact no"));
        table.addCell(partyContact);
        PdfPCell partyCity= new PdfPCell(new Phrase("City"));
        table.addCell(partyCity);
        PdfPCell partyState= new PdfPCell(new Phrase("State"));
        table.addCell(partyState);

        table.setHeaderRows(1);

        List<Party> party = partyDao.findAll();
        for(int i=0;i<documentModel.getToRow()-documentModel.getFromRow();i++)
        {
            table.addCell(party.get(i).getPartyName());
            table.addCell(party.get(i).getPartyAddress1());
            table.addCell(party.get(i).getContactNo());
            table.addCell(party.get(i).getCity());
            table.addCell(party.get(i).getCity());
        }

        document.add(table);

        document.close();

    }
}
