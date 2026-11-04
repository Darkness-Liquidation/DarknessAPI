package ac.darkness.darknessac.api.punishment;

import ac.darkness.darknessac.api.DarknessUser;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Base class for custom punishment animations.
 * <p>
 * Extend this class to make your own punishment modes (like flame circles,
 * lightning strikes, or floating effects).
 * <p>
 * How it works:
 * <ul>
 *   <li>Darkness runs all active animations on one shared server task.</li>
 *   <li>Callbacks ({@link #onStart}, {@link #onTick}, {@link #onFinish}) run on the server tick thread,
 *       so you can freely use Bukkit API (spawning particles, sounds) or send packets via PacketEvents.</li>
 *   <li>Anticheat setbacks and movement checks are automatically paused for the target player.</li>
 *   <li>When the animation finishes, punishment commands (ban, kick, etc.) run automatically.</li>
 * </ul>
 * <p>
 * To register your animation:
 * <pre>{@code
 * DarknessAPI.registerAnimation("MY_MODE", new MyCustomMode());
 * }</pre>
 * <p>
 * See the full working example at: <a href="https://github.com/Darkness-Liquidation/DarknessExample">DarknessExample</a>
 */
@Getter
public abstract class AnimationMode implements PunishAnimation {

    protected final int ticks;
    protected final int interval;
    protected final boolean levitation;
    protected final int amplifier;

    /**
     * Creates an animation with levitation.
     *
     * @param ticks Total runtime in ticks.
     * @param interval Ticks between {@link #onTick(ActiveAnimation, int)} calls (1 = every tick, 2 = every 2 ticks).
     * @param levitation True to float the player with levitation packets.
     * @param amplifier Potion amplifier for levitation (e.g. 1, 2), or -1 for default.
     */
    public AnimationMode(int ticks, int interval, boolean levitation, int amplifier) {
        this.ticks = ticks;
        this.interval = Math.max(1, interval);
        this.levitation = levitation;
        this.amplifier = amplifier;
    }

    /**
     * Creates an animation without levitation.
     * <p>
     * The player stays on the ground (like the FLAME animation).
     *
     * @param ticks Total runtime in ticks.
     * @param interval Ticks between {@link #onTick(ActiveAnimation, int)} calls.
     */
    public AnimationMode(int ticks, int interval) {
        this(ticks, interval, false, -1);
    }

    @Override
    public void execute(@NotNull DarknessUser user, @NotNull List<String> commands) {
        PunishAnimationManager.AnimationExecutor executor = PunishAnimationManager.getExecutor();
        if (executor != null) {
            executor.execute(this, user, commands);
        }
    }

    @Override
    public void execute(@NotNull Object user, @NotNull List<String> commands) {
        if (user instanceof DarknessUser) {
            execute((DarknessUser) user, commands);
        }
    }

    /**
     * Called once when the animation starts. Good place for sounds or opening effects.
     *
     * @param anim The active animation context.
     */
    public void onStart(@NotNull ActiveAnimation anim) {}

    /**
     * Called every interval ticks while the animation runs.
     *
     * @param anim The active animation context.
     * @param step Step counter (0, 1, 2...) that increments every interval ticks.
     */
    public void onTick(@NotNull ActiveAnimation anim, int step) {}

    /**
     * Called once when the animation ends, right before punishment commands run.
     *
     * @param anim The active animation context.
     */
    public void onFinish(@NotNull ActiveAnimation anim) {}
}
