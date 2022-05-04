package com.azura4k.mcpe.Payroll.Commands;


import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.PayRollAPI;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;


import java.util.ArrayList;
import java.util.List;

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
            CustomFormResponse response = Form.parseResponse(responseData);


            String BusinessName = response.getInput(0);
            Double Amount = Double.valueOf(response.getInput(1));
            String Employee = response.getInput(2);
            String Reason = response.getInput(3);
            boolean GiveReceipt = response.getToggle(0);


            if (!BusinessName.isEmpty() && Amount > 0){

                Business business = api.LoadBusiness(BusinessName);
                if (business == null){
                    player.sendMessage(PayRollAPI.getLanguage("BusinessNotFound"));
                }
                else if (PayRollAPI.getEconomy().getBalance(player1) >= Amount) {
                    business.Balance += Amount;
                    player.sendMessage(PayRollAPI.getLanguage("PayedToBusiness") + Amount + PayRollAPI.getLanguage("PayedToBusinessPt2") + BusinessName);
                    PayRollAPI.getEconomy().withdrawPlayer(player1, Amount);
                    business.SaveData();
                    if (GiveReceipt) {
                        //COPIED FROM INFOBOOKS PLUGIN CODE BY PetteriM1
                        if (Reason == null){
                            Reason = PayRollAPI.getLanguage("ReasonNotProvided");
                        }
                        if (Employee == null){
                            Employee = PayRollAPI.getLanguage("EmployeeNameNotProvided");
                        }


                        Material itemType = Material.matchMaterial("WRITTEN_BOOK");
                        ItemStack writtenBook = new ItemStack(itemType);

                        BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
                        bookMeta.setTitle(BusinessName + " " + PayRollAPI.getDate());
                        bookMeta.setAuthor(business.BusinessName);

                        List<String> pages = new ArrayList<String>();
                        pages.add(PayRollAPI.getLanguage("ReceiptBusinessHeader") + BusinessName); // Page 1
                        pages.add(PayRollAPI.getLanguage("ReceiptAmountHeader") + Amount); // Page 2
                        pages.add(PayRollAPI.getLanguage("ReceiptEmployeeHeader") + Employee); // Page 3
                        pages.add(PayRollAPI.getLanguage("ReceiptReasonHeader") + Reason);//Page 4
                        bookMeta.setPages(pages);
                        writtenBook.setItemMeta(bookMeta);

                        player.getInventory().addItem(writtenBook);
                        PayRollAPI.plugin.getServer().getPlayerExact(Employee).getInventory().addItem(writtenBook);
                    }

                }
                else if (PayRollAPI.getEconomy().getBalance(player1) < Amount){
                    player.sendMessage(PayRollAPI.getLanguage("OverDraftRisk"));
                }

            }
            else {
                player.sendMessage(PayRollAPI.getLanguage("BusinessNotFound"));
            }

        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
        return false;
    }
}
