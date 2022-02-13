package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Input;

public class HireEmployee {
    PayRollAPI api = new PayRollAPI();
    CustomForm form = new CustomForm(PayRollAPI.getLanguage("HireTitle"));
    Business Business;

    public void initalize(Player player, Business business){
        Business = business;


        form.addElement("PlayerName", Input.builder().setName(PayRollAPI.getLanguage("HirePlayerName")).build());
        form.addElement("Title", Input.builder().setName(PayRollAPI.getLanguage("HirePlayerTitle")).build());
        form.addElement("MaxMinutes", Input.builder().setName(PayRollAPI.getLanguage("HirePlayerMaxMinutes")).build());
        form.addElement("Rank", Input.builder().setName(PayRollAPI.getLanguage("HirePlayerRank")).build());
        form.addElement("Wage", Input.builder().setName(PayRollAPI.getLanguage("HirePlayerWage")).build());


        form.setHandler( (p, response) -> {
            String PlayerName = response.getInput("PlayerName").getValue();
            String Title = response.getInput("Title").getValue();
            double Max_Minutes = Double.parseDouble(response.getInput("MaxMinutes").getValue());
            int Rank = Integer.parseInt(response.getInput("Rank").getValue());
            double Wage = Double.parseDouble(response.getInput("Wage").getValue());


            if((PlayerName != null && PlayerName.length() > 0) && (Title != null && Title.length() > 0) && (Max_Minutes != 0) && (Rank >= Business.MinRank && Rank <= business.MaxRank) &&  (Wage >= PayRollAPI.PluginConfig.getDouble("MinimumWage"))){
                api.OfferPosition(Business.BusinessName, p.getPlayer().getServer().getPlayerExact(PlayerName), Title, Max_Minutes, Rank, Wage);
            }
            else
                p.sendMessage(PayRollAPI.getLanguage("HiringError"));
                Options options = new Options();
                options.initialize(p,Business);
        });

        form.send(player);
    }



}
