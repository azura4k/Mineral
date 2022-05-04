package com.azura4k.mcpe.Payroll.Listeners;

import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class AutoClockOut implements Listener {
    PayRollAPI api = new PayRollAPI();
    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        api.ForceClockOut(event.getPlayer());
    }
}
