package com.main.glory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class CommonMessage {

    public static String Null_Record_Passed = "null record passed";



    // jet module
    public static String Jet_Added = "Data added successfully";
    public static String Jet_Found = "Data found";
    public static String Jet_Not_Found = "Data not found";
    public static String Jet_Updated = "Data updated successfully";
    public static String Jet_Exist_With_Name = "jet exist with name";
    public static String Jet_Deleted = "Data deleted successfully";
    public static String Jet_Record_Exist = "Jet record exist";

    // Attendance module
    public static String Attendance_Added = "Data added successfully";
    public static String Attendance_Found = "Data found";
    public static String Attendance_Not_Found = "Data not found";
    public static String Attendance_Updated = "Data updated successfully";
    public static String Attendance_Deleted = "Data deleted successfully";
    public static String Attendance_Record_Exist = "Attendance record exist";


    // Employee module
    public static String Employee_Added = "Data added successfully";
    public static String Employee_Found = "Data found";
    public static String Employee_Not_Found = "Data not found";
    public static String Employee_Updated = "Data updated successfully";
    public static String Employee_Deleted = "Data deleted successfully";




    //company
    public static String Company_Added = "Data added successfully";
    public static String Company_Updated = "Data updated successfully";
    public static String Company_Found = "Data found";
    public static String Company_Not_Found = "Data not found";
    public static String Company_Name_Exist = "Company name exist";
    public static String Company_Deleted = "Data deleted successfully";

    //Invoice sequences
    public static String Invoice_Sequence_Added = "Data added successfully";
    public static String Invoice_Sequence_Updated = "Data updated successfully";
    public static String Invoice_Sequence_Found = "Invoice Sequence found";
    public static String Invoice_Sequence_Exist = "Data already exist";
    public static String Invoice_Sequence_Not_Found = "Invoice Sequence not found";
    public static String Invoice_Sequence_Greater = "Enter greater sequence";

    //Batch sequences
    public static String Batch_Sequence_Added = "Data added successfully";
    public static String Batch_Sequence_Updated = "Data updated successfully";
    public static String Batch_Sequence_Found = "Data found";
    public static String Batch_Sequence_Exist = "Data already exist";
    public static String Batch_Sequence_Not_Found = "Data not found";
    public static String Batch_Sequence_Greater = "Enter greater sequence";



    //report type
    public static String Report_Type_Added = "Data added successfully";
    public static String Report_Type_Updated = "Data updated successfully";
    public static String Report_Type_Found = "Data found";
    public static String Report_Type_Exist = "Data already exist";
    public static String Report_Type_Not_Found = "Data not found";
    public static String Report_Type_Deleted = "Data deleted successfully";



    //department
    public static String Department_Added = "Data added successfully";
    public static String Department_Updated = "Data updated successfully";
    public static String Department_Found = "Data found";
    public static String Department_Not_Found = "Data not found";
    public static String Department_Name_Exist = "Department name exist";
    public static String Department_Deleted = "Data deleted successfully";

    //department
    public static String Authorize_Added = "Data added successfully";
    public static String Authorize_Updated = "Data updated successfully";
    public static String Authorize_Found = "Data found";
    public static String Authorize_Not_Found = "Data not found";
    public static String Authorize_Name_Exist = "Authorize name exist";
    public static String Authorize_Deleted = "Data deleted successfully";

    //quality name
    public static String Quality_Name_Added = "Data added successfully";
    public static String Quality_Name_Exist = "Quality name exist ";
    public static String Quality_Name_Not_Exist = "Quality name not exist ";
    public static String Quality_Name_Updated = "Data updated successfully";
    public static String Quality_Name_Deleted = "Data deleted successfully";


    //Quality
    public static String Quality_Data_Exist = "Quality data exist";
    public static String Quality_Data_Found = "Data found";
    public static String Quality_Data_Not_Found = "Data not found";
    public static String Quality_Data_Added = "Data added successfully";
    public static String Quality_Data_Updated = "Data updated successfully";
    public static String Quality_Data_Found_ByMaster = "Data found for master ";
    public static String Quality_Data_Not_Found_ByMaster = "Data not found for master ";


    //Party
    public static String Party_Data_Exist = "Party data exist";
    public static String Party_Data_Found = "Data found";
    public static String Party_Data_Not_Found = "Data not found";
    public static String Party_Data_Added = "Data added successfully";
    public static String Party_Data_Updated = "Data updated successfully";
    public static String Party_Data_Found_ByMaster = "Data found for master ";
    public static String Party_Data_Not_Found_ByMaster = "Data not found for master ";

    //PartyQuality
    public static String PartyQuality_Data_Exist = "PartyQuality data exist";
    public static String PartyQuality_Data_Found = "Data found";
    public static String PartyQuality_Data_Not_Found = "Data not found";
    public static String PartyQuality_Data_Added = "Data added successfully";
    public static String PartyQuality_Data_Updated = "Data updated successfully";


    //StockBatch
    public static String StockBatch_Exist = "StockBatch data exist";
    public static String StockBatch_Found = "Data found";
    public static String StockBatch_Not_Found = "Data not found ";
    public static String StockBatch_Added = "Data added successfully";
    public static String StockBatch_Updated = "Data updated successfully";
    public static String StockBatch_Found_ByMaster = "Data found for master ";
    public static String StockBatch_Not_Found_ByMaster = "Data not found for master ";
    public static String StockBatch_Found_ByParty = "Data found for Party ";
    public static String StockBatch_Not_Found_ByParty = "Data not found for Party ";
    public static String StockBatch_Found_ByQuality = "Data found for quality ";
    public static String StockBatch_Not_Found_ByQuality = "Data not found for quality ";
    public static String StockBatch_Found_ByPartyQuality = "Data found for Party and Quality ";
    public static String StockBatch_Not_Found_ByPartyQuality = "Data not found for Party and Quality ";


    //batch
    public static String Batch_Data_Added = "Data added successfully";
    public static String Batch_Data_Updated = "Data updated successfully";
    public static String Batch_Data_Deleted = "Data deleted successfully";
    public static String Batch_Data_Found = "Data found";
    public static String Batch_Data_Not_Found = "Data not found";


    //Machine
    public static String Machine_Data_Added = "Data added successfully";
    public static String Machine_Data_Updated = "Data updated successfully";
    public static String Machine_Data_Not_Updated = "Data not updated successfully";
    public static String Machine_Data_Deleted = "Data deleted successfully";
    public static String Machine_Data_Found = "Data found";
    public static String Machine_Data_Not_Found = "Data not found";


    //Color
    public static String Color_Added = "Data added successfully";
    public static String Color_Issue= "Color box issued successfully";
    public static String Color_Already_Issue= "Color box already issued ";
    public static String Color_Updated = "Data updated successfully";
    public static String Color_Deleted = "Data deleted successfully";
    public static String Color_Found = "Data found ";
    public static String Color_Not_Found = "Data not found ";

    //Designation
    public static String Designation_Added = "Data added successfully";
    public static String Designation_Not_Added = "Data not added successfully";
    public static String Designation_Exit= "Designation exist";
    public static String Designation_Updated = "Data updated successfully";
    public static String Designation_Deleted = "Data deleted successfully";
    public static String Designation_Found = "Data found ";
    public static String Designation_Not_Found = "Data not found ";
    public static String Designation_Deletable = "Data is deletable ";
    public static String Designation_Not_Deletable = "Data is not deletable ";

    //Dispatch
    public static String Dispatch_Added = "Data added successfully";
    public static String Dispatch_Not_Added = "Data not added successfully";
    public static String Dispatch_Exit= "Dispatch exist";
    public static String Dispatch_Updated = "Data updated successfully";
    public static String Dispatch_Deleted = "Data deleted successfully";
    public static String Dispatch_Found = "Data found ";
    public static String Dispatch_Not_Found = "Data not found ";


    //Supplier
    public static String Supplier_Added = "Data added successfully ";
    public static String Supplier_Updated = "Data updated successfully ";
    public static String Supplier_Deleted = "Data deleted successfully ";
    public static String Supplier_Found = "Data found ";
    public static String Supplier_Not_Found = "Data not found ";


    //get by String
    public static String GetBy_String_Wrong = "Get by String Wrong ";



    //finish mtr
    public static String FinishMtr_Data_Added = "Finish Meter Stored successfully";
    public static String FinishMtr_Data_Updated = "Finish Meter Stored successfully";
    public static String FinishMtr_Data_Found = "Data found";
    public static String FinishMtr_Data_Not_Found = "Data not found";

    //User
    public static String User_Data_Exist = "User data exist";
    public static String User_Data_Not_Exist = "User data not exist";

    //task
    public static String Task_Data_Exist = "Task data exist";

    //purchase
    public static String Purchase_Data_Exist = "Purchase data exist";

    //Dyeing slip
    public static String Dyeing_Slip_Data_Exist = "Dyeing Slip data exist";






}