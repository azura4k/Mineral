package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD;


import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.SelectMenu;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.ModalForm;
import org.geysermc.cumulus.response.ModalFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class DeleteBusiness {
    PayRollAPI api = new PayRollAPI();
    ModalForm.Builder form = ModalForm.builder();


    public void initialize(Player player, Business business){
        form.title(PayRollAPI.getLanguage("DeleteBusinessFormTitle"));
        form.content(PayRollAPI.getLanguage("DeleteBusinessFormConfirmationText") + business.BusinessName);
        form.button1(PayRollAPI.getLanguage("DeleteBusinessFormYesButton"));
        form.button2(PayRollAPI.getLanguage("DeleteBusinessFormNoButton"));

        form.responseHandler((form, result) -> {
            ModalFormResponse response = form.parseResponse(result);
            if (response.getResult()){
                api.DeleteBusiness(business, player);
                SelectMenu selectMenu = new SelectMenu();
                selectMenu.initialize(player);
            }
            else {
                player.sendMessage(PayRollAPI.getLanguage("DeleteBusinessFormAverted"));
                Options menu = new Options();
                menu.initialize(player, business);
            }});
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }
}
