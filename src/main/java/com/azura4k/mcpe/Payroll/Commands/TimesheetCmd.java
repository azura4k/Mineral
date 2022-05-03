package com.azura4k.mcpe.Payroll.Commands;

import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Timesheet.SelectJob;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimesheetCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = commandSender.getServer().getPlayerExact(commandSender.getName());
        SelectJob form = new SelectJob();
        form.initialize(player);
        return false;
    }
}
