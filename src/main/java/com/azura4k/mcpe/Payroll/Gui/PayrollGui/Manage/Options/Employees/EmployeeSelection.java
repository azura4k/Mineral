package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.Form;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.handler.SimpleFormHandler;

import java.util.Objects;

public class EmployeeSelection {

    PayRollAPI api = new PayRollAPI();

    Business Business;
    Employee employee;


    SimpleFormHandler EmployeeClick = (p, button) -> {
        EmployeeSelectionOptions form = new EmployeeSelectionOptions();
        employee = api.LoadEmployee(Business, button.getName());
        form.initialize(p, Business, employee);
    };
    SimpleFormHandler SearchClick = (p, button) -> {
        EmployeeSelectionByName form = new EmployeeSelectionByName();
        form.initalize(p, Business);
    };
    SimpleFormHandler BackButton = (player, button) -> {
        Options form = new Options();
        form.initialize(player, Business);
    };

    public void Initialize(Player player, Business business){
        SimpleForm form = new SimpleForm();
        business.reload();
        Business = business;

        form.addButton(PayRollAPI.getLanguage("EmployeeSelectionFormSearchButton"), SearchClick);
        for (int i = 0; i < business.Employees.size() ; i++) {
                String Name = business.Employees.get(i).PlayerName;
                if (Objects.equals(Name, Business.Owner.getName()) && !Objects.equals(player, business.Owner)) {

                }
                else {
                    form.addButton(Name, EmployeeClick);
                }

        }
        form.addButton(PayRollAPI.getLanguage("BackButton"), BackButton);
        form.send(player);
    }
}
