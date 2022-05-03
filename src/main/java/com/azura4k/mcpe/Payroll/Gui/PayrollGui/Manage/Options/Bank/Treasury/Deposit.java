package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Bank.Treasury;


import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class Deposit {

    PayRollAPI api = new PayRollAPI();
    CustomForm.Builder form = CustomForm.builder();
    public void initialize(Player player, Business business, Employee employee){
        form.title(PayRollAPI.getLanguage("DepositFormTitle"));
        form.label(PayRollAPI.getLanguage("DepositFormCurrentBalance") + business.Balance);
        form.input(PayRollAPI.getLanguage("DepositFormEnterAmount"));
        form.responseHandler((form, responseData) -> {
            CustomFormResponse response = form.parseResponse(responseData);
            Double Amount = Double.valueOf(response.getInput(1));
            if (business.Deposit(employee, Amount)){
                player.sendMessage(PayRollAPI.getLanguage("DepositFormSuccesful") + Amount);
                Treasury customForm = new Treasury();
                customForm.initialize(player, business, employee);
            }
            else{
                player.sendMessage(PayRollAPI.getLanguage("OverDraftRisk"));
            }
        });
       FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }

}
