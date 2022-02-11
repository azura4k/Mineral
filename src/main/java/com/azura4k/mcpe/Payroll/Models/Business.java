package com.azura4k.mcpe.Payroll.Models;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import net.lldv.llamaeconomy.LlamaEconomy;

import java.util.ArrayList;

public class Business {

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
}
