package com.azura4k.mcpe.Payroll.Models;


import com.azura4k.mcpe.Payroll.PayRollAPI;

import com.azura4k.mcpe.Payroll.PayrollBase;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Business {

    PayRollAPI api = new PayRollAPI();

    public String BusinessName;
    public String BusinessDesc;
    public String CreationDate;
    public Player Owner;
    public double Balance;
    public int TrustedRank;
    public int MaxRank;
    public int MinRank;
    public ArrayList<Employee> Employees = new ArrayList<>();


    public void SaveData(){
        PayRollAPI.SaveNewBusinessData(this);
    }

    //Bank actions
    public boolean Deposit(Employee Depositor, double DepositValue) {
        //Returns true if done, returns false if not.

        //Check and see if Employee has rank to manage.
        if (Depositor.Rank > TrustedRank) {
            OfflinePlayer player = PayRollAPI.plugin.getServer().getOfflinePlayer(Depositor.playerUUID);
            if (PayRollAPI.getEconomy().getBalance(player) >= DepositValue) {
                PayRollAPI.getEconomy().withdrawPlayer(player, DepositValue);
                Balance += DepositValue;
                SaveData();
                return true;
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    public boolean Withdraw(Employee Withdrawal, double WithdrawValue) {
        //Returns true if done, returns false if not.

        //Check and see if Employee has rank to manage.
        if (Withdrawal.Rank > TrustedRank) {
            OfflinePlayer player = PayRollAPI.plugin.getServer().getOfflinePlayer(Withdrawal.playerUUID);
            if (Balance >= WithdrawValue) {
                PayRollAPI.getEconomy().depositPlayer(player, WithdrawValue);
                Balance -= WithdrawValue;
                SaveData();
                return true;
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public void reload(){
       Business Reloaded = api.LoadBusiness(this.BusinessName);
       this.BusinessDesc = Reloaded.BusinessDesc;
       this.Owner = Reloaded.Owner;
       this.Balance = Reloaded.Balance;
       this.TrustedRank = Reloaded.TrustedRank;
       this.MaxRank = Reloaded.MaxRank;
       this.MinRank = Reloaded.MinRank;
       this.Employees = Reloaded.Employees;
    }
}
