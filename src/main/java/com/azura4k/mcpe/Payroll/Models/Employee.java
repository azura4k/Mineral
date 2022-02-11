package com.azura4k.mcpe.Payroll.Models;


import com.azura4k.mcpe.Payroll.PayRollAPI;

import java.util.UUID;

public class Employee {

    //Loaded later with load
    public UUID playerUUID;
    public String EmployerName;

    public String StartDate;
    public String PlayerName;
    public String Title;
    public int Rank;
    public double MaximumHours;
    public double Wage;
    public double TotalWorkHours;
    public double HoursWorkedPerPayPeriod;

    public boolean Fired;
    public String FiredDate;

    PayRollAPI api = new PayRollAPI();


    public void SaveData(){
       PayRollAPI.SaveNewEmployeeData(this);
    }

    //Utilites
    public boolean isFired(){
        return Fired;
    }
}
