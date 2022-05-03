package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Timesheet;

import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


import java.util.ArrayList;


public class SelectJob {

    SimpleForm.Builder form = SimpleForm.builder();
    PayRollAPI api = new PayRollAPI();

    public void initialize(Player player){
        form.title(PayRollAPI.getLanguage("ClockInJobSelectFormTitle"));

        ArrayList<String> Employers = api.getBusinessesEmployedAt(player);

        for (int i = 0; i < Employers.size(); i++){
            final String businessName = api.LoadBusiness(Employers.get(i)).BusinessName;
            form.button(businessName);
        }

        form.responseHandler((form, reponseData) -> {
            SimpleFormResponse response = form.parseResponse(reponseData);
            if (response.isCorrect()){
                ClockInClockOut clockForm = new ClockInClockOut();
                clockForm.initialize(player, api.LoadBusiness(response.getClickedButton().getText()), api.LoadEmployee(api.LoadBusiness(response.getClickedButton().getText()), player.getName()));
            }
        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }

}
