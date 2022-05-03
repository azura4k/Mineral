package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;


import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


import java.util.Objects;

public class EmployeeSelectionOptions {

    SimpleForm.Builder form = SimpleForm.builder();
    Business Business;
    Employee Employee;

    public void initialize (Player player, Business business, Employee employee){
        Business = business;
        Employee = employee;
        form.button(PayRollAPI.getLanguage("EmployeeSelectionFormManage"));
        if (!(Objects.equals(employee.PlayerName, business.Owner.getName()))){
            form.button(PayRollAPI.getLanguage("EmployeeSelectionFormTerm"));
        }
        form.responseHandler((Form, reponseData)->{
            SimpleFormResponse response = Form.parseResponse(reponseData);
            if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("EmployeeSelectionFormManage"))){
                EmployeeInfoPage form = new EmployeeInfoPage();
                form.initialize(player,Business, Employee);
            }
            if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("EmployeeSelectionFormTerm"))){
                FireEmployee form = new FireEmployee();
                form.initialize(player, Business, Employee);
            }
        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }
}
