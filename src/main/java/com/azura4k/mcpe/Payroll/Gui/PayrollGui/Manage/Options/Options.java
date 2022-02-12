package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Bank.Treasury.Treasury;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD.BusinessInfo;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD.DeleteBusiness;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD.TransferOwnership;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees.EmployeeSelection;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees.HireEmployee;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees.QuitJob;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.SelectMenu;
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

        SimpleFormHandler HireEmployees = (p, button) ->{
            HireEmployee form = new HireEmployee();
            form.initalize(player, business);
        };

        SimpleFormHandler EmployeeInfo = (p, button) ->{
            EmployeeSelection form = new EmployeeSelection();
            form.Initialize(p, business);
        };

        SimpleFormHandler TransferOwner = (p, button) ->{
            TransferOwnership form = new TransferOwnership();
            form.Initalize(player, business);
        };

        SimpleFormHandler QuitJob = (p, button) ->{
            QuitJob form = new QuitJob();
            form.Initialize(p, business, api.LoadEmployee(business, player.getName()));
        };

        SimpleFormHandler DeleteBusiness = (p, button) ->{
            DeleteBusiness form = new DeleteBusiness();
            form.initialize(p, business);
        };

        SimpleFormHandler BackButton = (p, button) -> {
            SelectMenu menu = new SelectMenu();
            menu.initialize(p);
        };

        //Variables
        String business_info = PayRollAPI.getLanguage("OptionsFormBusinessInfoButton");
        String treasury = PayRollAPI.getLanguage("OptionsFormTreasuryButton");
        String employee_info = PayRollAPI.getLanguage("OptionsFormEmployeeInfo");
        String hire_employee = PayRollAPI.getLanguage("OptionsFormHireEmployees");
        String transfer_ownership = PayRollAPI.getLanguage("OptionsFormTransferOwner");
        String quit_job = PayRollAPI.getLanguage("OptionsFormQuitJob");
        String delete_business = PayRollAPI.getLanguage("OptionsFormDeleteBusiness");
        String back_button = PayRollAPI.getLanguage("BackButton");
        //Logic for building GUI

        if (player == business.Owner) {
            Options.addButton(business_info, BusinessInfo);
            Options.addButton(treasury, Treasury);
            Options.addButton(hire_employee, HireEmployees);
            Options.addButton(employee_info, EmployeeInfo);
            Options.addButton(transfer_ownership, TransferOwner);
            Options.addButton(delete_business, DeleteBusiness);
            Options.addButton(back_button, BackButton);

        }

        if (api.LoadEmployee(business, player.getName()).Rank >= business.TrustedRank && player != business.Owner) {
            Options.addButton(business_info, BusinessInfo);
            Options.addButton(treasury, Treasury);
            Options.addButton(hire_employee, HireEmployees);
            Options.addButton(employee_info, EmployeeInfo);
            Options.addButton(quit_job, QuitJob);
            Options.addButton(back_button, BackButton);
        }

        if (api.LoadEmployee(business, player.getName()).Rank < business.TrustedRank && player != business.Owner) {
            Options.addButton(business_info, BusinessInfo);
            Options.addButton(employee_info, EmployeeInfo);
            Options.addButton(quit_job, QuitJob);
            Options.addButton(back_button, BackButton);
        }

        Options.send(player);

    }
}
