package com.azura4k.mcpe.Payroll.Gui.PayrollGui.JobOffer;
import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.handler.SimpleFormHandler;

import java.util.ArrayList;


public class OfferMenu {

    SimpleForm form = new SimpleForm("Testing Forms");
    PayRollAPI api = new PayRollAPI();

    public void initialize(Player player){
        form.setTitle(PayRollAPI.getLanguage("JobOfferMenuTitle"));

        ArrayList<String> JobOffers = api.getAllJobOffers(player);

        for (int i = 0; i < JobOffers.size(); i++){
            final String businessName = JobOffers.get(i);
            form.addButton(businessName, Handler);
        }
        form.send(player);
    }

    SimpleFormHandler Handler = (p, button) ->{
        AcceptOrDenyJob choice = new AcceptOrDenyJob();
        choice.initiate(p, button.getName());
    };



}
