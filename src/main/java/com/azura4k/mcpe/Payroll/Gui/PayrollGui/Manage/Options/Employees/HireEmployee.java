package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;


import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class HireEmployee {
    PayRollAPI api = new PayRollAPI();
    CustomForm.Builder form = CustomForm.builder();

    Business Business;

    public void initalize(Player player, Business business){
        Business = business;

        form.title((PayRollAPI.getLanguage("HireTitle")));

        form.label(PayRollAPI.getLanguage("HireeWarning"));
        form.input(PayRollAPI.getLanguage("HirePlayerName"));
        form.input(PayRollAPI.getLanguage("HirePlayerTitle"));
        form.input(PayRollAPI.getLanguage("HirePlayerMaxMinutes"));
        form.input(PayRollAPI.getLanguage("HirePlayerRank"));
        form.input(PayRollAPI.getLanguage("HirePlayerWage"));


        form.responseHandler( (form, responseData) -> {
            CustomFormResponse response = form.parseResponse(responseData);
            String PlayerName = response.getInput(1);
            String Title = response.getInput(2);
            double Max_Minutes = Double.parseDouble(response.getInput(3));
            int Rank = Integer.parseInt(response.getInput(4));
            double Wage = Double.parseDouble(response.getInput(5));
            if((PlayerName != null && PlayerName.length() > 0) && (Title != null && Title.length() > 0) && (Max_Minutes != 0) && (Rank >= Business.MinRank && Rank <= business.MaxRank) &&  (Wage >= PayRollAPI.PluginConfig.getDouble("MinimumWage"))){
                api.OfferPosition(Business.BusinessName, player.getServer().getPlayerExact(PlayerName), Title, Max_Minutes, Rank, Wage);
            }
            else
                player.sendMessage(PayRollAPI.getLanguage("HiringError"));
                Options options = new Options();
                options.initialize(player,Business);
        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }



}
