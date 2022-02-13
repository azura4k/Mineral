package com.azura4k.mcpe.Payroll.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.JobOffer.OfferMenu;

public class JobOfferCmd extends Command {
    public JobOfferCmd() {
        super("joboffer", "Open GUI to accept or reject jobs", "/joboffers");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        Player player = commandSender.getServer().getPlayerExact(commandSender.getName());
        OfferMenu form = new OfferMenu();
        form.initialize(player);
        return false;
    }
}
