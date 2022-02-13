package com.azura4k.mcpe.Payroll.Gui.PayrollGui.JobOffer;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.ModalForm;

public class AcceptOrDenyJob {
    ModalForm form = new ModalForm(PayRollAPI.getLanguage("JobChoiceMenuTitle"));
    PayRollAPI api = new PayRollAPI();

    public void initiate(Player player, String businessName){
        Employee positionDetails = api.LoadPositionDetails(player, businessName);


        form.setPositiveButton(PayRollAPI.getLanguage("JobChoiceMenuYesButton"));
        form.setNegativeButton(PayRollAPI.getLanguage("JobChoiceMenuNoButton"));

        form.setContent(PayRollAPI.getLanguage("JobChoiceContextBusinessName") + positionDetails.EmployerName + "\n"
                + PayRollAPI.getLanguage("JobChoiceContextWage") + positionDetails.Wage + "\n" +
                PayRollAPI.getLanguage("JobChoiceContextTitle") + positionDetails.Title + "\n" +
                PayRollAPI.getLanguage("JobChoiceContextRank") + positionDetails.Rank + "\n" +
                PayRollAPI.getLanguage("JobChoiceContextMaxMinutes") + positionDetails.MaximumMinutes
        );

        form.setHandler( (p, result) -> {
            if (result){
                api.AcceptPosition(player, businessName);
            }else if (!result){
                api.DenyPosition(player, businessName);
            }

        });
        form.send(player);
    }
}
