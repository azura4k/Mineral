package com.azura4k.mcpe.Payroll.Gui.PayrollGui.JobOffer;


import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.ModalForm;
import org.geysermc.cumulus.response.ModalFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class AcceptOrDenyJob {
    ModalForm.Builder form = ModalForm.builder();

    PayRollAPI api = new PayRollAPI();

    public void initiate(Player player, String businessName){
        Employee positionDetails = api.LoadPositionDetails(player, businessName);
        form.title(PayRollAPI.getLanguage("JobChoiceMenuTitle"));
        form.button1(PayRollAPI.getLanguage("JobChoiceMenuYesButton"));
        form.button2(PayRollAPI.getLanguage("JobChoiceMenuNoButton"));

        form.content(
                PayRollAPI.getLanguage("JobChoiceContextBusinessName") + positionDetails.EmployerName + "\n" +
                PayRollAPI.getLanguage("JobChoiceContextWage") + positionDetails.Wage + "\n" +
                PayRollAPI.getLanguage("JobChoiceContextTitle") + positionDetails.Title + "\n" +
                PayRollAPI.getLanguage("JobChoiceContextRank") + positionDetails.Rank + "\n" +
                PayRollAPI.getLanguage("JobChoiceContextMaxMinutes") + positionDetails.MaximumMinutes
        );

        form.responseHandler( (form, result) -> {
            ModalFormResponse response = form.parseResponse(result);
            if (response.getResult()){
                api.AcceptPosition(player, businessName);
            }else if (!response.getResult()){
                api.DenyPosition(player, businessName);
            }

        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }
}
