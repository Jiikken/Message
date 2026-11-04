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

## MiniMessage

### Цвета

| Тег | Описание |
|---|---|
| `<black>`, `<dark_blue>`, `<dark_green>`, `<dark_aqua>`, `<dark_red>`, `<dark_purple>`, `<gold>`, `<gray>`, `<dark_gray>`, `<blue>`, `<green>`, `<aqua>`, `<red>`, `<light_purple>`, `<yellow>`, `<white>` | Именованные цвета Minecraft |
| `<#RRGGBB>` | HEX-цвет (например, `<#ff0000>`) |
| `<color:red>` / `<c:red>` | Альтернативный синтаксис цвета |
| `<shadow:#RRGGBB>` | Цвет тени текста |

### Форматирование (декорации)

| Тег | Алиасы | Описание |
|---|---|---|
| `<bold>/</bold>` | `<b>/</b>` | **Жирный** |
| `<italic>` | `<i>`, `<em>` | *Курсив* |
| `<underlined>` | `<u>` | Подчёркнутый |
| `<strikethrough>` | `<st>` | ~~Зачёркнутый~~ |
| `<obfuscated>` | `<obf>` | «Магический» текст |
| `<reset>` | `<r>` | Сброс всех стилей |

### Градиенты и эффекты

| Тег | Описание |
|---|---|
| `<gradient:red:blue>` | Плавный переход между цветами |
| `<rainbow>` | Радужный текст |
| `<transition:red:blue:0.5>` | Однотонный цвет из градиента по фазе |

### События (интерактивность)

| Тег | Описание |
|---|---|
| `<click:run_command:/help>` | Выполнить команду при клике |
| `<click:suggest_command:/msg >` | Подставить команду в чат |
| `<click:open_url:'https://...'>` | Открыть ссылку |
| `<click:copy_to_clipboard:'text'>` | Скопировать текст |
| `<hover:show_text:'подсказка'>` | Всплывающая подсказка |
| `<insert:текст>` | Вставка текста по Shift+клик |

### Компоненты

| Тег | Описание |
|---|---|
| `<key:key.jump>` | Отображение привязки клавиши |
| `<lang:block.minecraft.diamond_block>` | Переведённое название |
| `<score:Player:kills>` | Значение из таблицы очков |
| `<selector:@s>` | Селектор сущностей |
| `<head:Notch>` | Голова игрока |

### Служебные

| Тег | Описание |
|---|---|
| `<newline>` / `<br>` | Перенос строки |
| `\<` | Экранирование символа `<` |
| `<!bold>` | Отключить жирный (не сбрасывая остальное) |

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
