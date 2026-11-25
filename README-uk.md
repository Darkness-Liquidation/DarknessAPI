<div align="center">
  <h1>DarknessAPI</h1>
  <p>Офіційний кросплатформний API для Darkness Liquidation.</p>

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
    <a href="README-ru.md">Русский</a>
    &middot;
    <a href="README-uk.md"><b>Українська</b></a>
  </p>
</div>

---

### Навіщо потрібен DarknessAPI? Реальні завдання сервера

Більшість античіт-API обмежуються простим пересиланням сповіщень у чат. **DarknessAPI** розроблений як практичний інструмент для творців серверів, міні-ігор та плагінів, що вирішує реальні проблеми інтеграції:

1. **Виправлення помилкових спрацьовувань від сторонніх плагінів**
   Кастомні скіли (ривки, деші), батути, джетпаки, крюки-кішки або телепортації часто ламають фізику Minecraft і викликають помилкові флаги перевірок руху (`Speed`, `Fly`, `Sprint`). За допомогою `BypassManager` ви можете надати точковий імунітет на пару тіків або секунд лише для категорії `CheckCategory.MOVEMENT`, залишивши бойові перевірки (`CheckCategory.COMBAT`) повністю активними! Типобезпечний `enum CheckCategory` захищає від помилок у рядках.
2. **Trust Factor (Фактор довіри) для підбору гравців та санкцій**
   Вбудована система очок і рівнів довіри (`TrustLevel.GREEN`, `ORANGE`, `RED`). Використовуйте її для **Matchmaking як у CS2** (підозрілі гравці грають лише з підозрілими в ізольованому пулі дуелей чи міні-ігор) або для динамічних обмежень (збільшений кулдаун на команди, блокування торгівлі, обов'язковий лог у PvP).
3. **Керування буфером перевірок (`Buffer`) та безпечне скидання VL**
   Якщо за специфічних умов вашої кастомної механіки (наприклад, особливий блок або кастомний транспорт) чек починає накопичувати підозру — ви можете на льоту скинути його внутрішній буфер у `0.0`, миттєво запобігши помилковому кіку чи флагу. А якщо ваша власна логіка виявила чітера — викличте флаг античіта одним рядком.
4. **Глибока телеметрія для модераторів та скорбордів**
   Отримуйте реальну чутливість миші гравця в людських відсотках (`0-200%`), бренд клієнта (Lunar Client, Badlion, Feather, Vanilla), пінг за транзакціями та **повний список перевірок, які гравець флагав** (наприклад, `Interpolation (A) — 32 VL`). Це дозволяє виводити статистику модераторам у скорборд, таб або GUI для пріоритизації нагляду.
5. **Керування флагами та сетбеками (`DarknessViolationEvent`, `DarknessSetbackEvent`)**
   На спавні встановлені декоративні батути або швидкісні ліфти? Перехопіть `DarknessViolationEvent` і скасуйте детектування в мирних регіонах. На сервері є кастомний паркур або міні-ігри? Перехопіть `DarknessSetbackEvent` і скасуйте відкат гравця назад, зберігши запис флагу в логах для модераторів.
6. **Кінематографічні анімації бану (`AnimationMode`) без костилів**
   Хочете ефектно покарати чітера (вихор вогню, удари грому, левітація в повітрі)? Забудьте про спагеті з `BukkitRunnable`, таймерів та ручного заморожування гравця. Darkness автоматично ставить на паузу перевірки та рух, надає готові методи `onStart`, `onTick(step)`, `onFinish` і сам виконує команди бану після завершення.
7. **Асинхронність, Folia, безпека типів та Optional**
   Повна незалежність від Bukkit `Player`: всі методи перевантажені під `UUID` та `DarknessIdentity` для безпечного виклику з асинхронних потоків. Методи `findUser(player)` та `findCheck(name)` повертають `Optional`, дозволяючи писати лаконічний код без постійних `if (user != null)`. Категорії та типи перевірок строго типізовані через `CheckCategory` та `CheckType`.

---

### Підключення залежності (JitPack)

DarknessAPI публікується через JitPack:

#### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Darkness-Liquidation:DarknessAPI:1.2.2")
}
```

#### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.Darkness-Liquidation:DarknessAPI:1.2.2'
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
        <version>1.2.2</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

### Архітектура та спеціалізовані менеджери

Доступ до API здійснюється через синглтон `DarknessAPI`:

| Менеджер | Призначення | Потокобезпека |
| :--- | :--- | :--- |
| `BypassManager` | Тимчасовий точковий імунітет (глобально, за категорією, за назвою чека) | `@ThreadSafe` |
| `UserManager` | Сесії гравців (`DarknessUser`), телеметрія, чутливість, траст-фактор | `@ThreadSafe` |
| `ViolationManager` | Метрики порушень, інспекція чеків, скидання VL та симуляція флагів | `@ThreadSafe` |
| `PunishmentManager` | Виконання покарань, хуки хвиль банів та реєстрація кастомних анімацій | `@ThreadSafe` |
| `AlertManager` | Керування підпискою персоналу на сповіщення та системні повідомлення | `@ThreadSafe` |

---

### Практичні сценарії використання

#### Сценарій 1: Фікс помилкових спрацьовувань від скілів та предметів (`BypassManager`)

**Проблема**: Гравець активує скіл "Ривок", кастомний джетпак або наступає на батут. Античіт фіксує неприродну швидкість і викликає флаги `Speed` або `Fly`. Вимикати весь античіт небезпечно — чітер може активувати KillAura під час скіла.

**Рішення**: Надаємо тимчасовий імунітет лише для категорії `CheckCategory.MOVEMENT` або для конкретної перевірки на точну кількість тіків:

```java
import ac.darkness.darknessac.api.CheckCategory;
import ac.darkness.darknessac.api.CheckType;
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.BypassManager;
import org.bukkit.entity.Player;
import java.util.UUID;

BypassManager bypass = DarknessAPI.getBypassManager();

// 1. Точний імунітет у тіках (40 тіків = рівно 2 секунди) на категорію руху.
// Типобезпечний CheckCategory виключає помилки на кшталт "movement"!
// Бойові перевірки (Combat) продовжують карати чітерів!
bypass.setCategoryTempBypassTicks(player, CheckCategory.MOVEMENT, 40);

// Безпечний виклик за UUID (зручно для асинхронних потоків та незалежності від Player):
UUID uuid = player.getUniqueId();
bypass.setCategoryTempBypassTicks(uuid, CheckCategory.MOVEMENT, 40);

// 2. Точковий імунітет за типом перевірки (наприклад, CheckType.SPRINT)
bypass.setTempBypassTicks(player, CheckType.SPRINT, 30);
bypass.setTempBypassTicks(uuid, CheckType.SPRINT, 30);

// 3. Вимкнення або байпас КОНКРЕТНОЇ перевірки за назвою (наприклад, тільки SpeedA або FlightB)
// Якщо ложно спрацьовує лише один підтип — не потрібно вимикати весь Speed чи всю категорію Movement!
bypass.setTempBypassTicks(player, "SpeedA", 40);         // Тимчасовий байпас на 40 тіків (2 сек)
bypass.setTempBypass(player, "Flight (B)", 5);           // Тимчасовий байпас на 5 секунд
bypass.setCheckEnabled(player, "SpeedA", false);         // Повне вимкнення конкретного чека для гравця
boolean speedAbypassing = bypass.isBypassing(player, "SpeedA"); // Перевірка статусу

// Те саме можна викликати напряму через DarknessAPI:
DarknessAPI.setTempBypass(player, "SpeedA", 5);
DarknessAPI.setCheckEnabled(player, "SpeedA", true);

// Або точково через об'єкт чека конкретного гравця:
DarknessAPI.findUser(player)
    .flatMap(u -> u.findCheck("SpeedA"))
    .ifPresent(check -> {
        check.setTempBypass(5);    // Тимчасовий байпас на 5 сек
        check.setEnabled(false);   // Повне вимкнення чека
    });

// 4. Повний тимчасовий імунітет на час катсцени (в секундах)
bypass.setTempBypass(player, 5);

// 5. Перевірка статусу імунітету
if (bypass.isBypassing(player, CheckCategory.MOVEMENT)) {
    // У гравця тимчасово вимкнено перевірки руху
}

// 6. Динамічний предикат байпасу (без підрахунку тіків та ручного скидання!):
// Приклад: перевірки руху вимкнено, поки гравець летить на кастомному драконі
DarknessAPI.registerBypassPredicate(CheckCategory.MOVEMENT, p -> isFlyingDragon(p));

// 7. Дострокове скидання тимчасового байпасу (наприклад, якщо скіл перервали оглушенням)
bypass.resetBypass(player);
```

> **Примітка**: Повторний виклик `setTempBypass` коректно зсуває час закінчення (`currentTick + durationTicks`) і **не стакається нескінченно**, виключаючи баги з вічним байпасом.

---

#### Сценарій 2: Trust Factor — підбір гравців (Matchmaking) та динамічні санкції

**Проблема**: У рейтингових дуелях, BedWars чи SkyWars чітери з твінків псують статистику чесним гравцям, поки адміністрація не встигла їх забанити.

**Рішення**: Darkness безперервно аналізує поведінку гравця і розраховує фактор довіри (`TrustLevel` та `TrustScore` від 0 до 100%).

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.TrustLevel;
import org.bukkit.entity.Player;

// Чистий пошук через Optional: жодної обов'язкової рутини з if (user != null)
DarknessAPI.findUser(player).ifPresent(user -> {
    TrustLevel level = user.getTrustLevel(); // GREEN, ORANGE, RED
    int score = user.getTrustScore();        // 0 - 100%
    int points = user.getTrustPoints();

    // 1. Matchmaking у дуелях (як у CS2 Trust Factor):
    if (level == TrustLevel.RED || score < 40) {
        // Підозрілий гравець потрапляє в ізольований пул до таких самих підозрілих
        joinIsolatedQueue(player);
        player.sendMessage("§eПошук суперника з аналогічним рівнем довіри...");
    } else {
        // Чесні гравці грають лише з чесними
        joinCleanQueue(player);
    }

    // 2. Динамічні обмеження на сервері:
    if (level == TrustLevel.RED) {
        applyIncreasedCommandCooldown(player, 10); // Кулдаун на команди 10 сек
        disableTrading(player);                    // Заборона трейду та передачі речей
        notifyStaff("Увага: підозрілий гравець " + player.getName() + " (Траст: " + score + "%)");
    }
});
```

---

#### Сценарій 3: Керування буфером перевірки (`Buffer`) та виклик флагів

**Проблема А (Помилкові спрацьовування)**: Специфичний кастомний блок сервера накопичує буфер у перевірки. До флага залишився 1 крок.  
**Рішення**: Замість вимкнення перевірки обнуляємо її буфер у 0 при настанні вашої умови:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import org.bukkit.entity.Player;

// Лаконічний ланцюжок Optional без вкладених перевірок на null:
DarknessAPI.findUser(player)
    .flatMap(user -> user.findCheck("Speed"))
    .ifPresent(check -> {
        // Якщо буфер почав зростати через специфічну механіку — скидаємо його
        if (playerIsOnCustomConveyorBlock(player)) {
            check.resetBuffer(); // Встановлює buffer у 0.0
        }
    });
```

**Проблема Б (Власна детекція чітерів)**: Ваш серверний плагін зафіксував неможливий клік або авто-тотем і хоче покарати гравця через стандартний пайплайн античіта.  
**Рішення**: Викличте флаг перевірки через API:

```java
// Симуляція флага чека: спрацюють стандартні сповіщення персоналу, запис у логи та авто-бан
user.flag("KillAura", "A", "Custom combat hook triggered (impossible attack pattern)");

// Або через ViolationManager:
DarknessAPI.getViolationManager().simulateFlag(player, "AutoClicker", "A", "Click macro detected");
```

**Безпечне скидання порушень (VL)**:
```java
// Обнуляє лічильник VL до 0.0 без руйнування внутрішніх трекерів предикшену античіта
DarknessAPI.getViolationManager().resetViolations(player, "KillAura");
```

---

#### Сценарій 4: Телеметрія гравця та скорборди / GUI для модераторів

**Проблема**: Персоналу потрібна прозора статистика: хто грає з підозрілого клієнта, у кого дивна чутливість і **які саме перевірки гравець флагав найчастіше**.

**Рішення**: Darkness надає детальну інформацію про клієнт і список усіх порушень гравця:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.entity.Player;

DarknessUser user = DarknessAPI.getUser(player);
if (user != null) {
    // Чутливість миші у відсотках (0 - 200%)
    int sensX = user.getHumanSensitivityX();
    int sensY = user.getHumanSensitivityY();

    // Клієнтський бренд та версія
    String brand = user.getBrand(); // "Lunar Client", "Badlion", "vanilla", etc.
    String version = user.getVersionName(); // "1.20.4"
    int ping = user.getTransactionPing();  // Точний пінг за транзакціями

    // Список усіх чеків, які гравець флагав (для скорборда, таба чи GUI інспектора)
    for (CheckData check : user.getCheckData()) {
        if (check.violations() > 0) {
            String checkName = check.check();         // "Interpolation"
            String type = check.type();               // "A"
            String fullName = check.fullCheckName(); // "InterpolationA"
            double vl = check.violations();          // 32.0

            // Приклад: "Interpolation (A) — 32 VL"
            staff.sendMessage("§8» §7" + checkName + " §8(§c" + type + "§8) — §c" + (int) vl + " VL");
        }
    }
}
```

---

#### Сценарій 5: Події античіта (`EventBus` та `@DarknessEventHandler`)

Darkness використовує власну високопродуктивну неблокуючу шину подій `EventBus` (`DarknessAPI.INSTANCE.getEventBus()`), що забезпечує кросплатформеність без прив'язки до `Bukkit.getPluginManager().callEvent()`.

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.TrustLevel;
import ac.darkness.darknessac.api.event.DarknessEventHandler;
import ac.darkness.darknessac.api.events.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AnticheatIntegrationListener {

    // Реєстрація в onEnable():
    // DarknessAPI.INSTANCE.getEventBus().registerAnnotatedListeners(plugin, this);

    // 1. Скасування помилкових флагів на спавні або в мирних лобі
    @DarknessEventHandler(priority = 0, ignoreCancelled = true)
    public void onViolation(DarknessViolationEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.getWorld().getName().equalsIgnoreCase("spawn_lobby")) {
            event.setCancelled(true);
            return;
        }

        // Логування критичних детектувань у Discord
        if (event.getViolations() >= 15.0 && !event.isExperimental()) {
            sendDiscordAlert(event.getUsername(), event.getFullCheckName(), event.getViolations());
        }
    }

    // 2. Скасування відкату (сетбека) в зонах кастомного паркуру
    // Флаг і запис у логах залишаються, але гравця НЕ відкидає назад!
    @DarknessEventHandler(priority = 0, ignoreCancelled = true)
    public void onSetback(DarknessSetbackEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.getWorld().getName().equalsIgnoreCase("parkour")) {
            event.setCancelled(true); // Скасовуємо відкат
        }
    }

    // 3. Миттєва реакція на зниження Trust Factor (GREEN -> ORANGE -> RED)
    @DarknessEventHandler
    public void onTrustChange(DarknessTrustChangeEvent event) {
        if (event.getNewLevel() == TrustLevel.RED) {
            notifyStaff("§cУвага! Фактор довіри гравця " + event.getUsername() + " впав до RED (" + event.getTrustScore() + "%).");
        }
    }

    // 4. Скасування покарання для персоналу з правами
    @DarknessEventHandler(priority = 100, ignoreCancelled = true)
    public void onPunish(DarknessPunishEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.hasPermission("darkness.bypass.punish")) {
            event.setCancelled(true); // Скасовуємо покарання
            player.sendMessage("§cПокарання античіта скасовано: у вас є імунітет.");
        }
    }

    // 5. Перехоплення хвилі покарань (Ban Wave)
    @DarknessEventHandler
    public void onPunishWave(DarknessPunishWaveEvent event) {
        int queued = event.getPlayerUUIDs().size();
        int online = event.getOnlinePlayers().size();
        notifyStaff("Хвиля банів: " + queued + " гравців у черзі (" + online + " онлайн)");
    }

    // 6. Перехоплення команд покарання античіта
    @DarknessEventHandler
    public void onCommandExecute(DarknessCommandExecuteEvent event) {
        if (event.getCommand().startsWith("say ")) {
            event.setCommand(event.getCommand() + " [Verified]");
        }
    }

    // 7. Повідомлення про завершення перезавантаження конфігурації античіта
    @DarknessEventHandler
    public void onReload(DarknessReloadEvent event) {
        plugin.getLogger().info("Darkness reloaded! Success=" + event.isSuccess() + " (took " + event.getDurationMillis() + "ms)");
    }
}
```

---

#### Сценарій 6: Кінематографічні анімації покарань (`AnimationMode`)

**Проблема**: Створення гарної анімації бану (блискавки, вихор вогню, левітація) своїми силами вимагає великої кількості костилів: десятка `BukkitRunnable`, підрахунку фаз, заморожування гравця та виклику команд бану після завершення.

**Рішення**: Базовий клас `AnimationMode`. Darkness бере на себе все:
- Автоматично ставить на паузу перевірки та рух для гравця;
- За потреби вмикає пакетну левітацію;
- Надає покадрові колбеки `onStart`, `onTick(step)` та `onFinish`;
- Надає `anim.getViewers()` — список глядачів поруч, щоб надсилати частки лише тим, хто бачить анімацію (без навантаження на весь сервер);
- Після закінчення анімації античіт **сам автоматично виконує команди покарання** (бан, кік, глобальне повідомлення).

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
        // 40 тіків тривалість (2 сек), крок кожні 2 тіки, левітація увімкнена
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
        // Команди бану з конфігурації Darkness виконаються автоматично одразу після цього методу!
    }
}
```

Реєстрація в `onEnable`:
```java
DarknessAPI.registerAnimation("LIGHTNING", new LightningStrikeAnimation());
```

---

### Готовий приклад реалізації (DarknessExample)

У репозиторії **[DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)** доступний повноцінний робочий плагін, що демонструє:
- Адміністративні команди: `/darkness bypass`, `/darkness info`, `/darkness flag`, `/darkness punish`;
- Перевірку траст-фактора, чутливості та активних флагів;
- Кастомний режим анімації покарання `InfernoMode` з пакетами `PacketEvents`;
- Лістенери скасування помилкових спрацьовувань та хвиль банів.

👉 **[Перейти до DarknessExample на GitHub](https://github.com/Darkness-Liquidation/DarknessExample)**

---

### Контракти та гарантії API

1. **Життєвий цикл сесії**: `DarknessUser` існує лише поки гравець на сервері. При виході сесія інвалідується. Не зберігайте постійні посилання на `DarknessUser` у колекціях — запитуйте через `DarknessAPI.getUser(player)`.
2. **Безпека часу**: Всі методи `setTempBypass` та `setTempBypassTicks` розраховують абсолютний час закінчення (`currentTick + ticks`). Вони безпечні при багаторазових викликах і не призводять до вічного обходу.
3. **Потокобезпека**: Читання метрик `DarknessUser` (пінг, чутливість, траст, violations) є потокобезпечним і може викликатися з асинхронних завдань та паралельних потоків Folia.
4. **Версіонування та сумісність**: Мінорні оновлення гарантують повну зворотну сумісність — ваш код не зламається при оновленні версії API.

---

### Спільнота та підтримка

- Офіційний Discord: [Darkness Liquidation Discord](https://dsc.gg/darkness-liquidation)
- Репозиторій прикладів: [DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)
