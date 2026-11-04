# DarknessAPI

Public API for Darkness Anticheat. Allows third-party plugins to create custom punishment animations, listen to anticheat events, and integrate with player checks.

## Installation

### Gradle (Kotlin DSL)

Add the repository in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Or directly in `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.1hendex:DarknessAPI:Tag")
}
```

### Maven

Add the repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.1hendex</groupId>
        <artifactId>DarknessAPI</artifactId>
        <version>Tag</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

Replace `Tag` with the release version you need (for example, `1.0` or `1.0.0`).

---

## Quick Example: Custom Punishment Animation

Extend `AnimationMode` to build an animation, then register it on startup.

```java
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.punishment.ActiveAnimation;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MyCustomAnimation extends AnimationMode {

    // 40 ticks total, onTick every 2 ticks, no levitation (player stays on the ground)
    public MyCustomAnimation() {
        super(40, 2);
    }

    @Override
    public void onStart(ActiveAnimation anim) {
        Player p = anim.getPlayer().getAsPlayer();
        if (p != null) {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        }
    }

    @Override
    public void onTick(ActiveAnimation anim, int step) {
        Player p = anim.getPlayer().getAsPlayer();
        if (p == null) return;

        double radius = 1.5;
        double angle = step * 0.5;
        double px = anim.getX() + Math.cos(angle) * radius;
        double pz = anim.getZ() + Math.sin(angle) * radius;

        p.getWorld().spawnParticle(Particle.FLAME, px, anim.getY() + 0.5, pz, 3, 0.05, 0.05, 0.05, 0.01);
    }

    @Override
    public void onFinish(ActiveAnimation anim) {
        Player p = anim.getPlayer().getAsPlayer();
        if (p != null) {
            p.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, anim.getX(), anim.getY() + 1.0, anim.getZ(), 1);
        }
    }
}
```

Register the animation in your plugin's `onEnable`:

```java
@Override
public void onEnable() {
    DarknessAPI.registerAnimation("MY_ANIMATION", new MyCustomAnimation());
}
```

Darkness runs the animation on its shared timer, prevents anticheat setbacks on the target player while it runs, and executes the configured punishment commands (bans, kicks) when it finishes.

---

## Example Project

A complete, ready-to-run Paper plugin implementation is available in the [DarknessExample](https://github.com/Darkness-Liquidation/DarknessExample) repository.

