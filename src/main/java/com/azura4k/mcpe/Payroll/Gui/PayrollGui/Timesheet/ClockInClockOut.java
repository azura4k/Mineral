package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Timesheet;

import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class ClockInClockOut {

    Business Business;
    Employee Employee;


    SimpleForm.Builder form = SimpleForm.builder();
    PayRollAPI api = new PayRollAPI();


    public void initialize(Player player, Business business, Employee employee){
        Business = business;
        Employee = employee;

        form.title(business.BusinessName);
        form.content(PayRollAPI.getLanguage("CurrentPayMinutes") + employee.MinutesWorkedPerPayPeriod);
        form.button(PayRollAPI.getLanguage("ClockInButton"));
        form.button(PayRollAPI.getLanguage("ClouckOutButton"));
        form.button(PayRollAPI.getLanguage("PayoutOutButton"));
        form.responseHandler((form, responseData) -> {
            if (!form.isClosed(responseData)) {
                try {
                    SimpleFormResponse response = form.parseResponse(responseData);
                    if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("ClockInButton"))) {
                        api.RegisterOnClock(player, Employee);
                    } else if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("ClouckOutButton"))) {
                        api.RegisterOffClock(Employee);
                    } else if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("PayoutOutButton"))) {
                        api.PayEmployeeMinutelyRate(Employee);
                    }
                }catch (Exception ignored){
                    PayRollAPI.plugin.getLogger().warning(ignored.getMessage());
                }
            }
        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
        }
    }

