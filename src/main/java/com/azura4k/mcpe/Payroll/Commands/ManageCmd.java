package com.azura4k.mcpe.Payroll.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.SelectMenu;

public class ManageCmd extends Command {
    public ManageCmd() {
        super("manage", "Open GUI to create, delete, or manage a business.", "/Message");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        Player player = commandSender.getServer().getPlayerExact(commandSender.getName());
        commandSender.getServer().getLogger().info("CommandSuccessful");
        SelectMenu form = new SelectMenu();
        form.initialize(player);



        return false;
    }
}
