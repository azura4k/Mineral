package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Timesheet;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.handler.SimpleFormHandler;

import java.text.ParseException;

public class ClockInClockOut {

    Business Business;
    Employee Employee;
    SimpleForm form = new SimpleForm();
    PayRollAPI api = new PayRollAPI();

    //Handlers
    SimpleFormHandler ClockIn = (p, button) -> {
        api.RegisterOnClock(p.getPlayer(), Employee);
    };
    SimpleFormHandler ClockOut = (p, button) -> {
            api.RegisterOffClock(Employee);
    };
    SimpleFormHandler PayOut = (p, button) -> {
        api.PayEmployeeMinutelyRate(Employee);
    };
    public void initialize(Player player, Business business, Employee employee){
        Business = business;
        Employee = employee;

        form.setTitle(business.BusinessName);
        form.setContent(PayRollAPI.getLanguage("CurrentPayMinutes") + employee.MinutesWorkedPerPayPeriod);
        form.addButton(PayRollAPI.getLanguage("ClockInButton"), ClockIn);
        form.addButton(PayRollAPI.getLanguage("ClouckOutButton"), ClockOut);
        form.addButton(PayRollAPI.getLanguage("PayoutOutButton"), PayOut);
        form.send(player);
    }
}
