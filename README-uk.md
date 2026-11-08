<div align="center">
  <h1>DarknessAPI</h1>
  <p>Офіційний сучасний кросплатформний API для античита Darkness.</p>

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
    <a href="README-ru.md">Русский</a>
    &middot;
    <a href="README-uk.md"><b>Українська</b></a>
  </p>
</div>

---

### Що таке DarknessAPI?

DarknessAPI — офіційний інтерфейс для розробників сторонніх плагінів, міні-ігор та систем адміністрування. Забезпечує чисту, потокобезпечну архітектуру з чітким розділенням відповідальності та гарантією зворотної сумісності.

Основні можливості:
- **Спеціалізовані менеджери**: інтерфейси з єдиною відповідальністю (`UserManager`, `BypassManager`, `ViolationManager`, `PunishmentManager`, `AlertManager`).
- **Гранулярний Temp Bypass**: тимчасовий імунітет глобально, за категоріями (`Combat`, `Movement`, `World`) або за назвою чека для скілів, джетпаків, батутів та крюків.
- **Безпечне керування VL**: читання агрегованої телеметрії та безпечне скидання порушень без порушення математичних буферів перевірок.
- **Асинхронність та сумісність з Folia**: чіткі контракти потокобезпеки (`@ThreadSafe`, `@RegionThreadOnly`) та асинхронні методи на основі `CompletableFuture`.
- **Події Bukkit / Folia**: перехоплення детектів, скасування помилкових спрацьовувань на PvP-аренах, модифікація причин покарання та перехоплення хвиль банів.
- **Кастомні анімації покарань**: створення кінематографічних анімацій бану з ефектами часток та звуками.

### Вимоги

- Java 17 або новіша.
- Сервер Paper, Purpur або Folia із встановленим Darkness Anticheat.

---

### Підключення залежності (SemVer 2.0.0)

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

### Архітектура та менеджери

DarknessAPI розділяє обов'язки за незалежними менеджерами, зберігаючи зручні статичні виклики для зворотної сумісності:

| Менеджер | Призначення | Потокобезпека |
| :--- | :--- | :--- |
| `UserManager` | Резолв сесій гравців (`DarknessUser`) та асинхронні запити | `@ThreadSafe` |
| `BypassManager` | Глобальні, категорійні та чекові тимчасові байпаси | `@ThreadSafe` |
| `ViolationManager` | Метрики порушень, інспекція чеків та безпечне скидання VL | `@ThreadSafe` |
| `PunishmentManager` | Виконання покарань, хуки хвиль банів та анімації | `@ThreadSafe` |
| `AlertManager` | Відправлення сповіщень персоналу з правами | `@ThreadSafe` |

---

### 1. Тимчасовий імунітет (`BypassManager`)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.BypassManager;
import org.bukkit.entity.Player;

BypassManager bypass = DarknessAPI.getBypassManager();

// Імунітет від усіх перевірок на 5 секунд
bypass.setTempBypass(player, 5);

// Точний імунітет у серверних тіках (30 тіків = 1.5 секунди)
bypass.setTempBypassTicks(player, 30);

// Імунітет тільки для конкретної перевірки (Sprint або Interpolation)
bypass.setTempBypass(player, "Interpolation", 3);
bypass.setTempBypassTicks(player, "Sprint", 40);

// Імунітет для всієї категорії ("movement", залишаючи бойові перевірки активними!)
bypass.setCategoryTempBypass(player, "movement", 3);

// Перевірка статусу імунітету
if (bypass.isBypassing(player) || bypass.isBypassing(player, "Interpolation")) {
    // Гравець перебуває під імунітетом
}

// Дострокове скидання імунітету
bypass.resetBypass(player);
```

> **Семантика байпасу**: Повторний виклик `setTempBypass` оновлює час закінчення (`currentTick + durationTicks`). Час не стакається нескінченно, що запобігає випадковому перманентному байпасу.

---

### 2. Телеметрія та безпечне скидання VL (`ViolationManager`)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.ViolationManager;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.entity.Player;

ViolationManager violations = DarknessAPI.getViolationManager();

// Агреговані метрики порушень
double totalVl = violations.getTotalViolations(player);
double combatVl = violations.getCombatViolations(player);
double movementVl = violations.getMovementViolations(player);
double worldVl = violations.getWorldViolations(player);

// Ітерація по всіх активних чеках гравця
for (CheckData check : violations.getCheckData(player)) {
    String checkName = check.check();         // e.g. "KillAura"
    String subtype = check.type();            // e.g. "A"
    String fullName = check.fullCheckName();  // e.g. "KillAuraA"
    double vl = check.violations();
}

// БЕЗПЕЧНЕ СКИДАННЯ: обнуляє лічильник VL до 0.0 без руйнування буферів предикшену
violations.resetViolations(player, "KillAura");
violations.resetViolations(player, "AutoClicker", "A");

// Симуляція спрацювання чека для тестів
violations.simulateFlag(player, "KillAura", "A", "Manual test");
```

---

### 3. Користувацькі сесії (`UserManager`)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.managers.UserManager;
import org.bukkit.entity.Player;

UserManager users = DarknessAPI.getUserManager();

// Синхронний резолв (O(1), потокобезпечно)
DarknessUser user = users.getUser(player);
if (user != null) {
    int ping = user.getTransactionPing();
    String brand = user.getBrand();
    String version = user.getVersionName();
    int trustScore = user.getTrustScore(); // 0 - 100%
}

// Асинхронний резолв (для Folia та асинхронних завдань)
users.getUserAsync(player.getUniqueId()).thenAccept(u -> {
    if (u != null) {
        double vl = u.getTotalViolations();
    }
});
```

---

### 4. Покарання (`PunishmentManager`)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.PunishmentManager;
import org.bukkit.entity.Player;

PunishmentManager punishments = DarknessAPI.getPunishmentManager();

// Синхронне виконання покарання
punishments.punish(player, "Використання чітів");

// Асинхронний виклик (безпечний для сторонніх потоків)
punishments.punishAsync(player.getUniqueId(), "Cheating");
```

---

### 5. Система подій Bukkit / Folia

Події надсилаються через стандартний `PluginManager` Bukkit.

> **Важливо для Folia**: На серверах Folia події викликаються синхронно в **регіональному потоці** гравця. Читання даних повністю безпечне; при зміні світу в обробнику використовуйте регіональний планувальник сутності.

```java
import ac.darkness.darknessac.api.events.DarknessViolationEvent;
import ac.darkness.darknessac.api.events.DarknessPunishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AnticheatListener implements Listener {

    // Скасування детектів на PvP-аренах
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onViolation(DarknessViolationEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("pvp_arena")) {
            event.setCancelled(true);
        }
    }

    // Скасування покарань для персоналу
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPunish(DarknessPunishEvent event) {
        if (event.getPlayer().hasPermission("darkness.bypass.punish")) {
            event.setCancelled(true);
        }
    }
}
```

---

### 6. Кастомні анімації покарань

```java
package com.example.animation;

import ac.darkness.darknessac.api.punishment.ActiveAnimation;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StrikeAnimation extends AnimationMode {

    public StrikeAnimation() {
        super(40, 2); // 40 тіків тривалість, крок кожні 2 тіки
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

Реєстрація:
```java
DarknessAPI.getPunishmentManager().registerAnimation("STRIKE", new StrikeAnimation());
```

---

### Гарантії та контракти API

1. **Життєвий цикл гравця**:
   - `DarknessUser` валідний **тільки поки гравець перебуває в мережі**.
   - При виході гравця сесія очищається; `getUser()` повертає `null`.
   - Не зберігайте посилання на `DarknessUser` між тіками сервера.
2. **Семантика тимчасового імунітету**:
   - Час обчислюється в тіках сервера (20 тіків/сек).
   - Повторний виклик `setTempBypass` оновлює таймер до `currentTick + duration` (без нескінченного накопичення).
   - Невідома назва перевірки при `isBypassing("НевідомийЧек")` безпечно повертає `false` без NPE.
3. **Потокобезпека**:
   - Всі методи `UserManager`, `BypassManager` та `ViolationManager` позначені `@ThreadSafe` і безпечні для виклику з асинхронних завдань та паралельних регіонів Folia.
   - Читання метрик `DarknessUser` (пінг, траст, порушення) є атомарним.
4. **Семантичне версіонування (SemVer 2.0.0)**:
   - **MAJOR**: Несумісні зміни сигнатур або видалення застарілих методів.
   - **MINOR**: Додавання нових менеджерів, методів та подій зі збереженням зворотної сумісності.
   - **PATCH**: Виправлення помилок та оновлення документації.

---

### Приклад готового плагіна

👉 **[DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)**

---

### Спільнота та підтримка

- Discord: [Darkness Liquidation Discord](https://dsc.gg/darkness-liquidation)
