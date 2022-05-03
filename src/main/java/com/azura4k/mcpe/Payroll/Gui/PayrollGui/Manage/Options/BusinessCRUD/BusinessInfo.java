package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD;

import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


import java.util.Objects;

public class BusinessInfo {

    public void initialize(Player player, Business business){
        CustomForm.Builder Form =  CustomForm.builder();
        PayRollAPI api = new PayRollAPI();
        Employee employee = api.LoadEmployee(business, player.getName());

        Form.title(PayRollAPI.getLanguage("ManageBusinessInfoFormTitle"));
        //Checks if Player is Owner
        if (Objects.equals(employee.playerUUID.toString(), business.Owner.getUniqueId().toString())){
            Form.input(PayRollAPI.getLanguage("ManageBusinessInfoFormNameBelowOwner"), business.BusinessName);
            Form.input(PayRollAPI.getLanguage("ManageBusinessInfoFormDesc"),business.BusinessDesc);
            Form.input(PayRollAPI.getLanguage("ManageBusinessInfoFormMaxRank"), String.valueOf(business.MaxRank));
            Form.input(PayRollAPI.getLanguage("ManageBusinessInfoFormMin"),String.valueOf(business.MinRank));
            Form.input(PayRollAPI.getLanguage("ManageBusinessInfoFormTrustedRank"),String.valueOf(business.TrustedRank));
            Form.label(PayRollAPI.getLanguage(PayRollAPI.getLanguage("ManageBusinessInfoFormBalance") + business.Balance));
        }
        else if (employee.Rank >= business.TrustedRank && !Objects.equals(employee.playerUUID.toString(), business.Owner.getUniqueId().toString())){
            Form.label(PayRollAPI.getLanguage("ManageBusinessInfoFormNameBelowOwner") + business.BusinessName);
            Form.input(PayRollAPI.getLanguage("ManageBusinessInfoFormDesc"),business.BusinessDesc);
            Form.input(PayRollAPI.getLanguage("ManageBusinessInfoFormMaxRank"), String.valueOf(business.MaxRank));
            Form.input(PayRollAPI.getLanguage("ManageBusinessInfoFormMin"),String.valueOf(business.MinRank));
            Form.label(PayRollAPI.getLanguage("ManageBusinessInfoFormTrustedRank") + business.TrustedRank);
        }
        else if (employee.Rank < business.TrustedRank) {Form.label(PayRollAPI.getLanguage("ManageBusinessInfoFormNameBelowOwner") + business.BusinessName);
            Form.label(PayRollAPI.getLanguage("ManageBusinessInfoFormDesc") + business.BusinessDesc);
            Form.label(PayRollAPI.getLanguage("ManageBusinessInfoFormMaxRank") + business.MaxRank);
            Form.label(PayRollAPI.getLanguage("ManageBusinessInfoFormDesc") + business.MinRank);
        }

        //Changes Handler.
        Form.responseHandler((form, responseData) -> {
            CustomFormResponse response = form.parseResponse(responseData);
            //Check for null

            if (Objects.equals(employee.playerUUID.toString(), business.Owner.getUniqueId().toString())){

                if (!(response.getInput(1) == null) && response.getInput(1).length() > 0){
                    business.BusinessDesc = response.getInput(1);
                }

                if (!(response.getInput(2) == null) && response.getInput(2).length() > 0){
                    business.BusinessDesc = response.getInput(2);
                }

                if (!(response.getInput(3) == null) && response.getInput(3).length() > 0){
                    business.MaxRank = Integer.parseInt(response.getInput(3));
                }
                if (!(response.getInput(4) == null) && response.getInput(4).length() > 0 ){
                    business.MinRank = Integer.parseInt(response.getInput(4));
                }
                if (!(response.getInput(5) == null) && response.getInput(5).length() > 0){
                    business.TrustedRank = Integer.parseInt(response.getInput(5));
                }
            }
            else if (employee.Rank >= business.TrustedRank && !Objects.equals(employee.playerUUID.toString(), business.Owner.getUniqueId().toString())){
                if (!(response.getInput(1) == null) && response.getInput(1).length() > 0){
                    business.BusinessDesc = response.getInput(1);
                }
                if (!(response.getInput(2) == null) && response.getInput(2).length() > 0){
                    business.MaxRank = Integer.parseInt(response.getInput(2));
                }
                if (!(response.getInput(3) == null) && response.getInput(3).length() > 0 ){
                    business.MinRank = Integer.parseInt(response.getInput(3));
                }
            }

            business.SaveData();
            player.getPlayer().sendMessage(PayRollAPI.getLanguage("SuccessfullyUpdated"));

            Options options = new Options();
            options.initialize(player, business);
        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), Form);
    }
}


