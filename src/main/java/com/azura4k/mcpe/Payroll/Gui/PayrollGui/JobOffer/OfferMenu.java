package com.azura4k.mcpe.Payroll.Gui.PayrollGui.JobOffer;

import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;


import java.util.ArrayList;


public class OfferMenu {

    SimpleForm.Builder form = SimpleForm.builder();
    PayRollAPI api = new PayRollAPI();

    public void initialize(Player player){

        form.title(PayRollAPI.getLanguage("JobOfferMenuTitle"));

        ArrayList<String> JobOffers = api.getAllJobOffers(player);

        for (int i = 0; i < JobOffers.size(); i++){
            final String businessName = JobOffers.get(i);
            form.button(businessName);
        }
        form.responseHandler((form, responseData)->{
            SimpleFormResponse response = form.parseResponse(responseData);
            AcceptOrDenyJob choice = new AcceptOrDenyJob();
            if (response.isCorrect()) {
                choice.initiate(player, response.getClickedButton().getText());
            }
        });
    }
}
