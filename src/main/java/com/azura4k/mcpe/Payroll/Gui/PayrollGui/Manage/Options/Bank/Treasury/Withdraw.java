package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Bank.Treasury;

import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class Withdraw {
    PayRollAPI api = new PayRollAPI();
    CustomForm.Builder form = CustomForm.builder();

    public void initialize(Player player, Business business, Employee employee){
        form.title(PayRollAPI.getLanguage("DepositFormTitle"));
        form.label(PayRollAPI.getLanguage("WithdrawFormCurrentBalance") + business.Balance);
        form.input(PayRollAPI.getLanguage("WithdrawFormEnterAmount"));

        form.responseHandler((form, reponseData)->{
            CustomFormResponse response = form.parseResponse(reponseData);
            if (response.isCorrect()){
                Double Amount = Double.valueOf(response.getInput(1));

                if (business.Withdraw(employee, Amount)){
                    player.sendMessage(PayRollAPI.getLanguage("WithdrawFormSuccesful") + Amount);
                    Treasury customForm = new Treasury();
                    customForm.initialize(player, business, employee);
                }
                else{
                    player.sendMessage(PayRollAPI.getLanguage("BusinessOverdraftRisk"));
                }
            }

        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }
}
