<div align="center">
  <h1>DarknessAPI</h1>
  <p>Официальный кроссплатформенный API для античита Darkness Anticheat.</p>

  <p>
    <a href="https://dsc.gg/darkness-liquidation">
      <img alt="Discord" src="https://img.shields.io/discord/1380822433631834122?style=flat&label=Discord&logo=discord&color=5865F2&logoColor=white">
    </a>
    <img alt="Java 17" src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat&logo=openjdk&logoColor=white">
    <img alt="Platform Paper and Folia" src="https://img.shields.io/badge/Platform-Paper%20%2F%20Folia-FFFFFF?style=flat&logo=papermc&logoColor=black">
    <a href="https://jitpack.io/#Darkness-Liquidation/DarknessAPI">
      <img alt="JitPack" src="https://jitpack.io/v/Darkness-Liquidation/DarknessAPI.svg">
    </a>
    <a href="https://github.com/Darkness-Liquidation/DarknessExample">
      <img alt="Example" src="https://img.shields.io/badge/Example-DarknessExample-blue?style=flat&logo=github">
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

### Зачем нужно DarknessAPI? Реальные задачи сервера

Большинство античит-API ограничиваются отправкой алертов в чат. **DarknessAPI** создан как практический инструмент для разработчиков серверов, мини-игр и плагинов, решающий реальные проблемы интеграции:

1. **Устранение ложных срабатываний от сторонних плагинов**
   Кастомные скиллы (рывки, дэши), батуты, джетпаки, хуки-кошки или телепортации часто ломают физику Minecraft и вызывают ложные флаги проверок движения (`Speed`, `Fly`, `Sprint`). Через `BypassManager` вы можете выдать точечный иммунитет на пару тиков или секунд только для категории `CheckCategory.MOVEMENT`, оставив боевые проверки (`CheckCategory.COMBAT`) полностью активными! Типобезопасный `enum CheckCategory` защищает от опечаток в строках.
2. **Trust Factor (Фактор доверия) для подбора игроков и санкций**
   Встроенная система очков и уровней доверия (`TrustLevel.GREEN`, `ORANGE`, `RED`). Используйте её для **Matchmaking как в CS2** (подозрительные игроки играют только с подозрительными в изолированном пуле дуэлей/мини-игр) или для наложения динамических ограничений (увеличенный кулдаун на команды, запрет трейдов, принудительный лог в PvP).
3. **Управление буфером проверок (`Buffer`) и безопасный сброс VL**
   Если при специфических условиях вашей кастомной механики (например, особый блок или кастомный транспорт) чек начинает накапливать подозрение — вы можете на лету сбросить его внутренний буфер в `0.0`, мгновенно предотвратив ложный кик или флаг. А если ваша собственная логика поймала читера — вызовите флаг античита одной строкой.
4. **Глубокая телеметрия для персонала и скорбордов**
   Получайте реальную чувствительность мыши игрока в человеческих процентах (`0-200%`), бренд клиента (Lunar Client, Badlion, Feather, Vanilla), пинг по транзакциям и **полный список чеков, которые игрок флагал** (например, `Interpolation (A) — 32 VL`). Это позволяет выводить статистику модераторам в скорборд, таб или GUI для приоритизации слежки.
5. **Отмена флагов в мирных зонах (WorldGuard / Спавн / Арены)**
   На спавне стоят декоративные батуты или скоростные лифты? Перехватите `DarknessViolationEvent` и отмените детекты в мирных регионах в две строки.
6. **Кинематографические анимации бана (`AnimationMode`) без костылей**
   Хотите эффектно наказать читера (воронка огня, раскаты грома, левитация в воздухе)? Забудьте про спагетти из `BukkitRunnable`, таймеров и ручной заморозки игрока. Darkness автоматически ставит на паузу чеки и движение, даёт готовые методы `onStart`, `onTick(step)`, `onFinish` и сам выполняет команды бана по завершении.
7. **Асинхронность, Folia, безопасность типов и Optional**
   Полная независимость от Bukkit `Player`: все методы перегружены под `UUID` и `DarknessIdentity` для безопасного вызова из асинхронных потоков. Методы `findUser(player)` и `findCheck(name)` возвращают `Optional`, позволяя писать чистый код без постоянных `if (user != null)`. Вся работа с категориями и типами проверок строго типизирована через `CheckCategory` и `CheckType`.

---

### Подключение зависимости (JitPack)

DarknessAPI публикуется через JitPack:

#### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Darkness-Liquidation:DarknessAPI:1.1.0")
}
```

#### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.Darkness-Liquidation:DarknessAPI:1.1.0'
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
        <version>1.1.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

### Архитектура и специализированные менеджеры

Доступ к API осуществляется через синглтон `DarknessAPI`:

| Менеджер | Назначение | Потокобезопасность |
| :--- | :--- | :--- |
| `BypassManager` | Временный точечный иммунитет (глобально, по категории, по имени чека) | `@ThreadSafe` |
| `UserManager` | Сессии игроков (`DarknessUser`), телеметрия, сенса, траст-фактор | `@ThreadSafe` |
| `ViolationManager` | Метрики нарушений, инспекция чеков, сброс VL и симуляция флагов | `@ThreadSafe` |
| `PunishmentManager` | Выполнение наказаний, хуки волн банов и регистрация кастомных анимаций | `@ThreadSafe` |
| `AlertManager` | Управление подпиской персонала на алерты и отправка системных сообщений | `@ThreadSafe` |

---

### Практические сценарии применения

#### Сценарий 1: Фикс ложных срабатываний от скиллов и предметов (`BypassManager`)

**Проблема**: Игрок активирует скилл "Рывок", кастомный джетпак или наступает на батут. Античит детектит неестественную скорость и флагает `Speed` или `Fly`. Отключать весь античит нельзя — читер может активировать KillAura во время скилла.

**Решение**: Выдаём временный иммунитет только для категории `CheckCategory.MOVEMENT` или для конкретной проверки на точное количество тиков:

```java
import ac.darkness.darknessac.api.CheckCategory;
import ac.darkness.darknessac.api.CheckType;
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.BypassManager;
import org.bukkit.entity.Player;
import java.util.UUID;

BypassManager bypass = DarknessAPI.getBypassManager();

// 1. Точный иммунитет в тиках (40 тиков = ровно 2 секунды) на категорию движения.
// Типобезопасный CheckCategory исключает опечатки вроде "movement"!
// Боевые чеки (Combat) продолжают наказывать читеров!
bypass.setCategoryTempBypassTicks(player, CheckCategory.MOVEMENT, 40);

// Безопасный вызов по UUID (удобно для асинхронных потоков и независимости от Player):
UUID uuid = player.getUniqueId();
bypass.setCategoryTempBypassTicks(uuid, CheckCategory.MOVEMENT, 40);

// 2. Точечный иммунитет по типу проверки (например, CheckType.SPRINT)
bypass.setTempBypassTicks(player, CheckType.SPRINT, 30);
bypass.setTempBypassTicks(uuid, CheckType.SPRINT, 30);

// 3. Полный временный иммунитет на время катсцены (в секундах)
bypass.setTempBypass(player, 5);

// 4. Проверка статуса иммунитета
if (bypass.isBypassing(player, CheckCategory.MOVEMENT)) {
    // У игрока временно отключены проверки движения
}

// 5. Досрочный сброс (например, если скилл прервали оглушением)
bypass.resetBypass(player);
```

> **Примечание**: Повторный вызов `setTempBypass` корректно сдвигает время окончания (`currentTick + durationTicks`) и **не стакается бесконечно**, исключая баги с вечным байпассом.

---

#### Сценарий 2: Trust Factor — подбор игроков (Matchmaking) и динамические санкции

**Проблема**: В рейтинговых дуэлях, BedWars или SkyWars читеры с твинков портят статистику честным игрокам. При этом администрация не всегда успевает забанить нарушителя мгновенно.

**Решение**: Darkness непрерывно анализирует стиль игры и рассчитывает фактор доверия игрока (`TrustLevel` и `TrustScore` от 0 до 100%).

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.TrustLevel;
import org.bukkit.entity.Player;

// Чистый поиск через Optional: никакой обязательной рутины с if (user != null)
DarknessAPI.findUser(player).ifPresent(user -> {
    TrustLevel level = user.getTrustLevel(); // GREEN, ORANGE, RED
    int score = user.getTrustScore();        // 0 - 100%
    int points = user.getTrustPoints();

    // 1. Matchmaking в дуэлях (как в CS2 Trust Factor):
    if (level == TrustLevel.RED || score < 40) {
        // Подозрительный игрок попадает в скрытый пул к таким же подозрительным
        joinIsolatedQueue(player);
        player.sendMessage("§eПоиск соперника с аналогичным уровнем доверия...");
    } else {
        // Честные игроки играют только с честными
        joinCleanQueue(player);
    }

    // 2. Динамические санкции на сервере:
    if (level == TrustLevel.RED) {
        applyIncreasedCommandCooldown(player, 10); // Кулдаун на команды 10 сек
        disableTrading(player);                    // Запрет трейда и передачи вещей
        notifyStaff("Внимание: подозрительный игрок " + player.getName() + " (Траст: " + score + "%)");
    }
});
```

---

#### Сценарий 3: Управление буфером проверки (`Buffer`) и вызов флагов

**Проблема А (Ложные срабатывания)**: Специфический кастомный блок сервера накапливает буфер у проверки. До флага остался 1 шаг.  
**Решение**: Вместо выключения проверки обнуляем её буфер в 0 при наступлении вашего условия:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import org.bukkit.entity.Player;

// Быстрый поиск чека через Optional без вложенных null-проверок:
DarknessAPI.findUser(player)
    .flatMap(user -> user.findCheck("Speed"))
    .ifPresent(check -> {
        // Если буфер начал расти из-за специфической механики сервера — сбрасываем его
        if (playerIsOnCustomConveyorBlock(player)) {
            check.resetBuffer(); // Устанавливает buffer в 0.0
        }
    });
```

**Проблема Б (Своя детекция читеров)**: Ваш серверный плагин зафиксировал невозможный клик или авто-тотем и хочет наказать игрока через стандартизированный пайплайн античита.  
**Решение**: Вызовите флаг проверки через API:

```java
// Симуляция флага чека: сработают стандартные алерты персоналу, запись в логи и авто-бан
user.flag("KillAura", "A", "Custom combat hook triggered (impossible attack pattern)");

// Или через ViolationManager:
DarknessAPI.getViolationManager().simulateFlag(player, "AutoClicker", "A", "Click macro detected");
```

**Безопасный сброс нарушений (VL)**:
```java
// Обнуляет счетчик VL до 0.0 без разрушения внутренних трекеров предикшена античита
DarknessAPI.getViolationManager().resetViolations(player, "KillAura");
```

---

#### Сценарий 4: Телеметрия игрока и скорборды / GUI для модераторов

**Проблема**: Персоналу нужна прозрачная сводка: кто играет с сомнительного клиента, у кого странная сенса и **какие конкретно проверки игрок флагал чаще всего**.

**Решение**: Darkness предоставляет детальную информацию о клиенте и список всех нарушений игрока:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.entity.Player;

DarknessUser user = DarknessAPI.getUser(player);
if (user != null) {
    // Человеческая чувствительность мыши в процентах (0 - 200%)
    int sensX = user.getHumanSensitivityX();
    int sensY = user.getHumanSensitivityY();

    // Клиентский бренд и версия
    String brand = user.getBrand(); // "Lunar Client", "Badlion", "vanilla", etc.
    String version = user.getVersionName(); // "1.20.4"
    int ping = user.getTransactionPing();  // Точный пинг по транзакциям

    // Список всех чеков, которые флагал игрок (для скорборда, таба или GUI инспектора)
    for (CheckData check : user.getCheckData()) {
        if (check.violations() > 0) {
            String checkName = check.check();         // "Interpolation"
            String type = check.type();               // "A"
            String fullName = check.fullCheckName(); // "InterpolationA"
            double vl = check.violations();          // 32.0

            // Пример: "Interpolation (A) — 32 VL"
            staff.sendMessage("§8» §7" + checkName + " §8(§c" + type + "§8) — §c" + (int) vl + " VL");
        }
    }
}
```

---

#### Сценарий 5: Отмена флагов в мирных зонах и защита персонала (События)

**Проблема**: В лобби спавна установлены декоративные лаунчпады, вызывающие детекты, а тестирующий администратор не должен улететь в бан.

```java
import ac.darkness.darknessac.api.events.DarknessViolationEvent;
import ac.darkness.darknessac.api.events.DarknessPunishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AnticheatIntegrationListener implements Listener {

    // 1. Отмена ложных флагов на спавне или в PvP-аренах с кастомной физикой
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onViolation(DarknessViolationEvent event) {
        // Проверка мира или региона WorldGuard
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("spawn_lobby")) {
            event.setCancelled(true);
            return;
        }

        // Логирование критических детектов в Discord
        if (event.getViolations() >= 15.0 && !event.isExperimental()) {
            sendDiscordAlert(event.getPlayer().getName(), event.getFullCheckName(), event.getViolations());
        }
    }

    // 2. Отмена наказания для стаффа с правами
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPunish(DarknessPunishEvent event) {
        if (event.getPlayer().hasPermission("darkness.bypass.punish")) {
            event.setCancelled(true); // Отменяем бан/кик
            event.getPlayer().sendMessage("§cНаказание античита отменено: у вас есть иммунитет.");
        }
    }
}
```

---

#### Сценарий 6: Кинематографические анимации наказаний (`AnimationMode`)

**Проблема**: Создание красивой анимации бана (молнии, спирали огня, левитация игрока) своими силами требует огромного количества костылей: десятка `BukkitRunnable`, подсчета фаз, заморозки игрока и вызова команд бана после завершения.

**Решение**: Базовый класс `AnimationMode`. Darkness берет на себя всё:
- Автоматически ставит на паузу чеки и сетбэки для наказываемого игрока;
- При необходимости включает пакетную левитацию;
- Предоставляет покадровые колбэки `onStart`, `onTick(step)` и `onFinish`;
- Предоставляет `anim.getViewers()` — список зрителей рядом, чтобы отправлять партиклы только тем, кто реально видит анимацию (без нагрузки на сервер);
- По окончании анимации античит **сам автоматически исполняет команды наказания** (бан, кик, глобальный анонс).

```java
package com.example.animation;

import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.punishment.ActiveAnimation;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LightningStrikeAnimation extends AnimationMode {

    public LightningStrikeAnimation() {
        // 40 тиков длительность (2 сек), шаг каждые 2 тика, левитация включена
        super(40, 2, true, 1);
    }

    @Override
    public void onStart(@NotNull ActiveAnimation anim) {
        Player player = Bukkit.getPlayer(anim.getUser().getUniqueId());
        if (player != null) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.8f);
        }
    }

    @Override
    public void onTick(@NotNull ActiveAnimation anim, int step) {
        Player player = Bukkit.getPlayer(anim.getUser().getUniqueId());
        if (player == null) return;

        double radius = 1.5 - (step * 0.05);
        double angle = step * 0.5;
        double x = anim.getX() + radius * Math.cos(angle);
        double z = anim.getZ() + radius * Math.sin(angle);

        player.getWorld().spawnParticle(Particle.FLAME, x, anim.getY() + (step * 0.1), z, 2, 0, 0, 0, 0.01);
    }

    @Override
    public void onFinish(@NotNull ActiveAnimation anim) {
        Player player = Bukkit.getPlayer(anim.getUser().getUniqueId());
        if (player != null) {
            player.getWorld().strikeLightningEffect(player.getLocation());
            player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, anim.getX(), anim.getY() + 1.0, anim.getZ(), 1);
        }
        // Команды бана из конфига Darkness выполнятся сразу после этого метода автоматически!
    }
}
```

Регистрация в `onEnable`:
```java
DarknessAPI.registerAnimation("LIGHTNING", new LightningStrikeAnimation());
```

---

### Готовый пример реализации (DarknessExample)

В репозитории **[DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)** доступен полноценный рабочий плагин, демонстрирующий:
- Административные команды: `/darkness bypass`, `/darkness info`, `/darkness flag`, `/darkness punish`;
- Проверку траст-фактора, сенсы и активных флагов;
- Кастомный режим анимации наказания `InfernoMode` с пакетами `PacketEvents`;
- Листенеры отмены ложных срабатываний и волн банов.

👉 **[Перейти к DarknessExample на GitHub](https://github.com/Darkness-Liquidation/DarknessExample)**

---

### Контракты и гарантии API

1. **Жизненный цикл сессии**: `DarknessUser` существует только пока игрок на сервере. При выходе игрока сессия инвалидируется. Не сохраняйте постоянные ссылки на `DarknessUser` в коллекциях — запрашивайте через `DarknessAPI.getUser(player)`.
2. **Безопасность времени**: Все методы `setTempBypass` и `setTempBypassTicks` рассчитывают абсолютное время окончания (`currentTick + ticks`). Они безопасны при многократных вызовах и не приводят к вечному обходу.
3. **Потокобезопасность**: Чтение метрик `DarknessUser` (пинг, чувствительность, траст, violations) является потокобезопасным и может вызываться из асинхронных задач и параллельных потоков Folia.
4. **Версионирование и совместимость**: Минорные обновления гарантируют полную обратную совместимость — ваш код не сломается при обновлении версии API.

---

### Сообщество и поддержка

- Официальный Discord: [Darkness Liquidation Discord](https://dsc.gg/darkness-liquidation)
- Репозиторий примеров: [DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)
