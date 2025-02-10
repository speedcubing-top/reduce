package configuration;

import com.google.gson.JsonObject;
import top.speedcubing.common.configuration.ServerConfig;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.lib.eventbus.CubingEventHandler;
import top.speedcubing.reduce.Reduce;

public class Configuration {
    public static double[] ReduceKB;

    public static void reload() {
        ReduceKB = new double[]{
                ServerConfig.getConfig().getAsJsonObject("reducekb").get("hor").getAsDouble(),
                ServerConfig.getConfig().getAsJsonObject("reducekb").get("ver").getAsDouble(),
                ServerConfig.getConfig().getAsJsonObject("reducekb").get("airhor").getAsDouble(),
                ServerConfig.getConfig().getAsJsonObject("reducekb").get("airver").getAsDouble(),
                ServerConfig.getConfig().getAsJsonObject("reducekb").get("reach").getAsDouble()
        };
    }

    @CubingEventHandler(priority = 30)
    public void configReloadEvent(ConfigReloadEvent e) {
        reload();
    }
}
