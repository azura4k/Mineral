package com.azura4k.mcpe.Payroll.Commands;

import com.azura4k.mcpe.Payroll.Gui.PayrollGui.JobOffer.OfferMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JobOfferCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = commandSender.getServer().getPlayerExact(commandSender.getName());
        OfferMenu form = new OfferMenu();
        form.initialize(player);
        return false;
    }
}
