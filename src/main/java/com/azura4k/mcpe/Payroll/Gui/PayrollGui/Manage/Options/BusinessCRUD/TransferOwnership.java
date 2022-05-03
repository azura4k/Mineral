package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD;

import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


import java.util.Objects;

public class TransferOwnership {
    CustomForm.Builder form = CustomForm.builder();
    PayRollAPI api = new PayRollAPI();
    public void Initialize(Player player, Business business){
        form.title(PayRollAPI.getLanguage("TransferOwnershipFormTitle"));
        if (Objects.equals(player, business.Owner)){
            form.input(PayRollAPI.getLanguage("TransferOwnershipTextBoxName"));
            form.responseHandler((form, responseData) -> {
                CustomFormResponse response = form.parseResponse(responseData);
                if (response.getInput(1) != null && response.getInput(1).length() > 0 ){
                    api.TransferOwner(player, business,response.getInput(1));
                }
            });

            FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);

        }
    }
}
