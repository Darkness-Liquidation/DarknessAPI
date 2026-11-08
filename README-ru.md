<div align="center">
  <h1>DarknessAPI</h1>
  <p>Официальный современный кроссплатформенный API для античита Darkness.</p>

  <p>
    <a href="https://dsc.gg/darkness-liquidation">
      <img alt="Discord" src="https://img.shields.io/discord/1380822433631834122?style=flat&label=Discord&logo=discord&color=5865F2&logoColor=white">
    </a>
    <img alt="Java 17" src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat&logo=openjdk&logoColor=white">
    <img alt="Platform Paper and Folia" src="https://img.shields.io/badge/Platform-Paper%20%2F%20Folia-FFFFFF?style=flat&logo=papermc&logoColor=black">
    <a href="https://jitpack.io/#Darkness-Liquidation/DarknessAPI">
      <img alt="JitPack" src="https://jitpack.io/v/Darkness-Liquidation/DarknessAPI.svg">
    </a>
  </p>

  <p>
    <a href="README.md">English</a>
    &middot;
    <a href="README-ru.md"><b>Русский</b></a>
    &middot;
    <a href="README-uk.md">Українська</a>
  </p>
</div>

---

### Что такое DarknessAPI?

DarknessAPI — официальный интерфейс для разработчиков сторонних плагинов, мини-игр и систем администрирования. Предоставляет чистую, потокобезопасную архитектуру с разделением ответственности и гарантией обратной совместимости.

Основные возможности:
- **Специализированные менеджеры**: интерфейсы с единой ответственностью (`UserManager`, `BypassManager`, `ViolationManager`, `PunishmentManager`, `AlertManager`).
- **Гранулярный Temp Bypass**: временный иммунитет глобально, по категориям (`Combat`, `Movement`, `World`) или по имени конкретного чека для скиллов, джетпаков, батутов и крюков-кошек.
- **Безопасное управление VL**: чтение агрегированной телеметрии и безопасный сброс нарушений без разрушения математических буферов проверок.
- **Асинхронность и совместимость с Folia**: явные контракты потокобезопасности (`@ThreadSafe`, `@RegionThreadOnly`) и асинхронные методы на базе `CompletableFuture`.
- **События Bukkit / Folia**: перехват детектов, отмена ложных срабатываний в PvP-аренах, модификация причин наказания и перехват волн банов.
- **Кастомные анимации наказаний**: создание кинематографических анимаций бана с эффектами частиц и звуками.

### Требования

- Java 17 или новее.
- Сервер Paper, Purpur или Folia с установленным Darkness Anticheat.

---

### Подключение зависимости (SemVer 2.0.0)

#### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Darkness-Liquidation:DarknessAPI:%version%")
}
```

#### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.Darkness-Liquidation:DarknessAPI:%version%'
}
```

#### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Darkness-Liquidation</groupId>
        <artifactId>DarknessAPI</artifactId>
        <version>%version%</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

### Архитектура и менеджеры

DarknessAPI разделяет обязанности по независимым менеджерам, сохраняя удобные статические вызовы для обратной совместимости:

| Менеджер | Назначение | Потокобезопасность |
| :--- | :--- | :--- |
| `UserManager` | Резолв сессий игроков (`DarknessUser`) и асинхронные запросы | `@ThreadSafe` |
| `BypassManager` | Глобальные, категорийные и чековые временные байпассы | `@ThreadSafe` |
| `ViolationManager` | Метрики нарушений, инспекция чеков и безопасный сброс VL | `@ThreadSafe` |
| `PunishmentManager` | Выполнение наказаний, хуки волн банов и анимации | `@ThreadSafe` |
| `AlertManager` | Отправка оповещений персоналу с правами | `@ThreadSafe` |

---

### 1. Временный иммунитет (`BypassManager`)

Идеально для скиллов, дэшей, батутов, крюков и телепортаций:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.BypassManager;
import org.bukkit.entity.Player;

BypassManager bypass = DarknessAPI.getBypassManager();

// Иммунитет от всех проверок на 5 секунд
bypass.setTempBypass(player, 5);

// Точный иммунитет в серверных тиках (30 тиков = 1.5 секунды)
bypass.setTempBypassTicks(player, 30);

// Иммунитет только для конкретной проверки (например, Sprint или Interpolation)
bypass.setTempBypass(player, "Interpolation", 3);
bypass.setTempBypassTicks(player, "Sprint", 40);

// Иммунитет для целой категории ("movement", сохраняя боевые проверки активными!)
bypass.setCategoryTempBypass(player, "movement", 3);

// Проверка статуса иммунитета
if (bypass.isBypassing(player) || bypass.isBypassing(player, "Interpolation")) {
    // Игрок находится под иммунитетом
}

// Досрочный сброс иммунитета
bypass.resetBypass(player);
```

> **Семантика байпасса**: Повторный вызов `setTempBypass` обновляет время истечения (`currentTick + durationTicks`). Время не стакается бесконечно, что предотвращает случайный перманентный байпасс.

---

### 2. Телеметрия и безопасный сброс VL (`ViolationManager`)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.ViolationManager;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.entity.Player;

ViolationManager violations = DarknessAPI.getViolationManager();

// Агрегированные метрики нарушений
double totalVl = violations.getTotalViolations(player);
double combatVl = violations.getCombatViolations(player);
double movementVl = violations.getMovementViolations(player);
double worldVl = violations.getWorldViolations(player);

// Итерация по всем активным чекам игрока
for (CheckData check : violations.getCheckData(player)) {
    String checkName = check.check();         // e.g. "KillAura"
    String subtype = check.type();            // e.g. "A"
    String fullName = check.fullCheckName();  // e.g. "KillAuraA"
    double vl = check.violations();
}

// БЕЗОПАСНЫЙ СБРОС: обнуляет счётчик VL до 0.0 без разрушения буферов предикшена
violations.resetViolations(player, "KillAura");
violations.resetViolations(player, "AutoClicker", "A");

// Симуляция срабатывания чека для тестов и проверок
violations.simulateFlag(player, "KillAura", "A", "Manual test");
```

---

### 3. Пользовательские сессии (`UserManager`)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.managers.UserManager;
import org.bukkit.entity.Player;

UserManager users = DarknessAPI.getUserManager();

// Синхронный резолв (O(1), потокобезопасно)
DarknessUser user = users.getUser(player);
if (user != null) {
    int ping = user.getTransactionPing();
    String brand = user.getBrand();
    String version = user.getVersionName();
    int trustScore = user.getTrustScore(); // 0 - 100%
}

// Асинхронный резолв (для Folia и асинхронных задач)
users.getUserAsync(player.getUniqueId()).thenAccept(u -> {
    if (u != null) {
        double vl = u.getTotalViolations();
    }
});
```

---

### 4. Наказания (`PunishmentManager`)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.PunishmentManager;
import org.bukkit.entity.Player;

PunishmentManager punishments = DarknessAPI.getPunishmentManager();

// Синхронное выполнение наказания
punishments.punish(player, "Использование читов");

// Асинхронный вызов (безопасен для сторонних потоков)
punishments.punishAsync(player.getUniqueId(), "Cheating");
```

---

### 5. Система событий Bukkit / Folia

События отправляются через стандартный `PluginManager` Bukkit.

> **Важно для Folia**: На серверах Folia события вызываются синхронно в **региональном потоке** игрока. Чтение данных абсолютно безопасно; при изменении мира в обработчике используйте региональный планировщик сущности.

```java
import ac.darkness.darknessac.api.events.DarknessViolationEvent;
import ac.darkness.darknessac.api.events.DarknessPunishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AnticheatListener implements Listener {

    // Отмена детектов на PvP-аренах
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onViolation(DarknessViolationEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("pvp_arena")) {
            event.setCancelled(true);
        }
    }

    // Отмена наказаний для персонала
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPunish(DarknessPunishEvent event) {
        if (event.getPlayer().hasPermission("darkness.bypass.punish")) {
            event.setCancelled(true);
        }
    }
}
```

---

### 6. Кастомные анимации наказаний

```java
package com.example.animation;

import ac.darkness.darknessac.api.punishment.ActiveAnimation;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StrikeAnimation extends AnimationMode {

    public StrikeAnimation() {
        super(40, 2); // 40 тиков длительность, шаг каждые 2 тика
    }

    @Override
    public void onStart(ActiveAnimation anim) {
        Player player = anim.getPlayer().getAsPlayer();
        if (player != null) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        }
    }

    @Override
    public void onTick(ActiveAnimation anim, int step) {
        Player player = anim.getPlayer().getAsPlayer();
        if (player == null) return;
        player.getWorld().spawnParticle(Particle.FLAME, anim.getX(), anim.getY() + 0.5, anim.getZ(), 4);
    }

    @Override
    public void onFinish(ActiveAnimation anim) {
        Player player = anim.getPlayer().getAsPlayer();
        if (player != null) {
            player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, anim.getX(), anim.getY() + 1.0, anim.getZ(), 1);
        }
    }
}
```

Регистрация:
```java
DarknessAPI.getPunishmentManager().registerAnimation("STRIKE", new StrikeAnimation());
```

---

### Гарантии и контракты API

1. **Жизненный цикл игрока**:
   - `DarknessUser` валиден **только пока игрок находится в сети**.
   - При выходе игрока сессия очищается; `getUser()` возвращает `null`.
   - Не сохраняйте ссылки на `DarknessUser` между тиками сервера.
2. **Семантика временного иммунитета**:
   - Время исчисляется в тиках сервера (20 тиков/сек).
   - Повторный вызов `setTempBypass` обновляет таймер до `currentTick + duration` (без бесконечного накопления).
   - Неизвестное название проверки при `isBypassing("НеизвестныйЧек")` безопасно возвращает `false` без NPE.
3. **Потокобезопасность**:
   - Все методы `UserManager`, `BypassManager` и `ViolationManager` помечены `@ThreadSafe` и безопасны для вызова из асинхронных задач и параллельных регионов Folia.
   - Чтение метрик `DarknessUser` (пинг, траст, нарушения) является атомарным.
4. **Семантическое версионирование (SemVer 2.0.0)**:
   - **MAJOR**: Несовместимые изменения сигнатур или удаление устаревших методов.
   - **MINOR**: Добавление новых менеджеров, методов и событий с сохранением обратной совместимости.
   - **PATCH**: Исправления ошибок и обновление документации.

---

### Пример готового плагина

👉 **[DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)**

---

### Сообщество и поддержка

- Discord: [Darkness Liquidation Discord](https://dsc.gg/darkness-liquidation)
