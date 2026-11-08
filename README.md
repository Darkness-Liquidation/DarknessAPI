<div align="center">
  <h1>DarknessAPI</h1>
  <p>The official modern, cross-platform developer API for Darkness Anticheat.</p>

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
    <a href="README.md"><b>English</b></a>
    &middot;
    <a href="README-ru.md">Русский</a>
    &middot;
    <a href="README-uk.md">Українська</a>
  </p>
</div>

---

### What is DarknessAPI?

DarknessAPI is the official developer interface for **Darkness Anticheat**. It enables third-party plugins, mini-games, and administration tools to interface with Darkness through clean, modular, and thread-safe contracts.

Key features:
- **Modular Managers**: Specialized, thread-safe interfaces (`UserManager`, `BypassManager`, `ViolationManager`, `PunishmentManager`, `AlertManager`).
- **Granular Temp Bypass**: Safely suspend checks globally, by category (`Combat`, `Movement`, `World`), or by check name for skills, launchpads, and grappling hooks.
- **Safe Violation API**: Query aggregated telemetry or safely reset violations for specific checks without breaking mathematical prediction buffers.
- **Async & Folia Ready**: Native support for asynchronous queries (`CompletableFuture`) and thread-safety annotations (`@ThreadSafe`, `@RegionThreadOnly`).
- **Bukkit & Folia Events**: Intercept detections, cancel false flags in mini-game arenas, customize punishment reasons, and handle ban waves.
- **Custom Punishment Animations**: Register custom cinematic punishment sequences with particle effects and sounds.

### Requirements

- Java 17 or newer.
- Paper, Purpur, or Folia server running Darkness Anticheat.

---

### Installation

Add JitPack to your build script, then add the `DarknessAPI` dependency using **Semantic Versioning (SemVer)**:

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

### Architecture & Dedicated Managers

DarknessAPI separates concerns into dedicated, single-responsibility managers while retaining convenient static shortcuts on `DarknessAPI` for backward compatibility:

| Manager | Description | Thread Safety |
| :--- | :--- | :--- |
| `UserManager` | Online player session resolution (`DarknessUser`) and async lookups | `@ThreadSafe` |
| `BypassManager` | Global, category, and check-specific temporary exemptions | `@ThreadSafe` |
| `ViolationManager` | Aggregated telemetry, check data inspection, and safe VL resets | `@ThreadSafe` |
| `PunishmentManager` | Punishment execution, ban-wave hooks, and animation registration | `@ThreadSafe` |
| `AlertManager` | Staff broadcast notifications and formatted alert dispatch | `@ThreadSafe` |

---

### 1. Temporary Bypass (`BypassManager`)

Suspend anticheat checks for abilities, dashes, launchpads, grappling hooks, or teleports:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.BypassManager;
import org.bukkit.entity.Player;

BypassManager bypass = DarknessAPI.getBypassManager();

// Immunity from all checks for 5 seconds
bypass.setTempBypass(player, 5);

// Precise immunity in server ticks (e.g. 30 ticks = 1.5 seconds)
bypass.setTempBypassTicks(player, 30);

// Specific immunity for a single check (e.g. Interpolation or Sprint)
bypass.setTempBypass(player, "Interpolation", 3);
bypass.setTempBypassTicks(player, "Sprint", 40);

// Specific immunity for an entire category ("movement", "combat", or "world")
bypass.setCategoryTempBypass(player, "movement", 3);

// Query bypass state
if (bypass.isBypassing(player) || bypass.isBypassing(player, "Interpolation")) {
    // Player is currently immune
}

// Prematurely cancel all bypasses
bypass.resetBypass(player);
```

> **Bypass Semantics**: Calling `setTempBypass` repeatedly refreshes the expiration timestamp (`currentTick + durationTicks`). It does not stack infinitely, preventing accidental permanent bypasses.

---

### 2. Violations & Safe VL Management (`ViolationManager`)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.ViolationManager;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.entity.Player;

ViolationManager violations = DarknessAPI.getViolationManager();

// Read aggregate metrics
double totalVl = violations.getTotalViolations(player);
double combatVl = violations.getCombatViolations(player);
double movementVl = violations.getMovementViolations(player);
double worldVl = violations.getWorldViolations(player);

// Iterate through active checks
for (CheckData check : violations.getCheckData(player)) {
    String checkName = check.check();         // e.g. "KillAura"
    String subtype = check.type();            // e.g. "A"
    String fullName = check.fullCheckName();  // e.g. "KillAuraA"
    double vl = check.violations();
}

// SAFE RESET: Resets the violation counter to 0.0 without clearing prediction trackers
violations.resetViolations(player, "KillAura");
violations.resetViolations(player, "AutoClicker", "A");

// Simulate check flag for administrative/test purposes
violations.simulateFlag(player, "KillAura", "A", "Manual test");
```

---

### 3. User Telemetry & Async Lookups (`UserManager`)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.managers.UserManager;
import org.bukkit.entity.Player;

UserManager users = DarknessAPI.getUserManager();

// Synchronous resolution (O(1) hash map lookup, completely thread-safe)
DarknessUser user = users.getUser(player);
if (user != null) {
    int ping = user.getTransactionPing();
    String clientBrand = user.getBrand();
    String version = user.getVersionName();
    int trustScore = user.getTrustScore(); // 0 - 100%
}

// Asynchronous resolution (ideal for Folia and async worker threads)
users.getUserAsync(player.getUniqueId()).thenAccept(u -> {
    if (u != null) {
        double vl = u.getTotalViolations();
    }
});
```

---

### 4. Punishments & Ban-Waves (`PunishmentManager`)

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.managers.PunishmentManager;
import org.bukkit.entity.Player;

PunishmentManager punishments = DarknessAPI.getPunishmentManager();

// Trigger punishment workflow synchronously
punishments.punish(player, "Cheating (Automated detection)");

// Asynchronous trigger (safe for Folia async tasks)
punishments.punishAsync(player.getUniqueId(), "Unfair Advantage");
```

---

### 5. Bukkit & Folia Event System

Darkness dispatches events through Bukkit's standard `PluginManager`.

> **Folia Note**: On Folia servers, events are fired synchronously on the owning player's **region thread**. Read operations are completely safe; when modifying world state in response to events, ensure you use the entity's region scheduler.

#### DarknessViolationEvent (Flags & Detections)

```java
import ac.darkness.darknessac.api.events.DarknessViolationEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FlagListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onViolation(DarknessViolationEvent event) {
        String check = event.getFullCheckName(); // e.g. "KillAura.A"
        double vl = event.getViolations();
        String verbose = event.getVerbose();

        // Cancel flags in designated mini-game arenas
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("pvp_exempt_arena")) {
            event.setCancelled(true);
        }
    }
}
```

#### DarknessPunishEvent (Player Punishments)

```java
import ac.darkness.darknessac.api.events.DarknessPunishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PunishListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPunish(DarknessPunishEvent event) {
        // Cancel punishment for staff
        if (event.getPlayer().hasPermission("darkness.bypass.punish")) {
            event.setCancelled(true);
        }
    }
}
```

#### DarknessPunishWaveEvent (Punishment Waves)

```java
import ac.darkness.darknessac.api.events.DarknessPunishWaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PunishWaveListener implements Listener {

    @EventHandler
    public void onPunishWave(DarknessPunishWaveEvent event) {
        int queuedCount = event.getPlayerUUIDs().size();
    }
}
```

---

### 6. Custom Punishment Animations

Implement custom cinematic sequences when players are punished:

```java
package com.example.animation;

import ac.darkness.darknessac.api.punishment.ActiveAnimation;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StrikeAnimation extends AnimationMode {

    public StrikeAnimation() {
        super(40, 2); // 40 ticks total, step every 2 ticks
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

Registration in `onEnable`:
```java
DarknessAPI.getPunishmentManager().registerAnimation("STRIKE", new StrikeAnimation());
```

---

### Contract Guarantees & Specifications

1. **Player Lifecycle**:
   - `DarknessUser` is valid **only while the player is connected**.
   - Upon player quit, the session is invalidated.
   - Do **not** cache `DarknessUser` references across ticks. Always resolve via `DarknessAPI.getUser(player)` or `UserManager`.
2. **Bypass Duration Semantics**:
   - Durations use standard server ticks (20 ticks/sec).
   - Calling `setTempBypass` resets the timer to `currentTick + duration`; it does not stack indefinitely.
   - Passing an unrecognized check name to `isBypassing(name)` gracefully returns `false` without throwing an exception.
3. **Thread Safety**:
   - All `UserManager`, `BypassManager`, and `ViolationManager` methods are marked `@ThreadSafe` and can be called from async tasks or parallel Folia region threads.
   - `DarknessUser` metrics (ping, trust factor, violations) are volatile/atomic reads.
4. **Semantic Versioning (SemVer 2.0.0)**:
   - **MAJOR**: Breaking API removals or backward-incompatible signature changes.
   - **MINOR**: Backward-compatible new managers, features, and methods.
   - **PATCH**: Bugfixes, documentation updates, and internal optimizations.

---

### Example Project (DarknessExample)

A complete, production-ready implementation showcasing all API features is available:

👉 **[DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)**

---

### Community & Support

- Support Discord: [Darkness Liquidation Discord](https://dsc.gg/darkness-liquidation)
