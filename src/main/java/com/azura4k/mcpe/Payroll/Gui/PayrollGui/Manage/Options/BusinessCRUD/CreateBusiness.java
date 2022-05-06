package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD;


import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.SelectMenu;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import com.azura4k.mcpe.Payroll.PayrollBase;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


public class CreateBusiness {

    CustomForm.Builder form = CustomForm.builder();
    Double StartupCost = PayRollAPI.PluginConfig.getDouble("IntialStartupCost");
    PayRollAPI api = new PayRollAPI();

    public void initialize(Player player) {
        form.title(PayRollAPI.getLanguage("CreateBusinessFormTitle"));
        form.input(PayRollAPI.getLanguage("CreateBusinessFormNameLabel"));
        form.input(PayRollAPI.getLanguage("CreateBusinessFormDescriptionLabel"));
        form.label(PayRollAPI.getLanguage("CreateBusinessFormStartupCost") + StartupCost);
        form.responseHandler((form, reponseData)->{
            CustomFormResponse response = form.parseResponse(reponseData);
            if (!form.isClosed(reponseData)) {
                try {
                OfflinePlayer player2 = PayRollAPI.plugin.getServer().getOfflinePlayer(player.getUniqueId());
                if (PayRollAPI.getEconomy().getBalance(player2) >= StartupCost) {
                    PayRollAPI.getEconomy().withdrawPlayer(player2, StartupCost);
                    if (api.CreateBusiness(response.getInput(0), response.getInput(1), player)) {
                        SelectMenu customForm = new SelectMenu();
                        customForm.initialize(player);
                    } else {
                        player.sendMessage(PayRollAPI.getLanguage("BusinessNameAlrRegistered"));
                    }
                } else {
                    player.sendMessage(PayRollAPI.getLanguage("OverDraftRisk d"));
                }
            }
            catch (Exception ignored){
                PayRollAPI.plugin.getLogger().warning(ignored.getMessage());
            }
            }
        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);

    }





}

