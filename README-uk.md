<div align="center">
  <h1>DarknessAPI</h1>
  <p>Офіційний сучасний кросплатформний API розробника для античита Darkness.</p>

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

DarknessAPI — офіційний програмний інтерфейс розробника для античита **Darkness**. Він дозволяє стороннім плагінам і розробникам серверів інтегруватися з античитом без залежності від закритих внутрішніх механізмів ядра.

Основні можливості:
- **Імунітети та Temp Bypass**: зручна видача тимчасового імунітету від перевірок для кастомних здібностей, джетпаків, ланчпадів, дешів і телепортацій.
- **Аналіз порушень**: отримання сумарних порушень, порушень за категоріями (комбат, рух, світ) або конкретними перевірками для меню, Discord-логерів та стаф-утиліт.
- **Події Bukkit**: перехоплення детектів, скасування хибних прапорців, модифікація команд покарання та інтеграція з бан-хвилями.
- **Кастомні анімації покарань**: створення та реєстрація кінематографічних анімацій (з партиклами, звуками та автовиконанням команд).

### Вимоги

- Java 17 або новіша.
- Сервер Paper, Purpur або Folia зі встановленим Darkness Anticheat.

---

### Підключення (Installation)

Додайте репозиторій JitPack та залежність `DarknessAPI` у збірковий скрипт вашого проєкту.

#### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Darkness-Liquidation:DarknessAPI:1.0")
}
```

#### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.Darkness-Liquidation:DarknessAPI:1.0'
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
        <version>1.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

### 1. Тимчасовий та постійний обхід (Temp Bypass)

Ідеально підходить для здібностей міні-ігор, джетпаків, ланчпадів, гаків-кішок і телепортацій:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import org.bukkit.entity.Player;

DarknessUser user = DarknessAPI.INSTANCE.getUser(player);
if (user != null) {
    // Надати тимчасовий імунітет від усіх перевірок на 5 секунд
    user.setTempBypass(5);

    // Перевірити, чи гравець перебуває під обходом (тимчасовим або за правами)
    if (user.isBypassing()) {
        // Гравець ігнорується перевірками
    }

    // Перевірити, чи активний саме тимчасовий обхід
    if (user.isTempBypassing()) {
        // Тимчасовий імунітет активний
    }

    // Достроково скинути тимчасовий імунітет
    user.resetBypass();
}
```

---

### 2. Отримання та керування порушеннями (Violations)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.entity.Player;

DarknessUser user = DarknessAPI.INSTANCE.getUser(player);
if (user != null) {
    // Сумарні порушення гравця
    double totalVl = user.getTotalViolations();

    // Порушення за категоріями
    double combatVl = user.getCombatViolations();
    double movementVl = user.getMovementViolations();
    double worldVl = user.getWorldViolations();

    // Порушення за конкретною перевіркою (наприклад AutoAction A — чек на автореспаун)
    double autoActionVl = user.getViolations("AutoAction", "A");

    // Встановити або скинути порушення
    user.setViolations("AutoAction", "A", 0.0);

    // Отримати колекцію даних усіх перевірок гравця
    for (CheckData check : user.getCheckData()) {
        String name = check.check();         // наприклад "AutoAction"
        String type = check.type();          // наприклад "A"
        String fullName = check.fullCheckName(); // "AutoActionA"
        double vl = check.violations();
    }
}
```

---

### 3. Система подій Bukkit (Events)

DarknessAPI реєструє стандартні події Bukkit. Підписка здійснюється через звичні `@EventHandler`:

#### DarknessViolationEvent (Прапорець / Детект)

Викликається в момент детекту. Дозволяє логувати порушення в Discord або скасовувати прапорці в міні-іграх:

```java
import ac.darkness.darknessac.api.events.DarknessViolationEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DetectionListener implements Listener {

    @EventHandler
    public void onViolation(DarknessViolationEvent event) {
        String check = event.getFullCheckName(); // "AutoAction.A" (автореспаун)
        double vl = event.getViolations();
        String verbose = event.getVerbose();

        // Скасувати прапорець у зоні міні-гри
        if (event.getPlayer().getWorld().getName().equals("pvp_arena")) {
            event.setCancelled(true);
        }
    }
}
```

#### DarknessPunishEvent (Покарання гравця)

Викликається перед виконанням команд покарання (кік/бан):

```java
import ac.darkness.darknessac.api.events.DarknessPunishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PunishListener implements Listener {

    @EventHandler
    public void onPunish(DarknessPunishEvent event) {
        // Змінити причину покарання
        event.setReason("Використання заборонених модифікацій");

        // Або повністю скасувати покарання
        // event.setCancelled(true);
    }
}
```

#### DarknessPunishWaveEvent (Бан-хвиля)

Викликається під час спрацьовування періодичної бан-хвилі:

```java
import ac.darkness.darknessac.api.events.DarknessPunishWaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PunishWaveListener implements Listener {

    @EventHandler
    public void onPunishWave(DarknessPunishWaveEvent event) {
        int queuedPlayers = event.getPlayerUUIDs().size();
        // Кастомне сповіщення сервера або інтеграція з логером
    }
}
```

---

### 4. Кастомні анімації покарань (Punishment Animations)

Darkness має вбудований рушій анімацій покарань. Коли гравець карається, античит може програти видовищну анімацію, під час якої:
- Гравець знерухомлений, захищений від хибних детектів та сетбеків.
- Дії гравця безпечно заблоковані.
- Античит автоматично виконує команди бана/кіка після завершення анімації.

#### Створення анімації

Для створення анімації успадкуйте клас `AnimationMode`:

```java
package com.example.animation;

import ac.darkness.darknessac.api.punishment.ActiveAnimation;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StrikeAnimation extends AnimationMode {

    // totalTicks: 40 (тривалість 2 секунди)
    // interval: 2 (onTick викликається кожні 2 тіки)
    public StrikeAnimation() {
        super(40, 2);
    }

    @Override
    public void onStart(ActiveAnimation anim) {
        Player player = anim.getPlayer().getAsPlayer();
        if (player != null) {
            player.getWorld().playSound(
                    player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f
            );
        }
    }

    @Override
    public void onTick(ActiveAnimation anim, int step) {
        Player player = anim.getPlayer().getAsPlayer();
        if (player == null) return;

        double radius = 1.2;
        double angle = step * 0.4;
        double x = anim.getX() + Math.cos(angle) * radius;
        double z = anim.getZ() + Math.sin(angle) * radius;

        player.getWorld().spawnParticle(
                Particle.FLAME, x, anim.getY() + 0.5, z, 3, 0.05, 0.05, 0.05, 0.01
        );
    }

    @Override
    public void onFinish(ActiveAnimation anim) {
        Player player = anim.getPlayer().getAsPlayer();
        if (player != null) {
            player.getWorld().spawnParticle(
                    Particle.EXPLOSION_EMITTER, anim.getX(), anim.getY() + 1.0, anim.getZ(), 1
            );
        }
    }
}
```

#### Реєстрація анімації

Зареєструйте створену анімацію в методі `onEnable` вашого плагіна:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        DarknessAPI.registerAnimation("STRIKE", new StrikeAnimation());
    }

    @Override
    public void onDisable() {
        DarknessAPI.unregisterAnimation("STRIKE");
    }
}
```

У конфігурації Darkness (`config.yml`):
```yaml
punish_command:
  animation: "STRIKE"
```

---

### 5. Ручні прапорці, покарання та сповіщення

```java
import ac.darkness.darknessac.api.DarknessAPI;
import org.bukkit.entity.Player;

// Надіслати сповіщення персоналу з правами darkness.alerts
DarknessAPI.INSTANCE.getAlertManager().sendAlert("Гравець " + player.getName() + " здійснив підозрілу дію");

// Примусово зафіксувати порушення у гравця
DarknessUser user = DarknessAPI.INSTANCE.getUser(player);
if (user != null) {
    user.flag("BadPackets", "A", "Suspicious packet sequence");

    // Запустити покарання через DarknessUser
    user.punish("Cheating");
}

// Або запустити автоматичне покарання через інстанс DarknessAPI
DarknessAPI.INSTANCE.punish(player, "Cheating");
```

---

### Приклад готового плагіна (DarknessExample)

Готовий приклад плагіна з демонстрацією реєстрації анімацій, підписки на події та роботи з API доступний в офіційному репозиторії:

👉 **[DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)**

---

### Підтримка та спільнота

- Discord спільноти: [Darkness Liquidation Discord](https://dsc.gg/darkness-liquidation)
