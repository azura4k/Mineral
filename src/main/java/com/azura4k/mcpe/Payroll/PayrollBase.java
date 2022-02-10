package com.azura4k.mcpe.Payroll;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.azura4k.mcpe.Payroll.Commands.ManageCmd;
import com.azura4k.mcpe.Payroll.Commands.TestCmd;
import com.azura4k.mcpe.Payroll.Commands.TimesheetCmd;


public class PayrollBase extends PluginBase {
    @Override
    public void onLoad() {
        super.onLoad();
        getLogger().info("Starting Up");
        SimpleCommandMap CM = this.getServer().getCommandMap();
        CM.register("/manage", new ManageCmd());
        CM.register("/timesheet", new TimesheetCmd());
        CM.register("/test", new TestCmd());


    }
    @Override
    public void onEnable() {
        saveDefaultConfig();
        super.onEnable();
        this.getLogger().info(TextFormat.GREEN + "Payroll has Loaded");
        PayRollAPI.PayRollAPI(this);


    }
    @Override
    public void onDisable() {
        super.onDisable();

        this.getLogger().critical(TextFormat.RED + "Payroll has been Disabled");
    }
}
