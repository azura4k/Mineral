package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Bank.Treasury.Treasury;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD.BusinessInfo;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD.DeleteBusiness;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.handler.SimpleFormHandler;

public class Options {

    PayRollAPI api = new PayRollAPI();
    SimpleForm Options = new SimpleForm();

    public void initialize(Player player, Business business){

        // All handlers for the different buttons.
        SimpleFormHandler BusinessInfo = (p, button) ->{
            BusinessInfo form = new BusinessInfo();
            form.initialize(player, business);
        };
        SimpleFormHandler Treasury = (p, button) ->{
            Treasury form = new Treasury();
            form.initialize(p, business, api.LoadEmployee(business, player.getName()));
        };

        SimpleFormHandler EmployeeInfo = (p, button) ->{

        };

        SimpleFormHandler HireEmployees = (p, button) ->{

        };

        SimpleFormHandler FireEmployees = (p, button) ->{

        };

        SimpleFormHandler Transactions = (p, button) ->{

        };

        SimpleFormHandler TransferOwner = (p, button) ->{

        };

        SimpleFormHandler QuitJob = (p, button) ->{

        };

        SimpleFormHandler DeleteBusiness = (p, button) ->{
            DeleteBusiness form = new DeleteBusiness();
            form.initialize(p, business);
        };

        //Variables
        String business_info = PayRollAPI.getLanguage("OptionsFormBusinessInfoButton");
        String treasury = PayRollAPI.getLanguage("OptionsFormTreasuryButton");
        String employee_info = PayRollAPI.getLanguage("OptionsFormEmployeeInfo");
        String hire_employee = PayRollAPI.getLanguage("OptionsFormHireEmployees");
        String fire_employee = PayRollAPI.getLanguage("OptionsFormFireEmployees");
        String transactions = PayRollAPI.getLanguage("OptionsFormTransactions");
        String transfer_ownership = PayRollAPI.getLanguage("OptionsFormTransferOwner");
        String quit_job = PayRollAPI.getLanguage("OptionsFormQuitJob");
        String delete_business = PayRollAPI.getLanguage("OptionsFormDeleteBusiness");
        //Logic for building GUI

        if (player == business.Owner) {
            Options.addButton(business_info, BusinessInfo);
            Options.addButton(treasury, Treasury);
            Options.addButton(employee_info, EmployeeInfo);
            Options.addButton(hire_employee, HireEmployees);
            Options.addButton(fire_employee, FireEmployees);
            Options.addButton(transactions, Transactions);
            Options.addButton(transfer_ownership, TransferOwner);
            Options.addButton(delete_business, DeleteBusiness);

        }

        if (api.LoadEmployee(business, player.getName()).Rank >= business.TrustedRank && player != business.Owner) {
            Options.addButton(business_info, BusinessInfo);
            Options.addButton(treasury, Treasury);
            Options.addButton(employee_info, EmployeeInfo);
            Options.addButton(hire_employee, HireEmployees);
            Options.addButton(fire_employee, FireEmployees);
            Options.addButton(transactions, Transactions);
            Options.addButton(quit_job, QuitJob);
        }

        if (api.LoadEmployee(business, player.getName()).Rank < business.TrustedRank) {
            Options.addButton(business_info, BusinessInfo);
            Options.addButton(employee_info, EmployeeInfo);
            Options.addButton(transactions, Transactions);
            Options.addButton(quit_job, QuitJob);
        }

        Options.send(player);

    }
}
