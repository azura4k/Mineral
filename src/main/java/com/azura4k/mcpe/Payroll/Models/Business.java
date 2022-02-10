package com.azura4k.mcpe.Payroll.Models;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import net.lldv.llamaeconomy.LlamaEconomy;

public class Business {

    public String BusinessName;
    public String UID;
    public String BusinessDesc;
    public String CreationDate;
    public Player Owner;
    public double Balance;
    public int TrustedRank;
    public int MaxRank;
    public int MinRank;


    public void SaveData(){
        PayRollAPI.SaveNewBusinessData(this);
    }
    public boolean RenameBusiness(String NewName){
       if (PayRollAPI.RenameBusiness(BusinessName, NewName)){
           return true;
       }
       else{
           return false;
       }
    }

    //Bank actions
    public double getBalance(){
        return Balance;
    }
    public boolean Deposit(Employee Depositor, double DepositValue) {
        //Returns true if done, returns false if not.

        //Check and see if Employee has rank to manage.
        if (Depositor.Rank > TrustedRank) {
            if (LlamaEconomy.getAPI().getMoney(Depositor.playerUUID) >= DepositValue) {
                LlamaEconomy.getAPI().reduceMoney(Depositor.playerUUID, DepositValue);
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
            if (Balance >= WithdrawValue) {
                LlamaEconomy.getAPI().addMoney(Withdrawal.playerUUID, WithdrawValue);
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
    public boolean PayBusinessAsCustomer(Player Customer, double Total, String EmployeeName) {
        if (LlamaEconomy.getAPI().getMoney(Customer) >= Total) {
            LlamaEconomy.getAPI().reduceMoney(Customer.getUniqueId(), Total);
            Balance += Total;
            Transaction transaction = new Transaction();
            transaction.CreateTransaction(Customer.getName(), EmployeeName, Total, UID, PayRollAPI.getDate());
            //TODO Add give recipt with config check.
            return true;
        } else {
            return false;
        }
    }

    public boolean isBusiness(){
        if (UID.length() > 0){return true;} else {return false;}
    }
}
