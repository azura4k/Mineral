package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.ModalForm;

public class FireEmployee {

    ModalForm form = new ModalForm(PayRollAPI.getLanguage("EmployeeTerminationModalTitle"));
    PayRollAPI api = new PayRollAPI();


    public void initialize (Player player, Business business, Employee employee){
        form.addContent(PayRollAPI.getLanguage("EmployeeTerminationModalContentPt1") + employee.PlayerName + PayRollAPI.getLanguage("EmployeeTerminationModalContentPt2") + employee.EmployerName);
        form.setPositiveButton(PayRollAPI.getLanguage("EmployeeTerminationModalYesButton"));
        form.setNegativeButton(PayRollAPI.getLanguage("EmployeeTerminationModalNoButton"));
        form.setHandler( (p, result) ->{
            if (result){
                api.FireEmployee(employee);
                EmployeeSelection employeeSelection = new EmployeeSelection();
                employeeSelection.Initialize(p, business);
            }
            else if(!result){
                p.sendMessage(PayRollAPI.getLanguage("EmployeeTerminationAbortionMessage"));
                EmployeeSelection employeeSelection = new EmployeeSelection();
                employeeSelection.Initialize(p, business);
            }
        }
        );
        form.send(player);

    }
}
