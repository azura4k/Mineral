package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;


import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.Objects;

public class EmployeeSelection {

    PayRollAPI api = new PayRollAPI();

    Business Business;
    Employee employee;

    public void Initialize(Player player, Business business){
        SimpleForm.Builder form = SimpleForm.builder();
        business.reload();
        Business = business;
        form.title(PayRollAPI.getLanguage("EmployeeSelectionFormTitle"));
        form.button(PayRollAPI.getLanguage("EmployeeSelectionFormSearchButton"));
        for (int i = 0; i < business.Employees.size() ; i++) {
                String Name = business.Employees.get(i).PlayerName;
            if (!Objects.equals(Name, Business.Owner.getName()) || Objects.equals(player, business.Owner)) {
                form.button(Name);
            }

        }
        form.button(PayRollAPI.getLanguage("BackButton"));

        form.responseHandler((modal, reponseData) -> {
            if (!modal.isClosed(reponseData)){
                try{
            SimpleFormResponse response = modal.parseResponse(reponseData);
            if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("EmployeeSelectionFormSearchButton"))) {
                EmployeeSelectionByName customForm = new EmployeeSelectionByName();
                customForm.initialize(player, Business);
            }
            else if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("BackButton"))){
                Options customForm = new Options();
                customForm.initialize(player, Business);
            }
            else if (response.isCorrect()) {
                EmployeeSelectionOptions customForm = new EmployeeSelectionOptions();
                employee = api.LoadEmployee(Business, response.getClickedButton().getText());
                customForm.initialize(player, Business, employee);
            }
            }catch (Exception ignored){
                    PayRollAPI.plugin.getLogger().warning(ignored.getMessage());
                }
            }
        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }
}
