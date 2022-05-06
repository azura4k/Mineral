package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Bank.Treasury;

import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.Objects;


public class Withdraw {
    CustomForm.Builder form = CustomForm.builder();

    public void initialize(Player player, Business business, Employee employee){
        form.title(PayRollAPI.getLanguage("DepositFormTitle"));
        form.label(PayRollAPI.getLanguage("WithdrawFormCurrentBalance") + business.Balance);
        form.input(PayRollAPI.getLanguage("WithdrawFormEnterAmount"));

        form.responseHandler((Form, reponseData)->{
            if (!Form.isClosed(reponseData)) {
                try{
                CustomFormResponse response = Form.parseResponse(reponseData);
                    double Amount = Double.parseDouble(Objects.requireNonNull(response.getInput(1)));

                    if (business.Withdraw(employee, Amount)) {
                        player.sendMessage(PayRollAPI.getLanguage("WithdrawFormSuccesful") + Amount);
                        Treasury customForm = new Treasury();
                        customForm.initialize(player, business, employee);
                    } else {
                        player.sendMessage(PayRollAPI.getLanguage("BusinessOverdraftRisk"));
                    }
                }
                catch (Exception ignored){
                    PayRollAPI.plugin.getLogger().warning(ignored.getMessage());
                }
            }
        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }
}
