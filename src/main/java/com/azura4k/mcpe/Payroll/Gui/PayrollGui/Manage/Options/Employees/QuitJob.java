package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.ModalForm;
import org.geysermc.cumulus.response.ModalFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class QuitJob {

    PayRollAPI api = new PayRollAPI();


    ModalForm.Builder QuitJob = ModalForm.builder();
    public void Initialize(Player player, Business business, Employee employee){

        QuitJob.content(PayRollAPI.getLanguage("QuitPositionFormContent") + employee.EmployerName);
        QuitJob.button1(PayRollAPI.getLanguage("QuitPositionFormYes"));
        QuitJob.button2((PayRollAPI.getLanguage("QuitPositionFormNo")));

        QuitJob.responseHandler((form, result) -> {
            if (!form.isClosed(result)) {
                try {
                    ModalFormResponse response = form.parseResponse(result);
                    if (response.getResult()) {
                        if (PayRollAPI.PluginConfig.getBoolean("PayOutOnQuit")){
                        api.PayEmployeeMinutelyRate(employee);
                        }
                        api.FireEmployee(employee);
                    } else if (!response.getResult()) {
                        Options options = new Options();
                        options.initialize(player, business);
                    }
                }catch (Exception ignored){
                    PayRollAPI.plugin.getLogger().warning(ignored.getMessage());
                }
            }
        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), QuitJob);
    }
}
