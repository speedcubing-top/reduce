package top.speedcubing.reduce.redis;

import com.google.gson.Gson;
import top.speedcubing.common.redis.RedisBus;
import top.speedcubing.reduce.Reduce;
import top.speedcubing.server.mulitproxy.BungeeProxy;
import top.speedcubing.server.redis.message.ReduceMapMessage;

public class RedisListener {
    public static void init() {
        RedisBus.subscribe(RedisBus.getChannelPrefix() + ":server:reduce:reducemap", rawMessage -> {
            ReduceMapMessage message = new Gson().fromJson(rawMessage, ReduceMapMessage.class);
            Reduce.mapCache.put(message.id(), message.map());
            BungeeProxy.switchServer(message.id(), "reduce");
        });
    }
}
