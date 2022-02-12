package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.handler.SimpleFormHandler;

import java.util.Objects;

public class EmployeeSelectionOptions {

    SimpleForm form = new SimpleForm();

    Business Business;
    Employee Employee;

    //Handlers
    SimpleFormHandler manageHandler = (p, button) -> {
        EmployeeInfoPage form = new EmployeeInfoPage();
        form.initialize(p.getPlayer(),Business, Employee);
    };

    SimpleFormHandler fireHandler = (p, button) -> {
        FireEmployee form = new FireEmployee();
        form.initialize(p.getPlayer(), Business, Employee);
    };
    public void initialize (Player player, Business business, Employee employee){
        Business = business;
        Employee = employee;
        form.addButton("Manage", manageHandler);
        if (!(Objects.equals(employee.PlayerName, business.Owner.getName()))){
            form.addButton("Terminate", fireHandler);
        }
        form.send(player);
    }
}
