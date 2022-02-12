package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Bank.Treasury;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.handler.SimpleFormHandler;

public class Treasury {
    SimpleForm form = new SimpleForm(PayRollAPI.getLanguage("BankOptionsFormTitle"));

    Business Business;
    Employee employee;
    public void initialize (Player player, Business business, Employee employee1){
        Business = business;
        employee = employee1;
        form.addContent(PayRollAPI.getLanguage("BankOptionsFormBalanceText") + business.Balance);
        form.addButton(PayRollAPI.getLanguage("BankOptionsFormDepositButton"), Deposit);
        form.addButton(PayRollAPI.getLanguage("BankOptionsFormWithdrawButton"), Withdraw);
        form.addButton(PayRollAPI.getLanguage("BackButton"),  BackButton);
        form.send(player);
    }

    //Handlers
    SimpleFormHandler Deposit = (p, button) -> {
        Deposit form = new Deposit();
        form.initialize(p.getPlayer(), Business, employee);
    };

    SimpleFormHandler Withdraw = (p, button) -> {
        Withdraw form = new Withdraw();
        form.initialize(p.getPlayer(), Business, employee);
    };

    SimpleFormHandler BackButton = (p, button) -> {
        Options options = new Options();
        options.initialize(p.getPlayer(), Business);
    };


}
