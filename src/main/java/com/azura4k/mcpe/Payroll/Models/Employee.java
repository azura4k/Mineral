package com.azura4k.mcpe.Payroll.Models;


import com.azura4k.mcpe.Payroll.PayRollAPI;

import java.util.UUID;

public class Employee {

    //Loaded later with load
    public UUID playerUUID;
    public String EmployerUID;



    public String StartDate;
    public String PlayerName;
    public String Title;
    public int Rank;
    public double MaximumHours;
    public double Wage;
    public double TotalWorkHours;

    public boolean Fired;
    public String FiredDate;

    PayRollAPI api = new PayRollAPI();


    public void SaveData(){
       // PayRollAPI.SaveNewEmployeeData(this);
    }

    //Utilites
    public boolean isEmployeeOf(Business business){
        return business.UID == EmployerUID;
    }
}
