package com.azura4k.mcpe.Payroll;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.azura4k.mcpe.Payroll.Commands.JobOfferCmd;
import com.azura4k.mcpe.Payroll.Commands.ManageCmd;
import com.azura4k.mcpe.Payroll.Commands.TestCmd;
import com.azura4k.mcpe.Payroll.Commands.TimesheetCmd;
import lombok.SneakyThrows;

import java.util.Timer;
import java.util.TimerTask;


public class PayrollBase extends PluginBase {
        Timer timer = new Timer();
    @Override
    public void onLoad() {
        super.onLoad();
        getLogger().info("Starting Up");
        SimpleCommandMap CM = this.getServer().getCommandMap();
        CM.register("/manage", new ManageCmd());
        CM.register("/timesheet", new TimesheetCmd());
        CM.register("/test", new TestCmd());
        CM.register("/joboffer", new JobOfferCmd());


    }
    @Override
    public void onEnable() {
        saveDefaultConfig();
        super.onEnable();
        this.getLogger().info(TextFormat.GREEN + "Payroll has Loaded");
        PayRollAPI.Initialize(this);
        PayRollAPI api = new PayRollAPI();

        timer.schedule( new TimerTask() {
            @SneakyThrows
            public void run() {
                api.windDownTimer();
            }
        }, 0, 60*1000);


    }
    @Override
    public void onDisable() {
        super.onDisable();
        timer.cancel();
        this.getLogger().critical(TextFormat.RED + "Payroll has been Disabled");
    }
}
