package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Bank.Treasury;


import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class Treasury {
    SimpleForm.Builder form = SimpleForm.builder();
    Business Business;
    Employee employee;
    public void initialize (Player player, Business business, Employee employee1){

        Business = business;
        employee = employee1;
        form.title(PayRollAPI.getLanguage("BankOptionsFormTitle"));
        form.content(PayRollAPI.getLanguage("BankOptionsFormBalanceText") + business.Balance);
        form.button(PayRollAPI.getLanguage("BankOptionsFormDepositButton"));
        form.button(PayRollAPI.getLanguage("BankOptionsFormWithdrawButton"));
        form.button(PayRollAPI.getLanguage("BackButton"));
        form.responseHandler((form, reponseData)->{
            try{
            if (!form.isClosed(reponseData)){
            SimpleFormResponse response = form.parseResponse(reponseData);
            if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("BankOptionsFormDepositButton"))){
                Deposit Customform = new Deposit();
                Customform.initialize(player, Business, employee);
            }
            if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("BankOptionsFormWithdrawButton"))){
                Withdraw Customform = new Withdraw();
                Customform.initialize(player, Business, employee);
            }
            if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("BackButton"))){
                Options options = new Options();
                options.initialize(player, Business);
            }
            }}catch (Exception ignored){
                PayRollAPI.plugin.getLogger().warning(ignored.getMessage());
            }
        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }
}
