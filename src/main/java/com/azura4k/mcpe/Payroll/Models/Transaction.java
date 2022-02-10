package com.azura4k.mcpe.Payroll.Models;

public class Transaction {

    String CustomerName;
    String EmployeeName;
    double TotalAmount;
    String BusinessID;
    String DateTime;
    String TransactionID;

    public void CreateTransaction(String customerName, String employeeName, double total, String businessID, String dateTime){
        //Add to database
        CustomerName = CustomerName;
        EmployeeName = employeeName;
        TotalAmount = total;
        BusinessID = businessID;
        DateTime = dateTime;

        //TODO Post to database
    }
    public void LoadTransaction(String businessID, String transactionID ){
        //TODO LOAD FROM TABLE WITH THIS SPECIFC
    }

    public String getCustomerName() {
        return CustomerName;
    }
    public String getEmployeeName() {
        return EmployeeName;
    }
    public double getTotalAmount() {
        return TotalAmount;
    }
}
