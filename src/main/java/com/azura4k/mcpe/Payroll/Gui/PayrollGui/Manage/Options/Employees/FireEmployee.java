package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.ModalForm;
import org.geysermc.cumulus.response.ModalFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class FireEmployee {

    ModalForm.Builder form = ModalForm.builder();
    PayRollAPI api = new PayRollAPI();


    public void initialize (Player player, Business business, Employee employee){
        form.title(PayRollAPI.getLanguage("EmployeeTerminationModalTitle"));
        form.content(PayRollAPI.getLanguage("EmployeeTerminationModalContentPt1") + employee.PlayerName + PayRollAPI.getLanguage("EmployeeTerminationModalContentPt2") + employee.EmployerName);
        form.button1(PayRollAPI.getLanguage("EmployeeTerminationModalYesButton"));
        form.button2(PayRollAPI.getLanguage("EmployeeTerminationModalNoButton"));
        form.responseHandler( (form, data) ->{
            ModalFormResponse result = form.parseResponse(data);
            if (result.getResult()){
                api.FireEmployee(employee);
                EmployeeSelection employeeSelection = new EmployeeSelection();
                employeeSelection.Initialize(player, business);
            }
            else if(!result.getResult()){
                player.sendMessage(PayRollAPI.getLanguage("EmployeeTerminationAbortionMessage"));
                EmployeeSelection employeeSelection = new EmployeeSelection();
                employeeSelection.Initialize(player, business);
            }
        }
        );
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);

    }
}
