package com.jiikken.message;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class Message {
    private final Map<String, List<String>> messages = new HashMap<>();
    private final Logger logger = Logger.getLogger("Message");

    public void load(FileConfiguration c, Collection<String> keys) {
        messages.clear();
        logger.info("Загрузка сообщений...");
        for (String key : keys) {
            Object obj = c.get("messages." + key);
            if (obj == null) {
                messages.put(key, List.of());
                logger.warning("Ключ отсутствует в конфиге: messages." + key);
            } else if (obj instanceof List<?> l) {
                List<String> list = l.stream().map(String::valueOf).toList();
                messages.put(key, list);
                logger.info("Загружено '" + key + "': " + list);
            } else {
                List<String> list = List.of(String.valueOf(obj));
                messages.put(key, list);
                logger.info("Загружено '" + key + "': " + list);
            }
        }
        logger.info("Загрузка завершена. Всего ключей: " + messages.size());
    }

    public Sender of(String key) {
        if (!messages.containsKey(key)) {
            logger.warning("Вызван несуществующий ключ: '" + key + "'. Проверьте config.yml (messages." + key + ")");
        }
        return new Sender(key);
    }

    public final class Sender {
        private final String key;
        private final Map<String, String> placeholders = new HashMap<>();
        private Sender(String key) { this.key = key; }

        public Sender replace(String from, String to) { placeholders.put(from, to); return this; }

        public void send(CommandSender sender) {
            List<String> list = messages.getOrDefault(key, List.of());
            if (list.isEmpty()) {
                logger.warning("Попытка отправить пустое/несуществующее сообщение: '" + key + "'");
                return;
            }
            for (String raw : list) {
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