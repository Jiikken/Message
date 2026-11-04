# Message

Легковесная библиотека сообщений для Paper-плагинов. Упрощает работу с MiniMessage и JSON: загружает сообщения из `config.yml`, поддерживает плейсхолдеры и автоматически логирует проблемы.

## Установка

### JitPack

Добавь репозиторий:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Добавь зависимость:

```xml
<dependency>
    <groupId>com.github.Jiikken</groupId>
    <artifactId>Message</artifactId>
    <version>1.0.7</version>
</dependency>
```

## Использование

### 1. Инициализация

Создай экземпляр и загрузи сообщения из конфига:

```java
Message msg = new Message();
msg.load(getConfig(), List.of("pvpEnabled", "pvpDisabled", "noPermission"));
```
При добавлении нового сообщения в конфиг, в load() тоже нужно передать его.

Ключи передаются без префикса messages. — библиотека сама ищет их в секции messages.

### 2. Конфиг

```yml
messages:
  pvp:
    error:
      pvpAlreadyEnabled: "<red>PvP уже включён!"
    pvpEnabled: "<green>PvP включён</green>"
    pvpDisabled: "<gray>PvP <red><bold>выключен</bold></red></gray>"
  noPermission: "<red>У вас нет прав.</red>"
```

Поддерживаются как одиночные строки, так и списки (каждая строка отправится отдельным сообщением).

### 3. Отправка

```java
// Простое сообщение
msg.of("pvpEnabled").send(player);

// С плейсхолдерами
msg.of("pvpDisabled")
   .replace("%player%", player.getName())
   .replace("%color%", "red")
   .send(player);

// Смешанный путь
msg.of("pvp.pvpEnabled").send(player);
msg.of("pvp.error.pvpAlreadyEnabled").send(player);
```

### 4. JSON-сообщения

Если строка начинается с json:, она парсится через GsonComponentSerializer:
```yml
messages:
  raw: 'json:{"text":"Hello","color":"gold"}'
```

## Логирование

При загрузке библиотека выводит одно сообщение со всеми загруженными ключами:
```txt
[Message] Загрузка сообщений:
  ✔ pvpEnabled: [<green>PvP включён</green>]
  ✔ pvpDisabled: [<gray>PvP <red><bold>выключен</bold></red></gray>]
  ⚠ Отсутствует: messages.noPermission
Всего ключей: 2
```

Если вызвать msg.of("несуществующий"), в консоль уйдёт предупреждение с указанием, где искать ключ в config.yml.

## Требования

- Java 21+
- Paper 1.21+
