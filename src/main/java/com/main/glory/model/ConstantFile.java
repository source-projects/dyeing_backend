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
public class ConstantFile {

    public static String Null_Record_Passed = "null record passed";



    // jet module
    public static String Jet_Added = "Data added successfully";
    public static String Jet_Found = "Data found";
    public static String Jet_Not_Found = "Data not found";
    public static String Jet_Updated = "Data updated successfully";
    public static String Jet_Exist_With_Name = "jet exist with name";
    public static String Jet_Not_Exist_With_Name = "jet not exist";
    public static String Jet_Exist_With_Production = "Production exist with jet";
    public static String Jet_Exist_Without_Production = "Production not exist with jet";
    public static String Jet_Deleted = "Data deleted successfully";
    public static String Jet_Record_Exist = "Jet record exist";


    //production
    public static String Production_Found = "Data found";
    public static String Production_With_Jet = "Production is already in jet";
    public static String ProductionType_Exist = "Data Exist";
    public static String Production_Added = "Data added successfully";
    public static String Production_Not_Found = "Data not found";
    public static String Production_Updated = "Data updated successfully";
    public static String Production_Deleted = "Data deleted successfully";
    public static String Production_Unable_Deleted = "unable to delete production";
    public static String Production_Removed = "Data removed from jet ";
    public static String Production_Record_Exist = "Production record exist";

    //merge Batch
    public static String MergeBatch_Found = "Data found";
    public static String MergeBatch_Added = "Data added successfully";
    public static String MergeBatch_Not_Found = "Data not found";
    public static String MergeBatch_Updated = "Data updated successfully";
    public static String MergeBatch_Deleted = "Data deleted successfully";
    public static String MergeBatch_Unable_Deleted = "unable to delete Merge Batch";
    public static String MergeBatch_Record_Exist = "Merge Batch record exist";


    // Attendance module
    public static String Attendance_Found = "Data found";
    public static String Attendance_Added = "Data added successfully";
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
    public static String Employee_Exist = "Employee data exist";
    public static String Employee_Not_Exist = "Employee data not exist";



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
    public static String Batch_Sequence_Not_Exist = "Batch Sequence not exist";
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


    //Dyeing procecss
    public static String DyeingProcess_Added = "Data added successfully";
    public static String DyeingProcess_Updated = "Data updated successfully";
    public static String DyeingProcess_Found = "Data found";
    public static String DyeingProcess_Data_Exist = "Dyeing Process Exist";
    public static String DyeingProcess_Not_Found = "Data not found ";
    public static String DyeingProcess_Name_Exist = "Dyeing Process exist";
    public static String DyeingProcess_Deleted = "Data deleted successfully";

    //Dyeing Slip
    public static String DyeingSlip_Added = "Data added successfully";
    public static String DyeingSlip_Updated = "Data updated successfully";
    public static String DyeingSlip_Found = "Data found";
    public static String DyeingSlip_Not_Found = "Data not found ";
    public static String Additional_DyeingSlip_Not_Found = "Additional Slip not found ";
    public static String Re_DyeingSlip_Not_Found = "ReDyeing Slip not found ";
    public static String Direct_DyeingSlip_Not_Found = "Direct Dyeing slip not found ";
    public static String DyeingSlip_Name_Exist = "Dyeing Slip exist";
    public static String Additional_DyeingSlip_Exist = "Additional Dyeing Slip exist";
    public static String Direct_DyeingSlip_Exist = "Direct Dyeing Slip exist";
    public static String Re_Direct_DyeingSlip_Exist = "re-Direct Dyeing Slip exist";
    public static String DyeingSlip_Exist_With_Production = "Dyeing Slip exist With Production";
    public static String DyeingSlip_Exist_Without_Production = "Dyeing Slip not exist With Production";
    public static String DyeingSlip_Deleted = "Data deleted successfully";

    //Shade
    public static String Shade_Exist = "Shade data exist";
    public static String Shade_Not_Exist = "Shade data not exist ";
    public static String Shade_Found = "Data found";
    public static String Shade_Not_Found = "Data not found";
    public static String Shade_Added = "Data added successfully";
    public static String Shade_Updated = "Data updated successfully";
    public static String Shade_Deleted = "Data deleted successfully";




    //Authorize
    public static String Authorize_Added = "Data added successfully";
    public static String Authorize_Updated = "Data updated successfully";
    public static String Authorize_Found = "Data found";
    public static String Authorize_Not_Found = "Data not found";

    public static String ApprovedBy_Not_Found = "ApprovedBy not found";
    public static String ReceiverBy_Not_Found = "ReceiverBy not found";
    public static String Authorize_Name_Exist = "Authorize name exist";
    public static String Authorize_Deleted = "Data deleted successfully";
    public static String Authorize_Exist = "Purchase data exist";


    //quality name
    public static String Quality_Name_Added = "Data added successfully";
    public static String Quality_Name_Exist = "Quality name exist ";
    public static String Quality_Name_Not_Exist = "Quality name not exist ";
    public static String Quality_Name_Updated = "Data updated successfully";
    public static String Quality_Name_Deleted = "Data deleted successfully";


    //Quality
    public static String Quality_Data_Exist = "Quality data exist";
    public static String Quality_Data_Exist_With_QualityId = "Quality data exist for qualityId";
    public static String Quality_Data_Not_Exist = "Quality data not exist";
    public static String Quality_Data_Found = "Data found";
    public static String Quality_Data_Not_Found = "Data not found ";
    public static String Quality_Data_Added = "Data added successfully";
    public static String Quality_Data_Deleted = "Data deleted successfully";
    public static String Quality_Data_Updated = "Data updated successfully";
    public static String Quality_Data_Found_ByMaster = "Data found for master ";
    public static String Quality_Data_Not_Found_ByMaster = "Data not found for master ";
    public static String Quality_Data_Not_Added = "Data not added";



    //Party
    public static String Party_Exist = "Party data exist";
    public static String Party_Not_Exist = "Party data not exist ";
    public static String Party_Code_Exist = "Party code exist ";
    public static String Party_Found = "Data found";
    public static String Party_Not_Found = "Data not found";
    public static String Party_Added = "Data added successfully";
    public static String Party_Updated = "Data updated successfully";
    public static String Party_Deleted = "Data deleted successfully";
    public static String Party_Code_Less = "Party Code should be in 2-5 digit";
    public static String Party_Found_ByMaster = "Data found for master ";
    public static String Party_Not_Found_ByMaster = "Data not found for master ";

    //PartyQuality
    public static String PartyQuality_Data_Exist = "PartyQuality data exist";
    public static String PartyQuality_Data_Found = "Data found";
    public static String PartyQuality_Data_Not_Found = "Data not found";
    public static String PartyQuality_Data_Added = "Data added successfully";
    public static String PartyQuality_Data_Updated = "Data updated successfully";


    //StockBatch
    public static String StockBatch_Exist = "StockBatch data exist";
    public static String StockBatch_Not_Exist = "StockBatch data not exist";
    public static String StockBatch_Found = "Data found";
    public static String StockBatch_With_Batch = "Data found with batch";
    public static String StockBatch_Without_Batch = "Data found without batch";
    public static String StockBatch_Not_Found = "Data not found ";
    public static String StockBatch_Added = "Data added successfully";
    public static String StockBatch_Updated = "Data updated successfully";
    public static String StockBatch_Deleted = "Data deleted successfully";


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
    public static String Batch_Id_Found = "Data found ";
    public static String Batch_Id_Not_Found = "Data not found ";
    public static String Batch_Data_Not_Found = "Data not found ";
    public static String Batch_Data_Not_Exist = "Batch Data not Exist ";

    //Payment
    public static String Payment_Exist = "Party data exist";
    public static String Payment_Found = "Data found";
    public static String Payment_Not_Found = "Data not found";
    public static String Payment_Added = "Data added successfully";
    public static String Payment_Not_Added = "Data Not added ";
    public static String Payment_Updated = "Data updated successfully";
    public static String Payment_Deleted = "Data deleted successfully";
    public static String Bank_Found = "Data found";
    public static String Bank_Not_Found = "Data not found";

    //Purchase
    public static String Purchase_Exist = "Purchase data exist";
    public static String Purchase_Found = "Data found";
    public static String Purchase_Not_Found = "Data not found";
    public static String Purchase_Added = "Data added successfully";
    public static String Purchase_Not_Added = "Data Not added ";
    public static String Purchase_Updated = "Data updated successfully";
    public static String Purchase_Updated_By_Only_Admin = "Data updated By Admin Only ";
    public static String Purchase_Deleted = "Data deleted successfully";
    public static String Purchase_Deleted_If_Not_Checked = "Purchase not deleted because checked";




    //Machine
    public static String Machine_Data_Added = "Data added successfully";
    public static String Machine_Data_Updated = "Data updated successfully";
    public static String Machine_Data_Not_Updated = "Data not updated successfully";
    public static String Machine_Data_Deleted = "Data deleted successfully";
    public static String Machine_Data_Deletable = "Data is deletable";
    public static String Machine_Data_Not_Deletable = "Data is not deletable";
    public static String Machine_Data_Found = "Data found";
    public static String Machine_Data_Not_Found = "Data not found";
    public static String Machine_Category_Exist = "Machine category exist";
    public static String Machine_Category_Added = "Machine category Added";
    public static String Machine_Category_Updated = "Machine category Updated";


    //Color
    public static String Color_Added = "Data added successfully";
    public static String Color_Issue= "Color box issued successfully";
    public static String Color_Already_Issue= "Color box already issued ";
    public static String Color_Updated = "Data updated successfully";
    public static String Color_Deleted = "Data deleted successfully";
    public static String Color_Found = "Data found ";
    public static String Color_Not_Found = "Data not found ";
    public static String Color_Data_Exist = "Color data exist";


    //Task
    public static String Task_Added = "Data added successfully";
    public static String Task_Updated = "Data updated successfully";
    public static String Task_Deleted = "Data deleted successfully";
    public static String Task_Unable_Delete = "Unable to delete the task";
    public static String Task_Found = "Data found ";
    public static String Task_Not_Found = "Data not found ";
    public static String Task_Type_Not_Found = "Task Type not found ";


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
    public static String Dispatch_Password_Correct="Correct password entered";
    public static String Dispatch_Password_Wrong="Wrong password entered";


    //Supplier
    public static String Supplier_Added = "Data added successfully ";
    public static String Supplier_Not_Added = "Data not added  ";
    public static String Supplier_Updated = "Data updated successfully ";
    public static String Supplier_Invalid_Data = "Invalid Data send";
    public static String Supplier_Deleted = "Data deleted successfully ";
    public static String Supplier_Found = "Data found ";
    public static String Supplier_Not_Found = "Data not found ";
    public static String Supplier_Exist= "Supplier data exit ";

    //Supplier rate
    public static String SupplierRate_Added = "Data added successfully ";
    public static String SupplierRate_Not_Added = "Data not added  ";
    public static String SupplierRate_Updated = "Data updated successfully ";
    public static String SupplierRate_Deleted = "Data deleted successfully ";
    public static String SupplierRate_Found = "Data found ";
    public static String SupplierRate_Not_Found = "Data not found ";

    //get by String
    public static String GetBy_String_Wrong = "Get by String Wrong ";



    //finish mtr
    public static String FinishMtr_Data_Added = "Finish Meter Stored successfully";
    public static String FinishMtr_Data_Updated = "Finish Meter Stored successfully";
    public static String FinishMtr_Data_Found = "Data found";
    public static String FinishMtr_Data_Not_Found = "Data not found";

    //User
    public static String User_Added = "Data added successfully ";
    public static String User_Not_Added = "Data not added  ";
    public static String User_Updated = "Data updated successfully ";
    public static String User_Invalid_Data = "Invalid Data send";
    public static String User_Deleted = "Data deleted successfully ";
    public static String User_Found = "Data found ";
    public static String User_Not_Found = "Data not found ";
    public static String User_Exist = "User data exist";
    public static String User_Not_Exist = "User data not exist";
    public static String User_Login = "Successfully logged in";
    public static String User_Wrong_cred = "Wrong credential enter";


    //task
    public static String Task_Data_Exist = "Task data exist";

    //purchase
    public static String Purchase_Data_Exist = "Purchase data exist";

    //Dyeing slip
    public static String Dyeing_Slip_Data_Exist = "Dyeing Slip data exist";



    //JWT
    public static String JWT_Expired = "JWT expired";
    public static String JWT_Not_Found = "JWT not found";
    public static String Unauthorized_User = "Unauthorized User";




    //Constant variable for attendance
    public static Integer inOutTimeAuto = 12;
    public static Integer inOutTimeCheck = 12;




}