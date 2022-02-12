package com.azura4k.mcpe.Payroll.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;

import java.util.List;

public class TestCmd extends Command {
    public TestCmd() {
        super("test", "For development Purposes", "/test");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        PayRollAPI api = new PayRollAPI();
        Player player = commandSender.getServer().getPlayerExact(commandSender.getName());
        String Args = strings[0];
        String Args2 = strings[1];
        //api.CreateBusiness("testingBusiness", "Testing", player);

        //Business business = api.LoadBusiness("testingBusiness");
        //api.DeleteBusiness(business, player);
        //commandSender.getServer().getLogger().info(employee.Title);
        //commandSender.getServer().getLogger().info(employee.EmployerUID);
        //commandSender.getServer().getLogger().info(employee.player.getUniqueId().toString());

        //api.HireEmployee(business.UID, player, "Tester", 20, 5, 10);

        //Employee employee = api.LoadEmployee(business, "sonicking222");
        //api.FireEmployee(employee);
        //commandSender.getServer().getLogger().info(employee.EmployerUID);
        //commandSender.getServer().getLogger().info(employee.playerUUID.toString());
        //commandSender.getServer().getLogger().info(business.BusinessName);
        //commandSender.getServer().getLogger().info(employee.player.getName());
        //commandSender.getServer().getLogger().info(String.valueOf(business.Balance));
        Business business = api.LoadBusiness(Args);
        Employee employee = api.LoadEmployee(business, Args2);
        employee.Wage += 10;
        employee.SaveData();
        commandSender.getServer().getLogger().info(employee.playerUUID.toString());
        return true;
    }
}
