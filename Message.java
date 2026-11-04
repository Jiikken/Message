package Jiken.privateTerritory;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Messages {
    private final Map<String, List<String>> messages = new HashMap<>();
    private final Map<String, Consumer<CommandSender>> senders = new HashMap<>();

    public void load(FileConfiguration c, Collection<String> keys) {
        messages.clear();
        for (String key : keys) {
            Object obj = c.get("messages." + key);
            if (obj == null) messages.put(key, List.of());
            else if (obj instanceof List<?> l) messages.put(key, List.copyOf((List<String>) l));
            else messages.put(key, List.of((String) obj));
        }
    }

    public Sender of(String key) { return new Sender(key); }

    public final class Sender {
        private final String key;
        private final Map<String, String> placeholders = new HashMap<>();
        private Sender(String key) { this.key = key; }

        public Sender replace(String from, String to) { placeholders.put(from, to); return this; }

        public void send(CommandSender sender) {
            for (String raw : messages.getOrDefault(key, List.of())) {
                String msg = raw;
                for (var e : placeholders.entrySet()) msg = msg.replace(e.getKey(), e.getValue());
                if (msg.startsWith("json:"))
                    sender.sendMessage(GsonComponentSerializer.gson().deserialize(msg.substring(5)));
                else
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(msg));
            }
        }
    }
}