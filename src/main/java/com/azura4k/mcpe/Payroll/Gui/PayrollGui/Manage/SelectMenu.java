package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage;

import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD.CreateBusiness;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


import java.util.ArrayList;


public class SelectMenu {

    SimpleForm.Builder form = SimpleForm.builder();
    PayRollAPI api = new PayRollAPI();

    public void initialize(Player player){
        form.title(PayRollAPI.getLanguage("SelectFormTitle"));

        ArrayList<String> Employers = api.getBusinessesEmployedAt(player);

        for (int i = 0; i < Employers.size(); i++){
            final String businessName = api.LoadBusiness(Employers.get(i)).BusinessName;
            form.button(businessName);
        }

        form.button(PayRollAPI.getLanguage("CreateBusinessButton"));
        form.responseHandler((form, responseData) -> {
            SimpleFormResponse response = form.parseResponse(responseData);
            if (response.getClickedButton().getText().equals(PayRollAPI.getLanguage("CreateBusinessButton"))){
                CreateBusiness CreateBusiness = new CreateBusiness();
                CreateBusiness.initialize(player);
            }
            else if (!response.getClickedButton().getText().equals(PayRollAPI.getLanguage("CreateBusinessButton"))){
                Options options = new Options();
                options.initialize(player, api.LoadBusiness(response.getClickedButton().getText()));
            }
        });


        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }




}
