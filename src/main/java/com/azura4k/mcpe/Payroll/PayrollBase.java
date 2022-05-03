package com.azura4k.mcpe.Payroll;

import com.azura4k.mcpe.Payroll.Commands.JobOfferCmd;
import com.azura4k.mcpe.Payroll.Commands.ManageCmd;
import com.azura4k.mcpe.Payroll.Commands.PayToCmd;
import com.azura4k.mcpe.Payroll.Commands.TimesheetCmd;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Timer;
import java.util.TimerTask;


public class PayrollBase extends JavaPlugin {

    //For Scheduling
    Timer timer = new Timer();
    Timer timer2 = new Timer();
    //For API
    PayRollAPI api;

    private static Economy econ = null;
    public static Economy getEconomy() {
        return econ;
    }


    @Override
    public void onLoad() {
        super.onLoad();
        getLogger().info("Starting Up");



        this.getCommand("manage").setExecutor(new ManageCmd());
        this.getCommand("timesheet").setExecutor(new TimesheetCmd());
        this.getCommand("joboffer").setExecutor(new JobOfferCmd());
        this.getCommand("payto").setExecutor(new PayToCmd());

    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        super.onEnable();
        this.getLogger().info("Payroll has Loaded");
        PayRollAPI.Initialize(this);
         api = new PayRollAPI();
        this.getServer().getPluginManager().registerEvents(new AutoClockOut(), this);
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();



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
        this.getLogger().warning("Payroll has been Disabled");
    }
}

class AutoClockOut implements Listener {
    PayRollAPI api = new PayRollAPI();
    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        api.ForceClockOut(event.getPlayer());
    }
}
