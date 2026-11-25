<div align="center">
  <h1>DarknessAPI</h1>
  <p>The official modern, cross-platform developer API for Darkness Liquidation.</p>

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
    <a href="README.md"><b>English</b></a>
    &middot;
    <a href="README-ru.md">Русский</a>
    &middot;
    <a href="README-uk.md">Українська</a>
  </p>
</div>

---

### Why DarknessAPI? Real-World Server Use Cases

Most anticheat APIs only offer basic alert forwarding. **DarknessAPI** is designed as a practical, production-ready toolkit solving the actual friction server developers face when integrating anticheats with complex game modes and custom mechanics:

1. **Fixing False Positives from Third-Party Plugins & Skills**
   Custom RPG skills (dashes, double jumps), launchpads, jetpacks, grappling hooks, and teleports often disrupt vanilla movement mathematics, triggering movement flags like `Speed`, `Fly`, or `Sprint`. With `BypassManager`, you can grant temporary immunity for a few ticks or seconds to `CheckCategory.MOVEMENT` alone—leaving combat checks (`CheckCategory.COMBAT`) completely active! The type-safe `enum CheckCategory` eliminates string typos.
2. **Trust Factor for Matchmaking & Dynamic Server Sanctions**
   Built-in trust score and levels (`TrustLevel.GREEN`, `ORANGE`, `RED`). Implement **CS2-style Matchmaking** (isolating suspicious players into shadow queues in duels and mini-games) or apply progressive server restrictions (increased command cooldowns, trading locks, mandatory PvP logouts).
3. **Check Buffer Management (`Buffer`) & Safe VL Resets**
   When unique server mechanics (like custom conveyor blocks or vehicles) start accumulating suspicion on a check, you can reset its internal buffer to `0.0` in real-time, preventing false flags or kicks. And if your own custom combat logic detects an impossible attack pattern, simulate an anticheat violation in a single line.
4. **Deep Player Telemetry for Staff & Scoreboards**
   Inspect real mouse sensitivity converted to human percentages (`0-200%`), client brand (Lunar Client, Badlion, Feather, Vanilla), transaction-based ping, and a **comprehensive list of checks the player has flagged** (e.g. `Interpolation (A) — 32 VL`) for staff scoreboards, tab lists, and GUI inspection menus.
5. **Flag & Setback Interception (`DarknessViolationEvent`, `DarknessSetbackEvent`)**
   Got decorative launchpads or high-speed elevators in your lobby? Intercept `DarknessViolationEvent` to cancel false detections. Running custom parkour or minigames? Cancel setbacks via `DarknessSetbackEvent` so players aren't teleported back while retaining audit logs for staff.
6. **Zero-Boilerplate Cinematic Ban Animations (`AnimationMode`)**
   Want to punish cheaters with lightning strikes, flame vortices, and levitation? Skip messy `BukkitRunnable` chains, manual tick tracking, and player freeze logic. Darkness handles check pausing, levitation, and executes punishment commands automatically once the animation finishes.
7. **Async, Folia, Type-Safety & Optional**
   Complete independence from Bukkit `Player`: all methods are overloaded for `UUID` and `DarknessIdentity` for safe execution from asynchronous threads and cross-platform readiness. Methods such as `findUser(player)` and `findCheck(name)` return `Optional`, enabling clean functional code without repetitive null checks. Check categories and types are strongly typed via `CheckCategory` and `CheckType`.

---

### Installation (JitPack)

DarknessAPI is published via JitPack:

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

### Architecture & Dedicated Managers

Access the API via the `DarknessAPI` singleton:

| Manager | Description | Thread Safety |
| :--- | :--- | :--- |
| `BypassManager` | Granular temporary immunity (globally, by category, or by check name) | `@ThreadSafe` |
| `UserManager` | Player sessions (`DarknessUser`), telemetry, sensitivity, and trust scores | `@ThreadSafe` |
| `ViolationManager` | Violation metrics, check data inspection, safe VL resets, and flag simulation | `@ThreadSafe` |
| `PunishmentManager` | Punishment execution, ban wave hooks, and cinematic animation registration | `@ThreadSafe` |
| `AlertManager` | Staff alert subscriptions and custom broadcast notifications | `@ThreadSafe` |

---

### Real-World Scenarios & Code Examples

#### Scenario 1: Fixing False Positives from Skills & Custom Items (`BypassManager`)

**Problem**: A player triggers a "Dash" ability, uses a launchpad, or rides a jetpack. The anticheat flags unnatural velocity under `Speed` or `Fly`. Disabling the entire anticheat is dangerous because cheaters could abuse abilities while keeping KillAura active.

**Solution**: Issue temporary immunity for the `CheckCategory.MOVEMENT` category or a specific check for an exact number of server ticks:

```java
import ac.darkness.darknessac.api.CheckCategory;
import ac.darkness.darknessac.api.CheckType;
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.BypassManager;
import org.bukkit.entity.Player;
import java.util.UUID;

BypassManager bypass = DarknessAPI.getBypassManager();

// 1. Precise immunity in ticks (40 ticks = exactly 2 seconds) for movement checks.
// Strongly-typed CheckCategory prevents typos like "movement"!
// Combat checks remain 100% active against cheaters!
bypass.setCategoryTempBypassTicks(player, CheckCategory.MOVEMENT, 40);

// Safe asynchronous invocation via UUID (ideal for worker threads & non-Player contexts):
UUID uuid = player.getUniqueId();
bypass.setCategoryTempBypassTicks(uuid, CheckCategory.MOVEMENT, 40);

// 2. Specific immunity for a check type (e.g. CheckType.SPRINT)
bypass.setTempBypassTicks(player, CheckType.SPRINT, 30);
bypass.setTempBypassTicks(uuid, CheckType.SPRINT, 30);

// 3. Disable or bypass a SPECIFIC individual check by name (e.g. only SpeedA or FlightB)
// If only one subtype false-flags, you don't need to disable all Speed checks or the entire Movement category!
bypass.setTempBypassTicks(player, "SpeedA", 40);         // Temporary bypass for 40 ticks (2 sec)
bypass.setTempBypass(player, "Flight (B)", 5);           // Temporary bypass for 5 seconds
bypass.setCheckEnabled(player, "SpeedA", false);         // Completely disable a specific check for this player
boolean speedAbypassing = bypass.isBypassing(player, "SpeedA"); // Query status

// Can also be invoked directly via DarknessAPI:
DarknessAPI.setTempBypass(player, "SpeedA", 5);
DarknessAPI.setCheckEnabled(player, "SpeedA", true);

// Or on the player's check object directly:
DarknessAPI.findUser(player)
    .flatMap(u -> u.findCheck("SpeedA"))
    .ifPresent(check -> {
        check.setTempBypass(5);    // Temporary bypass for 5 sec
        check.setEnabled(false);   // Completely toggle check
    });

// 4. Full temporary immunity during cinematic cutscenes (in seconds)
bypass.setTempBypass(player, 5);

// 5. Query bypass status
if (bypass.isBypassing(player, CheckCategory.MOVEMENT)) {
    // Player has active movement check immunity
}

// 6. Dynamic condition-based bypass predicate (no tick tracking or manual resets!):
// Example: bypass movement checks as long as player is riding a custom dragon
DarknessAPI.registerBypassPredicate(CheckCategory.MOVEMENT, p -> isFlyingDragon(p));

// 7. Prematurely cancel temporary bypass (e.g. if the skill was interrupted by a stun)
bypass.resetBypass(player);
```

> **Note**: Repeated calls to `setTempBypass` safely refresh the expiration tick (`currentTick + durationTicks`). Time does **not stack infinitely**, preventing accidental permanent bypasses.

---

#### Scenario 2: Trust Factor — Matchmaking & Dynamic Sanctions

**Problem**: Ranked duels, BedWars, and mini-games suffer when cheaters or alt accounts ruin matches for legitimate players before staff can ban them.

**Solution**: Darkness continuously computes a player trust rating (`TrustLevel` and `TrustScore` from 0 to 100%).

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.TrustLevel;
import org.bukkit.entity.Player;

// Clean Optional lookup: no mandatory if (user != null) boilerplate
DarknessAPI.findUser(player).ifPresent(user -> {
    TrustLevel level = user.getTrustLevel(); // GREEN, ORANGE, RED
    int score = user.getTrustScore();        // 0 - 100%
    int points = user.getTrustPoints();

    // 1. CS2-style Matchmaking in duels and mini-games:
    if (level == TrustLevel.RED || score < 40) {
        // Queue suspicious players together in an isolated shadow pool
        joinIsolatedQueue(player);
        player.sendMessage("§eSearching for opponents with similar trust profile...");
    } else {
        // Clean players only play against clean players
        joinCleanQueue(player);
    }

    // 2. Dynamic server restrictions:
    if (level == TrustLevel.RED) {
        applyIncreasedCommandCooldown(player, 10); // 10s cooldown on commands
        disableTrading(player);                    // Prevent item transfers
        notifyStaff("Suspicious player detected: " + player.getName() + " (Trust: " + score + "%)");
    }
});
```

---

#### Scenario 3: Real-Time Check Buffer Management & Flag Triggers

**Problem A (Preventing False Detections)**: A custom block or vehicle causes suspicion to rise inside a check. The buffer is nearing the violation threshold.  
**Solution**: Reset the check's buffer to `0.0` in real-time when your condition matches:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import org.bukkit.entity.Player;

// Concise Optional chain without nested null checks:
DarknessAPI.findUser(player)
    .flatMap(user -> user.findCheck("Speed"))
    .ifPresent(check -> {
        // Reset the suspicion buffer if the player is on a custom conveyor block
        if (playerIsOnCustomConveyorBlock(player)) {
            check.resetBuffer(); // Sets buffer back to 0.0
        }
    });
```

**Problem B (Triggering Anticheat Flags from Custom Logic)**: Your server code caught an impossible attack sequence or inventory macro and needs to trigger standard anticheat alerts and logging.  
**Solution**: Fire a simulated flag via the API:

```java
// Simulates an anticheat flag: triggers staff alerts, log entries, and auto-bans
user.flag("KillAura", "A", "Custom combat hook triggered (impossible attack pattern)");

// Or via ViolationManager:
DarknessAPI.getViolationManager().simulateFlag(player, "AutoClicker", "A", "Click macro detected");
```

**Safe Violation Reset**:
```java
// Resets the VL counter to 0.0 without invalidating mathematical prediction buffers
DarknessAPI.getViolationManager().resetViolations(player, "KillAura");
```

---

#### Scenario 4: Deep Player Telemetry & Staff Scoreboards

**Problem**: Staff members need to know who plays with modified clients, what their mouse sensitivity is, and **which checks they have flagged the most**.

**Solution**: Darkness exposes detailed client telemetry and the full collection of active violations:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.entity.Player;

DarknessUser user = DarknessAPI.getUser(player);
if (user != null) {
    // Mouse sensitivity converted to human percentages (0 - 200%)
    int sensX = user.getHumanSensitivityX();
    int sensY = user.getHumanSensitivityY();

    // Client brand, version, and network ping
    String brand = user.getBrand(); // "Lunar Client", "Badlion", "vanilla", etc.
    String version = user.getVersionName(); // "1.20.4"
    int ping = user.getTransactionPing();  // Accurate transaction-based ping

    // List of all checks the player has flagged (for scoreboards, tab lists, or GUI inspection)
    for (CheckData check : user.getCheckData()) {
        if (check.violations() > 0) {
            String checkName = check.check();         // "Interpolation"
            String type = check.type();               // "A"
            String fullName = check.fullCheckName(); // "InterpolationA"
            double vl = check.violations();          // 32.0

            // Example display: "Interpolation (A) — 32 VL"
            staff.sendMessage("§8» §7" + checkName + " §8(§c" + type + "§8) — §c" + (int) vl + " VL");
        }
    }
}
```

---

#### Scenario 5: Anticheat Events (`EventBus` & `@DarknessEventHandler`)

Darkness features its own high-throughput, non-blocking `EventBus` (`DarknessAPI.INSTANCE.getEventBus()`), eliminating fragile dependencies on `Bukkit.getPluginManager().callEvent()`.

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.TrustLevel;
import ac.darkness.darknessac.api.event.DarknessEventHandler;
import ac.darkness.darknessac.api.events.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AnticheatIntegrationListener {

    // Register in onEnable():
    // DarknessAPI.INSTANCE.getEventBus().registerAnnotatedListeners(plugin, this);

    // 1. Cancel false flags in designated lobby worlds or WorldGuard regions
    @DarknessEventHandler(priority = 0, ignoreCancelled = true)
    public void onViolation(DarknessViolationEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.getWorld().getName().equalsIgnoreCase("spawn_lobby")) {
            event.setCancelled(true);
            return;
        }

        // Forward severe detections to Discord
        if (event.getViolations() >= 15.0 && !event.isExperimental()) {
            sendDiscordAlert(event.getUsername(), event.getFullCheckName(), event.getViolations());
        }
    }

    // 2. Cancel setback teleports in custom parkour areas
    // Detections and violations are still logged, but player is NOT teleported back!
    @DarknessEventHandler(priority = 0, ignoreCancelled = true)
    public void onSetback(DarknessSetbackEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.getWorld().getName().equalsIgnoreCase("parkour")) {
            event.setCancelled(true); // Prevent rollback teleport
        }
    }

    // 3. React immediately when a player's Trust Factor degrades (GREEN -> ORANGE -> RED)
    @DarknessEventHandler
    public void onTrustChange(DarknessTrustChangeEvent event) {
        if (event.getNewLevel() == TrustLevel.RED) {
            notifyStaff("Alert: " + event.getUsername() + " trust factor dropped to RED (" + event.getTrustScore() + "%)");
        }
    }

    // 4. Cancel punishment execution for staff members with bypass permissions
    @DarknessEventHandler(priority = 100, ignoreCancelled = true)
    public void onPunish(DarknessPunishEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.hasPermission("darkness.bypass.punish")) {
            event.setCancelled(true); // Cancel ban or kick
            player.sendMessage("Anticheat punishment bypassed: you have staff immunity.");
        }
    }

    // 5. Intercept punishment waves (Ban Waves)
    @DarknessEventHandler
    public void onPunishWave(DarknessPunishWaveEvent event) {
        int queued = event.getPlayerUUIDs().size();
        int online = event.getOnlinePlayers().size();
        notifyStaff("Ban wave: " + queued + " players queued (" + online + " currently online)");
    }

    // 6. Intercept anticheat command executions
    @DarknessEventHandler
    public void onCommandExecute(DarknessCommandExecuteEvent event) {
        if (event.getCommand().startsWith("say ")) {
            event.setCommand(event.getCommand() + " [Verified]");
        }
    }

    // 7. Receive notification when Darkness configuration reload completes
    @DarknessEventHandler
    public void onReload(DarknessReloadEvent event) {
        plugin.getLogger().info("Darkness reloaded! Success=" + event.isSuccess() + " (took " + event.getDurationMillis() + "ms)");
    }
}
```

---

#### Scenario 6: Cinematic Punishment Animations (`AnimationMode`)

**Problem**: Creating cinematic punishment sequences (thunder strikes, flame rings, floating cheaters) usually requires tangled `BukkitRunnable` chains, phase timers, and manual command execution.

**Solution**: Extend `AnimationMode`. Darkness handles all boilerplate:
- Automatically freezes the player and pauses movement checks and setbacks;
- Enables packet-level levitation (if configured);
- Provides step-based callbacks (`onStart`, `onTick(step)`, `onFinish`);
- Provides `anim.getViewers()` to spawn particles only for nearby players;
- **Automatically runs punishment commands** (ban, kick, broadcast) upon animation completion.

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
        // 40 ticks total (2 seconds), step every 2 ticks, levitation enabled
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
        // Configured punishment commands run automatically right after onFinish!
    }
}
```

Registration in `onEnable`:
```java
DarknessAPI.registerAnimation("LIGHTNING", new LightningStrikeAnimation());
```

---

### Working Reference Project (DarknessExample)

A fully functional reference plugin showcasing all API features is available on GitHub:
- Admin commands: `/darkness bypass`, `/darkness info`, `/darkness flag`, `/darkness punish`;
- Sensitivity percentage, client brand, and active violation inspection;
- Custom `InfernoMode` punishment animation with `PacketEvents`;
- Event listeners for false-positive cancellation and ban waves.

👉 **[Explore DarknessExample on GitHub](https://github.com/Darkness-Liquidation/DarknessExample)**

---

### Contract Guarantees & Specifications

1. **Player Lifecycle**: `DarknessUser` is valid only while the player is connected. Upon disconnect, the session is invalidated. Never persist `DarknessUser` references across ticks—always query via `DarknessAPI.getUser(player)`.
2. **Bypass Duration Semantics**: Durations use standard server ticks (20 ticks/sec). Calling `setTempBypass` safely recalculates the expiration timestamp (`currentTick + ticks`) without unbounded stacking.
3. **Thread Safety**: Reading metrics from `DarknessUser` (ping, sensitivity, trust factor, violations) is completely thread-safe and safe for async tasks and Folia regional threads.
4. **Versioning & Compatibility**: Minor releases and patches guarantee full backwards compatibility—your plugin won't break on API updates.

---

### Community & Support

- Support Discord: [Darkness Liquidation Discord](https://dsc.gg/darkness-liquidation)
- Reference Project: [DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)
