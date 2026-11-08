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

DarknessAPI is the official developer interface for **Darkness Anticheat**. It allows third-party plugin developers and server administrators to integrate with Darkness without depending on internal implementation details.

Key use cases:
- **Exemptions & Temp Bypass**: Seamlessly grant temporary immunity for mini-game skills, jetpacks, grappling hooks, and teleports.
- **Violation Queries**: Query total, combat, movement, or check-specific violations for moderation menus, Discord loggers, and staff utilities.
- **Bukkit Events**: Intercept flags, cancel false positives, customize punishment commands, and integrate with ban-waves.
- **Custom Punishment Animations**: Create and register cinematic punishment sequences (with particle effects, sounds, and automatic command execution).

### Requirements

- Java 17 or newer.
- Paper, Purpur, or Folia server running Darkness Anticheat.

---

### Installation

Add JitPack to your build script, then add the `DarknessAPI` dependency.

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

### 1. Temporary & Permanent Bypass (Temp Bypass)

Ideal for custom abilities, dashes, launchpads, grappling hooks, or respawns where physics simulation should be temporarily suspended:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import org.bukkit.entity.Player;

DarknessUser user = DarknessAPI.INSTANCE.getUser(player);
if (user != null) {
    // Grant temporary immunity from all checks for 5 seconds
    user.setTempBypass(5);

    // Check if player currently bypasses checks (temporary or permission)
    if (user.isBypassing()) {
        // Player is exempted
    }

    // Check if player has an active temporary bypass
    if (user.isTempBypassing()) {
        // Player has temporary immunity
    }

    // Cancel / reset bypass immediately
    user.resetBypass();
}
```

---

### 2. Violations & Check Telemetry

Query aggregated violations or iterate over all registered checks:

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.entity.Player;

DarknessUser user = DarknessAPI.INSTANCE.getUser(player);
if (user != null) {
    // Total violation count across all checks
    double totalVl = user.getTotalViolations();

    // Category violation counters
    double combatVl = user.getCombatViolations();
    double movementVl = user.getMovementViolations();
    double worldVl = user.getWorldViolations();

    // Specific check violation level (e.g. AutoAction A - AutoRespawn check)
    double autoActionVl = user.getViolations("AutoAction", "A");

    // Reset or update check violations
    user.setViolations("AutoAction", "A", 0.0);

    // Iterate through all active player check telemetry
    for (CheckData check : user.getCheckData()) {
        String name = check.check();         // e.g. "AutoAction"
        String type = check.type();          // e.g. "A"
        String fullName = check.fullCheckName(); // e.g. "AutoActionA"
        double vl = check.violations();
    }
}
```

---

### 3. Bukkit Event System

Darkness dispatches standard Bukkit events through the server's `PluginManager`. You can listen to them using standard `@EventHandler` annotations:

#### DarknessViolationEvent (Flags & Detection)

Fired whenever a check flags suspicious behavior. Allows inspecting verbose data, sending custom Discord webhooks, or canceling the detection:

```java
import ac.darkness.darknessac.api.events.DarknessViolationEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DetectionListener implements Listener {

    @EventHandler
    public void onViolation(DarknessViolationEvent event) {
        String check = event.getFullCheckName(); // "AutoAction.A" (AutoRespawn)
        double vl = event.getViolations();
        String verbose = event.getVerbose();

        // Cancel flag in specific mini-game arenas
        if (event.getPlayer().getWorld().getName().equals("pvp_arena")) {
            event.setCancelled(true);
        }
    }
}
```

#### DarknessPunishEvent (Player Punishments)

Fired before Darkness executes the configured punishment commands (kicks or bans):

```java
import ac.darkness.darknessac.api.events.DarknessPunishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PunishListener implements Listener {

    @EventHandler
    public void onPunish(DarknessPunishEvent event) {
        // Customize the punishment reason
        event.setReason("Unfair Advantage (Automated detection)");

        // Or cancel the punishment completely
        // event.setCancelled(true);
    }
}
```

#### DarknessPunishWaveEvent (Punishment Waves)

Fired when a scheduled batch ban-wave triggers:

```java
import ac.darkness.darknessac.api.events.DarknessPunishWaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PunishWaveListener implements Listener {

    @EventHandler
    public void onPunishWave(DarknessPunishWaveEvent event) {
        int queuedPlayers = event.getPlayerUUIDs().size();
        // Custom broadcast or Discord notification
    }
}
```

---

### 4. Custom Punishment Animations

Darkness includes a built-in punishment animation engine. When a player is punished, Darkness can play a cinematic sequence during which:
- The target is locked in place and shielded from anticheat setbacks.
- Movement and combat interactions are safely paused.
- Configured punishment commands (ban, kick) are automatically executed when the animation finishes.

#### Creating an Animation

Extend `AnimationMode` to implement your custom sequence:

```java
package com.example.animation;

import ac.darkness.darknessac.api.punishment.ActiveAnimation;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StrikeAnimation extends AnimationMode {

    // totalTicks: 40 (2 seconds duration)
    // interval: 2 (onTick runs every 2 ticks)
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

#### Registering the Animation

Register your animation in `onEnable` of your plugin:

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

Configure your animation in Darkness `config.yml`:
```yaml
punish_command:
  animation: "STRIKE"
```

---

### 5. Staff Alerts & Manual Actions

```java
import ac.darkness.darknessac.api.DarknessAPI;
import org.bukkit.entity.Player;

// Broadcast an alert to staff members with darkness.alerts permission
DarknessAPI.INSTANCE.getAlertManager().sendAlert("Player " + player.getName() + " performed a suspicious action");

// Manually trigger a check flag on a player
DarknessUser user = DarknessAPI.INSTANCE.getUser(player);
if (user != null) {
    user.flag("BadPackets", "A", "Suspicious packet sequence");

    // Trigger punishment directly via DarknessUser
    user.punish("Cheating");
}

// Or trigger automated punishment flow via DarknessAPI instance
DarknessAPI.INSTANCE.punish(player, "Cheating");
```

---

### Example Project (DarknessExample)

A complete, ready-to-run Paper plugin implementation showcasing event handling, custom animations, and bypasses is available in the official repository:

👉 **[DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample)**

---

### Support and Community

- Community & Support: [Darkness Liquidation Discord](https://dsc.gg/darkness-liquidation)
