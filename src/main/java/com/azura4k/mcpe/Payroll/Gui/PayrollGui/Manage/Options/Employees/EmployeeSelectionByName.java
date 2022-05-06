package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;

public class EmployeeSelectionByName {

    PayRollAPI api = new PayRollAPI();
    CustomForm.Builder form = CustomForm.builder();
    public void initialize(Player player, Business business) {

        form.title(PayRollAPI.getLanguage("EmployeeSearchFormTitle"));
        form.input(PayRollAPI.getLanguage("EmployeeSearchFormInstructions"));
        form.responseHandler((form, reponseData)->{
            if (!form.isClosed(reponseData)){
                try{
            CustomFormResponse response = form.parseResponse(reponseData);

            if (response.getInput(0) == null){
                player.sendMessage(PayRollAPI.getLanguage("NoValueDetected"));
            }
            else {
                String Query = response.getInput(0);
                Employee employee = api.LoadEmployee(business, Query);
                if (!(employee == null)) {
                    EmployeeSelectionOptions customForm = new EmployeeSelectionOptions();
                    customForm.initialize(player, business, employee);
                } else {
                    player.sendMessage(PayRollAPI.getLanguage("NoEmployeeFound"));
                    EmployeeSelection form2 = new EmployeeSelection();
                    form2.Initialize(player, business);
                }}
            }catch (Exception ignored){}
        }});
            FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }

}
