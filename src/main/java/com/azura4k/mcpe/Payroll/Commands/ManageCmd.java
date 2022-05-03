package com.azura4k.mcpe.Payroll.Commands;


import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.SelectMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManageCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player player = commandSender.getServer().getPlayerExact(commandSender.getName());
        SelectMenu form = new SelectMenu();
        form.initialize(player);
        return false;
    }
}
