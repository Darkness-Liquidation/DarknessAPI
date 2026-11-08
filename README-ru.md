<div align="center">
  <h1>DarknessAPI</h1>
  <p>Официальный современный кроссплатформенный API разработчика для античита Darkness.</p>

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

DarknessAPI — официальный программный интерфейс разработчика для античита **Darkness**. Он позволяет сторонним плагинам и разработчикам серверов интегрироваться с античитом без зависимости от закрытых внутренних механизмов ядра.

Основные возможности:
- **Иммунитеты и Temp Bypass**: удобная выдача временного иммунитета от проверок для кастомных способностей, джетпаков, ланчпадов, дэшей и телепортаций.
- **Анализ нарушений**: получение суммарных нарушений, нарушений по категориям (комбат, передвижение, мир) или конкретным проверкам для меню, Discord-логеров и стафф-утилит.
- **События Bukkit**: перехват детектов, отмена ложных флагов, модификация команд наказания и интеграция с бан-волнами.
- **Кастомные анимации наказаний**: создание и регистрация кинематографичных анимаций (с партиклами, звуками и автовыполнением команд).

### Требования

- Java 17 или новее.
- Сервер Paper, Purpur или Folia с установленным Darkness Anticheat.

---

### Подключение (Installation)

Добавьте репозиторий JitPack и зависимость `DarknessAPI` в сборочный скрипт вашего проекта.

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

### 1. Временный и постоянный обход (Temp Bypass)

Идеально подходит для скиллов мини-игр, способностей, джетпаков, ланчпадов, крюков-кошек и телепортаций:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import org.bukkit.entity.Player;

// Вариант 1: Через DarknessUser
DarknessUser user = DarknessAPI.INSTANCE.getUser(player);
if (user != null) {
    // Иммунитет от всех проверок на 5 секунд
    user.setTempBypass(5);

    // Точный иммунитет в тиках (1 сек = 20 тиков, например 30 тиков = 1.5 сек)
    user.setTempBypassTicks(30);

    // Точечный иммунитет для конкретного чека (например Interpolation или Sprint)
    user.setTempBypass("Interpolation", 3);
    user.setTempBypassTicks("Sprint", 40);

    // Точечный иммунитет по категории ("Movement", оставляя Combat активным!)
    user.setCategoryTempBypass("Movement", 3);

    // Проверить статус иммунитета
    if (user.isBypassing() || user.isBypassing("Interpolation")) {
        // Игрок игнорируется проверками
    }

    // Досрочно сбросить временный иммунитет
    user.resetBypass();
}

// Вариант 2: Напрямую через DarknessAPI.INSTANCE в одну строку
DarknessAPI.INSTANCE.setTempBypass(player, 5);
DarknessAPI.INSTANCE.setCategoryTempBypass(player, "Movement", 3);
DarknessAPI.INSTANCE.setTempBypassTicks(player, "Interpolation", 40);
```

---

### 2. Получение и управление нарушениями (Violations)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.entity.Player;

DarknessUser user = DarknessAPI.INSTANCE.getUser(player);
if (user != null) {
    // Суммарные нарушения игрока
    double totalVl = user.getTotalViolations();

    // Нарушения по категориям
    double combatVl = user.getCombatViolations();
    double movementVl = user.getMovementViolations();
    double worldVl = user.getWorldViolations();

    // Нарушения по конкретному чеку (например AutoAction A — чек на автореспаун)
    double autoActionVl = user.getViolations("AutoAction", "A");

    // Установить или сбросить нарушения
    user.setViolations("AutoAction", "A", 0.0);

    // Получить коллекцию данных всех проверок игрока
    for (CheckData check : user.getCheckData()) {
        String name = check.check();         // например "AutoAction"
        String type = check.type();          // например "A"
        String fullName = check.fullCheckName(); // "AutoActionA"
        double vl = check.violations();
    }
}
```

---

### 3. События Bukkit (Events)

DarknessAPI регистрирует стандартные события Bukkit. Подписка осуществляется через привычные `@EventHandler`:

#### DarknessViolationEvent (Флаг / Детект)

Вызывается в момент детекта. Позволяет логировать нарушения в Discord или отменять флаги в мини-играх:

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
        int ping = event.getPing(); // Пинг игрока

        DarknessUser user = event.getUser(); // Прямой доступ к DarknessUser

        // Отменить флаг в зоне мини-игры
        if (event.getPlayer().getWorld().getName().equals("pvp_arena")) {
            event.setCancelled(true);
        }
    }
}
```

#### DarknessPunishEvent (Наказание игрока)

Вызывается перед выполнением команд наказания (кик/бан):

```java
import ac.darkness.darknessac.api.events.DarknessPunishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PunishListener implements Listener {

    @EventHandler
    public void onPunish(DarknessPunishEvent event) {
        // Изменить причину наказания
        event.setReason("Использование запрещенных модификаций");

        // Или полностью отменить наказание
        // event.setCancelled(true);
    }
}
```

#### DarknessPunishWaveEvent (Бан-волна)

Вызывается при срабатывании периодической бан-волны:

```java
import ac.darkness.darknessac.api.events.DarknessPunishWaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PunishWaveListener implements Listener {

    @EventHandler
    public void onPunishWave(DarknessPunishWaveEvent event) {
        int queuedPlayers = event.getPlayerUUIDs().size();
        // Кастомное оповещение сервера или интеграция с логером
    }
}
```

---

### 4. Кастомные анимации наказаний (Punishment Animations)

Darkness имеет встроенный движок анимаций наказаний. Когда игрок наказывается, античит может проиграть зрелищную анимацию, во время которой:
- Игрок обездвижен, защищён от ложных детектов и сетбеков.
- Действия игрока безопасно заблокированы.
- Античит автоматически выполняет команды бана/кика по завершении анимации.

#### Создание анимации

Для создания анимации унаследуйте класс `AnimationMode`:

```java
package com.example.animation;

import ac.darkness.darknessac.api.punishment.ActiveAnimation;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StrikeAnimation extends AnimationMode {

    // totalTicks: 40 (2 секунды длительность)
    // interval: 2 (onTick вызывается каждые 2 тика)
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

#### Регистрация анимации

Зарегистрируйте созданную анимацию в методе `onEnable` вашего плагина:

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

В конфигурации Darkness (`config.yml`):
```yaml
punish_command:
  animation: "STRIKE"
```

---

### 5. Ручные флаги, наказания и алерты

```java
import ac.darkness.darknessac.api.DarknessAPI;
import org.bukkit.entity.Player;

// Отправить алерт персоналу с правами darkness.alerts
DarknessAPI.INSTANCE.getAlertManager().sendAlert("Игрок " + player.getName() + " совершил подозрительное действие");

// Заставить античит зафиксировать нарушение у игрока
DarknessUser user = DarknessAPI.INSTANCE.getUser(player);
if (user != null) {
    user.flag("BadPackets", "A", "Suspicious packet sequence");

    // Запустить автоматическое наказание через DarknessUser
    user.punish("Cheating");
}

// Или запустить наказание через инстанс DarknessAPI
DarknessAPI.INSTANCE.punish(player, "Cheating");
```

---

### Пример готового плагина (DarknessExample)

Готовый пример плагина с демонстрацией регистрации анимаций, подписки на события и работы с API доступен в официальном репозитории:

👉 **[DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)**

---

### Поддержка и сообщество

- Discord сообщества: [Darkness Liquidation Discord](https://dsc.gg/darkness-liquidation)
