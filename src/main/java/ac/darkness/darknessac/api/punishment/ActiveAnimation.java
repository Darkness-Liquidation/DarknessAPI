package ac.darkness.darknessac.api.punishment;

import ac.darkness.darknessac.api.DarknessUser;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The state and controls of a running animation.
 * <p>
 * Passed to {@link AnimationMode#onStart(ActiveAnimation)},
 * {@link AnimationMode#onTick(ActiveAnimation, int)}, and
 * {@link AnimationMode#onFinish(ActiveAnimation)}.
 * <p>
 * Spawning Bukkit particles and sounds:
 * <pre>{@code
 * Player p = org.bukkit.Bukkit.getPlayer(anim.getUser().getUniqueId());
 * if (p != null) {
 *     p.getWorld().spawnParticle(Particle.FLAME, anim.getX(), anim.getY(), anim.getZ(), 10);
 * }
 * }</pre>
 * <p>
 * Sending packets through PacketEvents:
 * <pre>{@code
 * for (DarknessUser viewer : anim.getViewers()) {
 *     PacketEvents.getAPI().getPlayerManager().sendPacket(viewer.getUniqueId(), packet);
 * }
 * }</pre>
 */
public interface ActiveAnimation {

    /**
     * @return The animation mode settings.
     */
    @NotNull AnimationMode getMode();

    /**
     * @return The player being punished.
     */
    @NotNull DarknessUser getUser();

    /**
     * Same as {@link #getUser()}.
     * <p>
     * In Bukkit/Paper, call {@code Bukkit.getPlayer(anim.getUser().getUniqueId())} for the Bukkit Player.
     *
     * @return The player being punished.
     */
    @NotNull default DarknessUser getPlayer() {
        return getUser();
    }

    /**
     * @return Commands scheduled to run when the animation finishes.
     */
    @NotNull List<String> getCommands();

    /**
     * @return Total animation length in ticks.
     */
    int getTicks();

    /**
     * @return Ticks between onTick calls.
     */
    int getInterval();

    /**
     * @return True if levitation packets are enabled.
     */
    boolean isLevitation();

    /**
     * @return Levitation amplifier level, or -1 if disabled.
     */
    int getAmplifier();

    /**
     * @return X coordinate where the animation started.
     */
    double getX();

    /**
     * @return Y coordinate where the animation started.
     */
    double getY();

    /**
     * @return Z coordinate where the animation started.
     */
    double getZ();

    /**
     * @return Current tick counter (starts at 0).
     */
    int getTick();

    /**
     * @return Current step counter (increments every interval ticks).
     */
    int getStep();

    /**
     * Players nearby who can see the animation.
     * <p>
     * Useful for sending packets only to people in range instead of the whole server.
     *
     * @return List of nearby players.
     */
    @NotNull List<? extends DarknessUser> getViewers();

    /**
     * Finishes the animation right now, runs the punishment commands, and cleans up.
     */
    void complete();

    /**
     * Cleans up effects without running punishment commands (for example, if the player leaves).
     */
    void cleanup();
}
