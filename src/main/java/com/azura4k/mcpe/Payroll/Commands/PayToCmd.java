package com.azura4k.mcpe.Payroll.Commands;


import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.PayRollAPI;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;

public class PayToCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        PayRollAPI api = new PayRollAPI();
        CustomForm.Builder form = CustomForm.builder();

        form.title(PayRollAPI.getLanguage("PayToFormTitle"));
        form.input(PayRollAPI.getLanguage("PayToFormBusiness"));
        form.input(PayRollAPI.getLanguage("PayToFormAmount"));
        form.input(PayRollAPI.getLanguage("PayToFormReason"));
        form.input(PayRollAPI.getLanguage("PayToFormEmployee"));
        form.toggle(PayRollAPI.getLanguage("PayToFormAskforRecipt"), true);

        Player player = PayRollAPI.plugin.getServer().getPlayerExact(commandSender.getName());
        OfflinePlayer player1 = PayRollAPI.plugin.getServer().getOfflinePlayer(player.getUniqueId());

        form.responseHandler((Form, responseData)->{
            if (!Form.isClosed(responseData)) {
                try {
                    CustomFormResponse response = Form.parseResponse(responseData);
                    String BusinessName = response.getInput(0);
                    Double Amount = Double.valueOf(response.getInput(1));
                    String Reason = response.getInput(2);
                    String Employee = response.getInput(3);
                    boolean GiveReceipt = response.getToggle(4);


                    if (!BusinessName.isEmpty() && Amount > 0) {

                        Business business = api.LoadBusiness(BusinessName);
                        if (business == null) {
                            player.sendMessage(PayRollAPI.getLanguage("BusinessNotFound"));
                        } else if (PayRollAPI.getEconomy().getBalance(player1) >= Amount) {
                            business.Balance += Amount;
                            player.sendMessage(PayRollAPI.getLanguage("PayedToBusiness") + Amount + PayRollAPI.getLanguage("PayedToBusinessPt2") + BusinessName);
                            PayRollAPI.getEconomy().withdrawPlayer(player1, Amount);
                            business.SaveData();
                            if (GiveReceipt) {
                                PayRollAPI.WriteReceipt(business, player, Amount, Reason, Employee);
                            }

                        } else if (PayRollAPI.getEconomy().getBalance(player1) < Amount) {
                            player.sendMessage(PayRollAPI.getLanguage("OverDraftRisk"));
                        }

                    } else {
                        player.sendMessage(PayRollAPI.getLanguage("BusinessNotFound"));
                    }
                }catch (Exception ignored){
                    PayRollAPI.plugin.getLogger().warning(ignored.getMessage());
                }
            }
        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
        return false;
    }
}
