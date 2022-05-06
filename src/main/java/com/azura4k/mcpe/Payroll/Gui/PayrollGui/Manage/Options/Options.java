package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options;

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
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class Options {

    PayRollAPI api = new PayRollAPI();
    SimpleForm.Builder Options = SimpleForm.builder();

    public void initialize(Player player, Business business){

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
            Options.button(business_info);
            Options.button(treasury);
            Options.button(hire_employee);
            Options.button(employee_info);
            Options.button(transfer_ownership);
            Options.button(delete_business);
            Options.button(back_button);
        }

        if (api.LoadEmployee(business, player.getName()).Rank >= business.TrustedRank && player != business.Owner) {
            Options.button(business_info);
            Options.button(treasury);
            Options.button(hire_employee);
            Options.button(employee_info);
            Options.button(quit_job);
            Options.button(back_button);
        }

        if (api.LoadEmployee(business, player.getName()).Rank < business.TrustedRank && player != business.Owner) {
            Options.button(business_info);
            Options.button(employee_info);
            Options.button(quit_job);
            Options.button(back_button);
        }

        Options.responseHandler((form, responseData) -> {
            if (!form.isClosed(responseData)) {
                try {
                    SimpleFormResponse response = form.parseResponse(responseData);

                    if (response.getClickedButton().getText().equals(business_info)) {
                        BusinessInfo customForm = new BusinessInfo();
                        customForm.initialize(player, business);
                    } else if (response.getClickedButton().getText().equals(treasury)) {
                        Treasury customForm = new Treasury();
                        customForm.initialize(player, business, api.LoadEmployee(business, player.getName()));
                    } else if (response.getClickedButton().getText().equals(employee_info)) {
                        EmployeeSelection customForm = new EmployeeSelection();
                        customForm.Initialize(player, business);
                    } else if (response.getClickedButton().getText().equals(hire_employee)) {
                        HireEmployee customForm = new HireEmployee();
                        customForm.initalize(player, business);
                    } else if (response.getClickedButton().getText().equals(transfer_ownership)) {
                        TransferOwnership customForm = new TransferOwnership();
                        customForm.Initialize(player, business);
                    } else if (response.getClickedButton().getText().equals(quit_job)) {
                        QuitJob customForm = new QuitJob();
                        customForm.Initialize(player, business, api.LoadEmployee(business, player.getName()));
                    } else if (response.getClickedButton().getText().equals(delete_business)) {
                        DeleteBusiness customForm = new DeleteBusiness();
                        customForm.initialize(player, business);
                    } else if (response.getClickedButton().getText().equals(back_button)) {
                        SelectMenu menu = new SelectMenu();
                        menu.initialize(player);
                    }
                }catch (Exception ignored){
                    PayRollAPI.plugin.getLogger().warning(ignored.getMessage());
                }
            }
        });


        FloodgateApi.getInstance().sendForm(player.getUniqueId(), Options);

    }
}
