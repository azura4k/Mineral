package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.ModalForm;

public class FireEmployee {

    ModalForm form = new ModalForm("Termination");
    PayRollAPI api = new PayRollAPI();


    public void initialize (Player player, Business business, Employee employee){
        form.addContent("Are you sure you want to terminate employment of " + employee.PlayerName + " with " + employee.EmployerName);
        form.setPositiveButton("Yes");
        form.setNegativeButton("No");
        form.setHandler( (p, result) ->{
            if (result){
                api.FireEmployee(employee);
                EmployeeSelection employeeSelection = new EmployeeSelection();
                employeeSelection.Initialize(p, business);
            }
            else if(!result){
                p.sendMessage("Aborted Termination of Player");
                EmployeeSelection employeeSelection = new EmployeeSelection();
                employeeSelection.Initialize(p, business);
            }
        }
        );
        form.send(player);

    }
}
