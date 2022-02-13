package com.azura4k.mcpe.Payroll;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.azura4k.mcpe.Payroll.Commands.JobOfferCmd;
import com.azura4k.mcpe.Payroll.Commands.ManageCmd;
import com.azura4k.mcpe.Payroll.Commands.PayToCmd;
import com.azura4k.mcpe.Payroll.Commands.TimesheetCmd;
import java.util.Timer;
import java.util.TimerTask;


public class PayrollBase extends PluginBase {

    //For Scheduling
    Timer timer = new Timer();
    Timer timer2 = new Timer();
    //For API
    PayRollAPI api;

    @Override
    public void onLoad() {
        super.onLoad();
        getLogger().info("Starting Up");
        SimpleCommandMap CM = this.getServer().getCommandMap();
        CM.register("/manage", new ManageCmd());
        CM.register("/timesheet", new TimesheetCmd());
        CM.register("/joboffer", new JobOfferCmd());
        CM.register("/payto", new PayToCmd());

    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        super.onEnable();
        this.getLogger().info(TextFormat.GREEN + "Payroll has Loaded");
        PayRollAPI.Initialize(this);
         api = new PayRollAPI();
        this.getServer().getPluginManager().registerEvents(new AutoClockOut(), this);

        //For every minute, check for the Wind Down

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                api.windDownTimer();
            }
        };
        timer.schedule(task, 0, getConfig().getLong("ForceClockOutInMilliseconds"));
    }
    @Override
    public void onDisable() {
        super.onDisable();
        timer.cancel();
        this.getLogger().critical(TextFormat.RED + "Payroll has been Disabled");
    }
}

class AutoClockOut implements Listener{
    PayRollAPI api = new PayRollAPI();
    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        api.ForceClockOut(event.getPlayer());
    }
}
