package com.azura4k.mcpe.Payroll.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TimesheetCmd extends Command {
    public TimesheetCmd() {
        super("timesheet", "Open GUI to clock in, clock out of work.", "/timesheet <business>");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        Player player = commandSender.getServer().getPlayerExact(commandSender.getName());
        commandSender.getServer().getLogger().info("Command too Successful");




        return false;
    }
}
