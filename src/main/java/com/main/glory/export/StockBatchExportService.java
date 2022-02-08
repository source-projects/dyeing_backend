package com.main.glory.export;


import java.awt.Color;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.main.glory.model.StockDataBatchData.request.BatchFilterRequest;
import com.main.glory.model.StockDataBatchData.response.FabricInDetailsChildData;
import com.main.glory.model.StockDataBatchData.response.FabricInDetailsData;
import com.main.glory.model.StockDataBatchData.response.FabricInDetailsMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.user.UserData;
import com.main.glory.servicesImpl.StockBatchServiceImpl;


public class StockBatchExportService {

    List<FabricInDetailsMast> list;


    public StockBatchExportService(List<FabricInDetailsMast> list) {
        this.list = list;
    }


    public String exportFabricInDetail(BatchFilterRequest request) throws IOException, ParseException {
        File pdfDirectory = new File("pdf");

        if(!pdfDirectory.exists())
            pdfDirectory.mkdir();
        String fileName = String.valueOf(new Date().getTime())+".pdf";
        OutputStream outputStream =
                new FileOutputStream(new File(pdfDirectory+"/"+fileName));
        Document document = new Document(PageSize.A4);
        //PdfWriter.getInstance(document, servletResponse.getOutputStream());
        PdfWriter.getInstance(document, outputStream);

        document.open();
        //String text = "from :"+(request.getFrom()!=null?getDateInFormat(request.getFrom()):"");
        String text = "From :"+(request.getFrom()!=null?(getStringDateFormatted(request.getFrom())):"");
        Paragraph report =new Paragraph("DAILY GREIGE INWARD REPORT");
        report.setAlignment("Center");
        document.add(report);

        //from.setAlignment("Center");
        //document.add(from);
        //text = new String("to :"+(request.getTo()!=null?getDateInFormat(request.getTo()):""));
        text += "  To :"+(request.getTo()!=null?(getStringDateFormatted(request.getTo())):"");
        Paragraph to =new Paragraph(text);
        to.setAlignment("Center");
        document.add(to);


//        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//        font.setSize(18);
//        font.setColor(Color.BLUE);

        PdfPTable masterTable = new PdfPTable(4);
        masterTable.setWidthPercentage(100f);
        //table.setWidths(new float[] {9.5f});
        masterTable.setSpacingBefore(10);
        PdfPCell cellBlankRow = new PdfPCell(new Phrase(" "));

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        PdfPCell masterCell = new PdfPCell();
        //masterCell.setBackgroundColor(Color.BLUE);
        masterCell.setPadding(4);
        masterCell.setPhrase(new Phrase(""));
        masterTable.addCell(masterCell);

        masterCell.setPhrase(new Phrase("GREIGR TAKKA"));
        masterTable.addCell(masterCell);

        masterCell.setPhrase(new Phrase("GREIGR MTR"));
        masterTable.addCell(masterCell);

        masterCell.setPhrase(new Phrase("BILLING VALUE "));
        masterTable.addCell(masterCell);




        for (FabricInDetailsMast fabricInDetailsMast : list) {

            masterCell.setPhrase(new Phrase(fabricInDetailsMast.getMasterName()));
            masterTable.addCell(masterCell);

            masterCell.setPhrase(new Phrase(fabricInDetailsMast.getTotalPcs().toString()));
            masterTable.addCell(masterCell);

            masterCell.setPhrase(new Phrase(fabricInDetailsMast.getTotalMtr().toString()));
            masterTable.addCell(masterCell);

            masterCell.setPhrase(new Phrase(fabricInDetailsMast.getTotalBillingValue().toString()));
            masterTable.addCell(masterCell);

        }

        masterTable.addCell("TOTAL");
        Double masterTotalMtr = list.stream().mapToDouble(FabricInDetailsMast::getTotalMtr).sum();
        Long masterTotalPcs = list.stream().mapToLong(FabricInDetailsMast::getTotalPcs).sum();
        Double masterTotalBillingValues = list.stream().mapToDouble(FabricInDetailsMast::getTotalBillingValue).sum();

        masterTable.addCell(masterTotalPcs.toString());
        masterTable.addCell(masterTotalMtr.toString());
        masterTable.addCell(masterTotalBillingValues.toString());
        document.add(masterTable);


        for (FabricInDetailsMast fabricInDetailsMast : list) {
            //create the table for master level


            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100f);
            //table.setWidths(new float[] {9.5f});
            table.setSpacingBefore(10);



            writeTableHeaderWithPartyAndQuality(fabricInDetailsMast, table);
            //writeTableDataWithPartyAndQuality(fabricInDetailsMast,table);
            //writeTableHeader(table);
            //writeTableData(table);
            document.add(table);
            document.newPage();
        }


        //System.out.println(document);
        document.close();
        outputStream.close();



        return fileName;
    }

    private String getStringDateFormatted(String from) {
        String[] strings  = from.split("-");
        return strings[2]+"-"+strings[1]+"-"+strings[0];
    }

    private static String getDateInFormat(String date) throws ParseException {
        System.out.println(date);
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date finaleDate= new Date(date);
        return simpleDateFormat.format(finaleDate);

    }

    private void writeTableDataWithPartyAndQuality(FabricInDetailsMast fabricInDetailsMast, PdfPTable masterTable) {
        for (FabricInDetailsData fabricInDetailsData : fabricInDetailsMast.getList()) {
            masterTable.addCell(fabricInDetailsData.getPartyName());
            masterTable.addCell(fabricInDetailsData.getPartyCode());
            masterTable.addCell(fabricInDetailsData.getTotalMtr().toString());
            masterTable.addCell(fabricInDetailsData.getTotalWt().toString());
            masterTable.addCell(fabricInDetailsData.getTotalPcs().toString());
            masterTable.addCell(fabricInDetailsData.getTotalBillingValue().toString());
        }
    }

    private void writeTableHeaderWithPartyAndQuality(FabricInDetailsMast fabricInDetailsMast, PdfPTable masterTable) {
//        Font font = FontFactory.getFont(FontFactory.HELVETICA);
//        PdfPCell masterCell = new PdfPCell();
//        //masterCell.setBackgroundColor(Color.BLUE);
//        masterCell.setPadding(5);
//        masterCell.setPhrase(new Phrase("Master: "+fabricInDetailsMast.getMasterName()));
//        masterTable.addCell(masterCell);
//
//        PdfPCell cell = new PdfPCell();
//        cell.setBackgroundColor(Color.BLUE);
//        cell.setPadding(5);
//
//
//
//        for(FabricInDetailsData fabricInDetailsData:fabricInDetailsMast.getList())
//        {
//            PdfPTable partyTable = new PdfPTable(6);
//            partyTable.setWidthPercentage(100f);
//            //partyTable.setWidths(new float[] {1.5f,1.5f,1.5f,1.5f,1.5f,1.5f});
//            partyTable.setSpacingBefore(10);
//
//            font.setColor(Color.WHITE);
//            cell.setPhrase(new Phrase("Party Name", font));
//            partyTable.addCell(cell);
//
//            cell.setPhrase(new Phrase("Party Code", font));
//            partyTable.addCell(cell);
//
//            cell.setPhrase(new Phrase("Total Mtr", font));
//            partyTable.addCell(cell);
//
//            cell.setPhrase(new Phrase("Total Wt", font));
//            partyTable.addCell(cell);
//
//            cell.setPhrase(new Phrase("Total Pcs", font));
//            partyTable.addCell(cell);
//
//            cell.setPhrase(new Phrase("Billing Value", font));
//            partyTable.addCell(cell);
//
//
//
//            partyTable.addCell(fabricInDetailsData.getPartyName());
//            partyTable.addCell(fabricInDetailsData.getPartyCode());
//            partyTable.addCell(fabricInDetailsData.getTotalMtr().toString());
//            partyTable.addCell(fabricInDetailsData.getTotalWt().toString());
//            partyTable.addCell(fabricInDetailsData.getTotalPcs().toString());
//            partyTable.addCell(fabricInDetailsData.getTotalBillingValue().toString());
//
//
//            masterTable.addCell(partyTable);
//
//            //partyTable.completeRow();
//
//            Map<Quality,List<FabricInDetailsChildData>> fabricInDetailsDataListMap = fabricInDetailsData.getList().stream().collect(Collectors.groupingBy(FabricInDetailsChildData::getQuality));
//            for (Map.Entry<Quality,List<FabricInDetailsChildData>> entry : fabricInDetailsDataListMap.entrySet()) {
//
//                //child record
//                PdfPTable qualityTable = new PdfPTable(8);
//                qualityTable.setWidthPercentage(100f);
//                //qualityTable.setWidths(new float[] {1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f});
//                PdfPCell qualityCell = new PdfPCell();
//                qualityCell.setBackgroundColor(Color.BLUE);
//                qualityCell.setPadding(5);
//
//                //column header
//                qualityCell.setPhrase(new Phrase("Quality Name", font));
//                qualityTable.addCell(qualityCell);
//                qualityCell.setPhrase(new Phrase("Quality Id", font));
//                qualityTable.addCell(qualityCell);
//                qualityCell.setPhrase(new Phrase("Rate", font));
//                qualityTable.addCell(qualityCell);
//                qualityCell.setPhrase(new Phrase("Batch Id", font));
//                qualityTable.addCell(qualityCell);
//                qualityCell.setPhrase(new Phrase("Total Mtr", font));
//                qualityTable.addCell(qualityCell);
//                qualityCell.setPhrase(new Phrase("Total Wt", font));
//                qualityTable.addCell(qualityCell);
//                qualityCell.setPhrase(new Phrase("Total Pcs", font));
//                qualityTable.addCell(qualityCell);
//                qualityCell.setPhrase(new Phrase("Billing Values", font));
//                qualityTable.addCell(qualityCell);
//
//                for(FabricInDetailsChildData fabricInDetailsChildData:entry.getValue())
//                {
//                    qualityTable.addCell(fabricInDetailsChildData.getQualityName());
//                    qualityTable.addCell(fabricInDetailsChildData.getQualityId());
//                    qualityTable.addCell(fabricInDetailsChildData.getRate().toString());
//                    qualityTable.addCell(fabricInDetailsChildData.getBatchId());
//                    qualityTable.addCell(fabricInDetailsChildData.getTotalMtr().toString());
//                    qualityTable.addCell(fabricInDetailsChildData.getTotalWt().toString());
//                    qualityTable.addCell(fabricInDetailsChildData.getTotalPcs().toString());
//                    qualityTable.addCell(fabricInDetailsChildData.getBillingValue().toString());
//                    //masterTable.addCell(qualityTable);
//                }
//                masterTable.addCell(qualityTable);
//
//
//            }
//
//
//        }


        PdfPCell cellBlankRow = new PdfPCell(new Phrase(" "));

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        PdfPCell masterCell = new PdfPCell();
        //masterCell.setBackgroundColor(Color.BLUE);
        masterCell.setPadding(5);
        masterCell.setPhrase(new Phrase("Master: " + fabricInDetailsMast.getMasterName()));
        masterCell.setColspan(8);
        masterTable.addCell(masterCell);

        masterTable.completeRow();
        cellBlankRow.setColspan(8);
        masterTable.addCell(cellBlankRow);


        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);


        PdfPTable partyTable = new PdfPTable(8);
        partyTable.setWidthPercentage(100f);
        //partyTable.setWidths(new float[] {1.5f,1.5f,1.5f,1.5f,1.5f,1.5f});
        partyTable.setSpacingBefore(10);

        for (FabricInDetailsData fabricInDetailsData : fabricInDetailsMast.getList()) {
            PdfPCell partyNameCell = new PdfPCell();
            partyNameCell.setBackgroundColor(Color.BLUE);
            partyNameCell.setPadding(5);


            font.setColor(Color.WHITE);
            partyNameCell.setPhrase(new Phrase("Party Name", font));
            partyNameCell.setColspan(3);
            masterTable.addCell(partyNameCell);

            cell.setPhrase(new Phrase("Party Code", font));
            masterTable.addCell(cell);

            cell.setPhrase(new Phrase("Total Mtr", font));
            masterTable.addCell(cell);

            cell.setPhrase(new Phrase("Total Wt", font));
            masterTable.addCell(cell);

            cell.setPhrase(new Phrase("Total Pcs", font));
            masterTable.addCell(cell);

            cell.setPhrase(new Phrase("Billing Value", font));
            masterTable.addCell(cell);

            //masterTable.completeRow();

            //Font partyDataCell = new Font(Font.HELVETICA);
            Color partyDataColor = getColorBasedOnMasterName(fabricInDetailsMast.getMasterName());
            //partyDataCell.setColor(partyDataColor);

            partyNameCell = new PdfPCell();
            partyNameCell.setPadding(5);
            partyNameCell.setColspan(3);
            partyNameCell.setBackgroundColor(partyDataColor);
            partyNameCell.setPhrase(new Phrase(fabricInDetailsData.getPartyName()));
            masterTable.addCell(partyNameCell);

            partyNameCell = new PdfPCell();
            partyNameCell.setPadding(5);
            partyNameCell.setBackgroundColor(partyDataColor);
            partyNameCell.setPhrase(new Phrase(fabricInDetailsData.getPartyCode()));
            masterTable.addCell(partyNameCell);
            partyNameCell.setPhrase(new Phrase(fabricInDetailsData.getTotalMtr().toString()));
            masterTable.addCell(partyNameCell);
            partyNameCell.setPhrase(new Phrase(fabricInDetailsData.getTotalWt().toString()));
            masterTable.addCell(partyNameCell);
            partyNameCell.setPhrase(new Phrase(fabricInDetailsData.getTotalPcs().toString()));
            masterTable.addCell(partyNameCell);
            partyNameCell.setPhrase(new Phrase(fabricInDetailsData.getTotalBillingValue().toString()));
            masterTable.addCell(partyNameCell);
//            masterTable.addCell(new Phrase(fabricInDetailsData.getPartyCode(),partyDataCell));
//            masterTable.addCell(new Phrase(fabricInDetailsData.getTotalMtr().toString(),partyDataCell));
//            masterTable.addCell(new Phrase(fabricInDetailsData.getTotalWt().toString(),partyDataCell));
//            masterTable.addCell(new Phrase(fabricInDetailsData.getTotalPcs().toString(),partyDataCell));
//            masterTable.addCell(new Phrase(fabricInDetailsData.getTotalBillingValue().toString(),partyDataCell));
            masterTable.completeRow();
            masterTable.addCell(cellBlankRow);

            //masterTable.addCell(partyTable);

            //partyTable.completeRow();

            Map<Quality, List<FabricInDetailsChildData>> fabricInDetailsDataListMap = fabricInDetailsData.getList().stream().collect(Collectors.groupingBy(FabricInDetailsChildData::getQuality));
            for (Map.Entry<Quality, List<FabricInDetailsChildData>> entry : fabricInDetailsDataListMap.entrySet()) {

                Font qualityHeaderFont = new Font(Font.HELVETICA);
                qualityHeaderFont.setSize(10);
                qualityHeaderFont.setColor(Color.white);
                //child record
                //PdfPTable qualityTable = new PdfPTable(8);
                //qualityTable.setWidthPercentage(100f);
                //qualityTable.setWidths(new float[] {1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f});
                PdfPCell qualityCell = new PdfPCell();
                qualityCell.setBackgroundColor(Color.BLUE);
                qualityCell.setPadding(5);

                //column header
                qualityCell.setPhrase(new Phrase("Quality Name", qualityHeaderFont));
                masterTable.addCell(qualityCell);
                qualityCell.setPhrase(new Phrase("Quality Id", qualityHeaderFont));
                masterTable.addCell(qualityCell);
                qualityCell.setPhrase(new Phrase("Rate", qualityHeaderFont));
                masterTable.addCell(qualityCell);
                qualityCell.setPhrase(new Phrase("Batch Id", qualityHeaderFont));
                masterTable.addCell(qualityCell);
                qualityCell.setPhrase(new Phrase("Total Mtr", qualityHeaderFont));
                masterTable.addCell(qualityCell);
                qualityCell.setPhrase(new Phrase("Total Wt", qualityHeaderFont));
                masterTable.addCell(qualityCell);
                qualityCell.setPhrase(new Phrase("Total Pcs", qualityHeaderFont));
                masterTable.addCell(qualityCell);
                qualityCell.setPhrase(new Phrase("Billing Values", qualityHeaderFont));
                masterTable.addCell(qualityCell);

                //partyTable.completeRow();

                for (FabricInDetailsChildData fabricInDetailsChildData : entry.getValue()) {
                    Font qualityDataFont = new Font(Font.HELVETICA);
                    qualityDataFont.setSize(10);
                    Color color = new Color(244,243,239);
                    color = getColorBasedOnMasterAndBillingValues(fabricInDetailsMast.getMasterName(),fabricInDetailsChildData.getBillingValue());

                    PdfPCell qualityDataCell = new PdfPCell();
                    qualityDataCell.setBackgroundColor(color);
                    qualityDataCell.setPhrase(new Phrase(fabricInDetailsChildData.getQualityName(),qualityDataFont));
                    masterTable.addCell(qualityDataCell);

                    qualityDataCell.setBackgroundColor(color);
                    qualityDataCell.setPhrase(new Phrase(fabricInDetailsChildData.getQualityId(),qualityDataFont));
                    masterTable.addCell(qualityDataCell);

                    qualityDataCell.setBackgroundColor(color);
                    qualityDataCell.setPhrase(new Phrase(fabricInDetailsChildData.getRate().toString(),qualityDataFont));
                    masterTable.addCell(qualityDataCell);

                    qualityDataCell.setBackgroundColor(color);
                    qualityDataCell.setPhrase(new Phrase(fabricInDetailsChildData.getBatchId(),qualityDataFont));
                    masterTable.addCell(qualityDataCell);

                    qualityDataCell.setBackgroundColor(color);
                    qualityDataCell.setPhrase(new Phrase(fabricInDetailsChildData.getTotalMtr().toString(),qualityDataFont));
                    masterTable.addCell(qualityDataCell);

                    qualityDataCell.setBackgroundColor(color);
                    qualityDataCell.setPhrase(new Phrase(fabricInDetailsChildData.getTotalWt().toString(),qualityDataFont));
                    masterTable.addCell(qualityDataCell);

                    qualityDataCell.setBackgroundColor(color);
                    qualityDataCell.setPhrase(new Phrase(fabricInDetailsChildData.getTotalPcs().toString(),qualityDataFont));
                    masterTable.addCell(qualityDataCell);

                    qualityDataCell.setBackgroundColor(color);
                    qualityDataCell.setPhrase(new Phrase(fabricInDetailsChildData.getBillingValue().toString(),qualityDataFont));
                    masterTable.addCell(qualityDataCell);



                    //masterTable.addCell(qualityTable);
                }

                //add row for total
                Font qualityDataFont = new Font(Font.HELVETICA);
                qualityDataFont.setSize(10);
                PdfPCell qualityTotalDataCell = new PdfPCell();
                //qualityTotalDataCell.setBackgroundColor(color);
                qualityTotalDataCell.setPhrase(new Phrase("Total",qualityDataFont));
                qualityTotalDataCell.setColspan(4);
                masterTable.addCell(qualityTotalDataCell);



                //Calculation quality wise
                Long totalPcs = entry.getValue().stream().mapToLong(FabricInDetailsChildData::getTotalPcs).sum();
                Double totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(entry.getValue().stream().mapToDouble(FabricInDetailsChildData::getTotalMtr).sum());
                Double totalWt = StockBatchServiceImpl.changeInFormattedDecimal(entry.getValue().stream().mapToDouble(FabricInDetailsChildData::getTotalWt).sum());
                Double totalBillingValue = StockBatchServiceImpl.changeInFormattedDecimal(entry.getValue().stream().mapToDouble(FabricInDetailsChildData::getBillingValue).sum());
                masterTable.addCell(totalMtr.toString());
                masterTable.addCell(totalWt.toString());
                masterTable.addCell(totalPcs.toString());
                masterTable.addCell(totalBillingValue.toString());
                masterTable.addCell(cellBlankRow);
                //masterTable.addCell(partyTable);


            }


        }


    }

    private Color getColorBasedOnMasterAndBillingValues(String masterName, Double billingValue) {
        Color color = new Color(244,243,239);
        if (billingValue < 10000) {
            color = new Color(194,24,7);
        } else if (billingValue > 10000 && billingValue <= 12500) {
            color = new Color(255,225,53);
        } else if(billingValue > 12500){
            if (masterName.equalsIgnoreCase("BRIJESH")) {
                color = new Color(252, 238, 167);
            } else if (masterName.equalsIgnoreCase("MANISH")) {
                color = new Color(255, 182, 193);
            } else if (masterName.equalsIgnoreCase("GLORY")) {
                color = new Color(144, 238, 144);
            }

        }
        return color;

    }

    private Color getColorBasedOnMasterName(String masterName) {
        Color color = new Color(244,243,239);

            if (masterName.equalsIgnoreCase("BRIJESH")) {
                color = new Color(252, 238, 167);
            } else if (masterName.equalsIgnoreCase("MANISH")) {
                color = new Color(255, 182, 193);
            } else if (masterName.equalsIgnoreCase("GLORY")) {
                color = new Color(144, 238, 144);
            }


        return color;

    }

//    private void writeTableDataWithPartyAndQuality(FabricInDetailsMast fabricInDetailsMast, PdfPTable masterTable) {
//        //design master table
//        PdfPTable partyTable = new PdfPTable(6);
//        partyTable.setWidthPercentage(100f);
//        partyTable.setWidths(new float[] {1.5f,1.5f,1.5f,1.5f,1.5f,1.5f});
//        partyTable.setSpacingBefore(10);
//
//        for(FabricInDetailsData fabricInDetailsData:fabricInDetailsMast.getList()) {
//            PdfPCell partyCell = new PdfPCell();
//            //cell.setBackgroundColor(Color.BLUE);
//            partyCell.setPadding(5);
//            Font font = FontFactory.getFont(FontFactory.HELVETICA);
//            font.setColor(Color.BLACK);
//            partyCell.setPhrase(new Phrase("Party Name :" + fabricInDetailsData.getPartyName(), font));
//            partyTable.addCell(partyCell);
//            partyCell.setPhrase(new Phrase("Party Code :" + fabricInDetailsData.getPartyCode(), font));
//            partyTable.addCell(partyCell);
//            partyCell.setPhrase(new Phrase("Total Mtr :" + fabricInDetailsData.getTotalMtr(), font));
//            partyTable.addCell(partyCell);
//            partyCell.setPhrase(new Phrase("Party Wt :" + fabricInDetailsData.getTotalWt(), font));
//            partyTable.addCell(partyCell);
//            partyCell.setPhrase(new Phrase("Party BillingValue :" + fabricInDetailsData.getTotalBillingValue(), font));
//            partyTable.addCell(partyCell);
//            partyCell.setPhrase(new Phrase("Party Pcs :" + fabricInDetailsData.getTotalPcs(), font));
//            partyTable.addCell(partyCell);
//
//            //quality table
//            for(FabricInDetailsChildData fabricInDetailsChildData:fabricInDetailsData.getList())
//            {
//                PdfPTable qualityTable = new PdfPTable(8);
//                qualityTable.setWidthPercentage(100f);
//                qualityTable.setWidths(new float[] {1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f});
//                qualityTable.setSpacingBefore(10);
//
//                PdfPCell qualityCell = new PdfPCell();
//                //cell.setBackgroundColor(Color.BLUE);
//                qualityCell.setPadding(5);
//                Font qualityFont = FontFactory.getFont(FontFactory.HELVETICA);
//                qualityFont.setColor(Color.BLACK);
//                qualityCell.setPhrase(new Phrase("Quality Name :" + fabricInDetailsChildData.getQualityName(), qualityFont));
//                qualityTable.addCell(qualityCell);
//
//                qualityCell.setPhrase(new Phrase("Quality Id :" + fabricInDetailsChildData.getQualityId(), qualityFont));
//                qualityTable.addCell(qualityCell);
//
//                qualityCell.setPhrase(new Phrase("BatchId :" + fabricInDetailsChildData.getBatchId(), qualityFont));
//                qualityTable.addCell(qualityCell);
//
//                qualityCell.setPhrase(new Phrase("Total Pcs :" + fabricInDetailsChildData.getTotalPcs(), qualityFont));
//                qualityTable.addCell(qualityCell);
//
//                qualityCell.setPhrase(new Phrase("Total Mtr :" + fabricInDetailsChildData.getTotalMtr(), qualityFont));
//                qualityTable.addCell(qualityCell);
//
//                qualityCell.setPhrase(new Phrase("Total Wt :" + fabricInDetailsChildData.getTotalWt(), qualityFont));
//                qualityTable.addCell(qualityCell);
//
//                qualityCell.setPhrase(new Phrase("QualityRate :" + fabricInDetailsChildData.getRate(), qualityFont));
//                qualityTable.addCell(qualityCell);
//
//                qualityCell.setPhrase(new Phrase("Billing Values :" + fabricInDetailsChildData.getBillingValue(), qualityFont));
//                qualityTable.addCell(qualityCell);
//
//                partyTable.addCell(qualityTable);
//
//            }
//
//            masterTable.addCell(partyTable);
//
//        }
//
//    }
}
