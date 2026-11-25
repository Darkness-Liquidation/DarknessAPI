package ac.darkness.darknessac.api.managers;

import ac.darkness.darknessac.api.CheckCategory;
import ac.darkness.darknessac.api.CheckType;
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessIdentity;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.annotation.ThreadSafe;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * Dedicated manager for global, category-specific, and check-specific temporary bypasses.
 * All duration methods safely recalculate expiration without unbounded stacking.
 */
@ThreadSafe
public interface BypassManager {

    void setTempBypass(@NotNull UUID uuid, long seconds);

    default void setTempBypass(@NotNull Player player, long seconds) {
        setTempBypass(player.getUniqueId(), seconds);
    }

    default void setTempBypass(@NotNull DarknessIdentity identity, long seconds) {
        setTempBypass(identity.getUniqueId(), seconds);
    }

    void setTempBypassTicks(@NotNull UUID uuid, long ticks);

    default void setTempBypassTicks(@NotNull Player player, long ticks) {
        setTempBypassTicks(player.getUniqueId(), ticks);
    }

    default void setTempBypassTicks(@NotNull DarknessIdentity identity, long ticks) {
        setTempBypassTicks(identity.getUniqueId(), ticks);
    }

    void setTempBypass(@NotNull UUID uuid, @NotNull CheckType checkType, long seconds);

    default void setTempBypass(@NotNull Player player, @NotNull CheckType checkType, long seconds) {
        setTempBypass(player.getUniqueId(), checkType, seconds);
    }

    default void setTempBypass(@NotNull DarknessIdentity identity, @NotNull CheckType checkType, long seconds) {
        setTempBypass(identity.getUniqueId(), checkType, seconds);
    }

    void setTempBypassTicks(@NotNull UUID uuid, @NotNull CheckType checkType, long ticks);

    default void setTempBypassTicks(@NotNull Player player, @NotNull CheckType checkType, long ticks) {
        setTempBypassTicks(player.getUniqueId(), checkType, ticks);
    }

    default void setTempBypassTicks(@NotNull DarknessIdentity identity, @NotNull CheckType checkType, long ticks) {
        setTempBypassTicks(identity.getUniqueId(), checkType, ticks);
    }

    void setCategoryTempBypass(@NotNull UUID uuid, @NotNull CheckCategory category, long seconds);

    default void setCategoryTempBypass(@NotNull Player player, @NotNull CheckCategory category, long seconds) {
        setCategoryTempBypass(player.getUniqueId(), category, seconds);
    }

    default void setCategoryTempBypass(@NotNull DarknessIdentity identity, @NotNull CheckCategory category, long seconds) {
        setCategoryTempBypass(identity.getUniqueId(), category, seconds);
    }

    void setCategoryTempBypassTicks(@NotNull UUID uuid, @NotNull CheckCategory category, long ticks);

    default void setCategoryTempBypassTicks(@NotNull Player player, @NotNull CheckCategory category, long ticks) {
        setCategoryTempBypassTicks(player.getUniqueId(), category, ticks);
    }

    default void setCategoryTempBypassTicks(@NotNull DarknessIdentity identity, @NotNull CheckCategory category, long ticks) {
        setCategoryTempBypassTicks(identity.getUniqueId(), category, ticks);
    }

    void setTempBypass(@NotNull UUID uuid, @NotNull String checkName, long seconds);

    default void setTempBypass(@NotNull Player player, @NotNull String checkName, long seconds) {
        setTempBypass(player.getUniqueId(), checkName, seconds);
    }

    default void setTempBypass(@NotNull DarknessIdentity identity, @NotNull String checkName, long seconds) {
        setTempBypass(identity.getUniqueId(), checkName, seconds);
    }

    void setTempBypassTicks(@NotNull UUID uuid, @NotNull String checkName, long ticks);

    default void setTempBypassTicks(@NotNull Player player, @NotNull String checkName, long ticks) {
        setTempBypassTicks(player.getUniqueId(), checkName, ticks);
    }

    default void setTempBypassTicks(@NotNull DarknessIdentity identity, @NotNull String checkName, long ticks) {
        setTempBypassTicks(identity.getUniqueId(), checkName, ticks);
    }

    void setCheckEnabled(@NotNull UUID uuid, @NotNull String checkName, boolean enabled);

    default void setCheckEnabled(@NotNull Player player, @NotNull String checkName, boolean enabled) {
        setCheckEnabled(player.getUniqueId(), checkName, enabled);
    }

    default void setCheckEnabled(@NotNull DarknessIdentity identity, @NotNull String checkName, boolean enabled) {
        setCheckEnabled(identity.getUniqueId(), checkName, enabled);
    }

    boolean isBypassing(@NotNull UUID uuid);

    default boolean isBypassing(@NotNull Player player) {
        return isBypassing(player.getUniqueId()) || isPredicateBypassing(player);
    }

    default boolean isBypassing(@NotNull DarknessIdentity identity) {
        return isBypassing(identity.getUniqueId());
    }

    boolean isBypassing(@NotNull UUID uuid, @NotNull CheckType checkType);

    default boolean isBypassing(@NotNull Player player, @NotNull CheckType checkType) {
        return isBypassing(player.getUniqueId(), checkType) || isPredicateBypassing(player, checkType);
    }

    default boolean isBypassing(@NotNull DarknessIdentity identity, @NotNull CheckType checkType) {
        return isBypassing(identity.getUniqueId(), checkType);
    }

    boolean isBypassing(@NotNull UUID uuid, @NotNull CheckCategory category);

    default boolean isBypassing(@NotNull Player player, @NotNull CheckCategory category) {
        return isBypassing(player.getUniqueId(), category) || isPredicateBypassing(player, category);
    }

    default boolean isBypassing(@NotNull DarknessIdentity identity, @NotNull CheckCategory category) {
        return isBypassing(identity.getUniqueId(), category);
    }

    boolean isBypassing(@NotNull UUID uuid, @NotNull String checkName);

    default boolean isBypassing(@NotNull Player player, @NotNull String checkName) {
        return isBypassing(player.getUniqueId(), checkName);
    }

    default boolean isBypassing(@NotNull DarknessIdentity identity, @NotNull String checkName) {
        return isBypassing(identity.getUniqueId(), checkName);
    }

    void resetBypass(@NotNull UUID uuid);

    default void resetBypass(@NotNull Player player) {
        resetBypass(player.getUniqueId());
    }

    default void resetBypass(@NotNull DarknessIdentity identity) {
        resetBypass(identity.getUniqueId());
    }

    // ------------------- DYNAMIC BYPASS PREDICATES -------------------

    /**
     * Registers a dynamic condition-based bypass for an entire check category.
     * When the predicate evaluates to true for a given player, checks in that category will bypass.
     *
     * @param category category to bypass when condition holds
     * @param predicate condition returning true if player should bypass
     */
    default void registerBypassPredicate(@NotNull CheckCategory category, @NotNull Predicate<Player> predicate) {
        PredicateHolder.CATEGORY.computeIfAbsent(category, k -> new CopyOnWriteArrayList<>()).add(predicate);
    }

    default void unregisterBypassPredicate(@NotNull CheckCategory category, @NotNull Predicate<Player> predicate) {
        List<Predicate<Player>> list = PredicateHolder.CATEGORY.get(category);
        if (list != null) list.remove(predicate);
    }

    default void registerUserBypassPredicate(@NotNull CheckCategory category, @NotNull Predicate<DarknessUser> predicate) {
        PredicateHolder.CATEGORY_USER.computeIfAbsent(category, k -> new CopyOnWriteArrayList<>()).add(predicate);
    }

    default void unregisterUserBypassPredicate(@NotNull CheckCategory category, @NotNull Predicate<DarknessUser> predicate) {
        List<Predicate<DarknessUser>> list = PredicateHolder.CATEGORY_USER.get(category);
        if (list != null) list.remove(predicate);
    }

    default void registerUUIDBypassPredicate(@NotNull CheckCategory category, @NotNull Predicate<UUID> predicate) {
        PredicateHolder.CATEGORY_UUID.computeIfAbsent(category, k -> new CopyOnWriteArrayList<>()).add(predicate);
    }

    default void unregisterUUIDBypassPredicate(@NotNull CheckCategory category, @NotNull Predicate<UUID> predicate) {
        List<Predicate<UUID>> list = PredicateHolder.CATEGORY_UUID.get(category);
        if (list != null) list.remove(predicate);
    }

    default void registerBypassPredicate(@NotNull CheckType checkType, @NotNull Predicate<Player> predicate) {
        PredicateHolder.CHECK_TYPE.computeIfAbsent(checkType, k -> new CopyOnWriteArrayList<>()).add(predicate);
    }

    default void unregisterBypassPredicate(@NotNull CheckType checkType, @NotNull Predicate<Player> predicate) {
        List<Predicate<Player>> list = PredicateHolder.CHECK_TYPE.get(checkType);
        if (list != null) list.remove(predicate);
    }

    default void registerUserBypassPredicate(@NotNull CheckType checkType, @NotNull Predicate<DarknessUser> predicate) {
        PredicateHolder.CHECK_TYPE_USER.computeIfAbsent(checkType, k -> new CopyOnWriteArrayList<>()).add(predicate);
    }

    default void unregisterUserBypassPredicate(@NotNull CheckType checkType, @NotNull Predicate<DarknessUser> predicate) {
        List<Predicate<DarknessUser>> list = PredicateHolder.CHECK_TYPE_USER.get(checkType);
        if (list != null) list.remove(predicate);
    }

    default void registerUUIDBypassPredicate(@NotNull CheckType checkType, @NotNull Predicate<UUID> predicate) {
        PredicateHolder.CHECK_TYPE_UUID.computeIfAbsent(checkType, k -> new CopyOnWriteArrayList<>()).add(predicate);
    }

    default void unregisterUUIDBypassPredicate(@NotNull CheckType checkType, @NotNull Predicate<UUID> predicate) {
        List<Predicate<UUID>> list = PredicateHolder.CHECK_TYPE_UUID.get(checkType);
        if (list != null) list.remove(predicate);
    }

    default void registerBypassPredicate(@NotNull Predicate<Player> predicate) {
        PredicateHolder.GLOBAL.add(predicate);
    }

    default void unregisterBypassPredicate(@NotNull Predicate<Player> predicate) {
        PredicateHolder.GLOBAL.remove(predicate);
    }

    default void registerUserBypassPredicate(@NotNull Predicate<DarknessUser> predicate) {
        PredicateHolder.GLOBAL_USER.add(predicate);
    }

    default void unregisterUserBypassPredicate(@NotNull Predicate<DarknessUser> predicate) {
        PredicateHolder.GLOBAL_USER.remove(predicate);
    }

    default void registerUUIDBypassPredicate(@NotNull Predicate<UUID> predicate) {
        PredicateHolder.GLOBAL_UUID.add(predicate);
    }

    default void unregisterUUIDBypassPredicate(@NotNull Predicate<UUID> predicate) {
        PredicateHolder.GLOBAL_UUID.remove(predicate);
    }

    default boolean isPredicateBypassing(@NotNull UUID uuid) {
        if (!PredicateHolder.GLOBAL_UUID.isEmpty()) {
            for (Predicate<UUID> pred : PredicateHolder.GLOBAL_UUID) {
                try {
                    if (pred.test(uuid)) return true;
                } catch (Throwable ignored) {}
            }
        }
        if (!PredicateHolder.GLOBAL_USER.isEmpty()) {
            DarknessUser user = DarknessAPI.INSTANCE != null ? DarknessAPI.INSTANCE.getUser(uuid) : DarknessAPI.getUser(uuid);
            if (user != null) {
                for (Predicate<DarknessUser> pred : PredicateHolder.GLOBAL_USER) {
                    try {
                        if (pred.test(user)) return true;
                    } catch (Throwable ignored) {}
                }
            }
        }
        if (!PredicateHolder.GLOBAL.isEmpty()) {
            try {
                Player player = org.bukkit.Bukkit.getPlayer(uuid);
                if (player != null) {
                    for (Predicate<Player> pred : PredicateHolder.GLOBAL) {
                        try {
                            if (pred.test(player)) return true;
                        } catch (Throwable ignored) {}
                    }
                }
            } catch (Throwable ignored) {}
        }
        return false;
    }

    default boolean isPredicateBypassing(@NotNull UUID uuid, @NotNull CheckCategory category) {
        if (isPredicateBypassing(uuid)) return true;
        List<Predicate<UUID>> uuidList = PredicateHolder.CATEGORY_UUID.get(category);
        if (uuidList != null) {
            for (Predicate<UUID> pred : uuidList) {
                try {
                    if (pred.test(uuid)) return true;
                } catch (Throwable ignored) {}
            }
        }
        List<Predicate<DarknessUser>> userList = PredicateHolder.CATEGORY_USER.get(category);
        if (userList != null && !userList.isEmpty()) {
            DarknessUser user = DarknessAPI.INSTANCE != null ? DarknessAPI.INSTANCE.getUser(uuid) : DarknessAPI.getUser(uuid);
            if (user != null) {
                for (Predicate<DarknessUser> pred : userList) {
                    try {
                        if (pred.test(user)) return true;
                    } catch (Throwable ignored) {}
                }
            }
        }
        List<Predicate<Player>> playerList = PredicateHolder.CATEGORY.get(category);
        if (playerList != null && !playerList.isEmpty()) {
            try {
                Player player = org.bukkit.Bukkit.getPlayer(uuid);
                if (player != null) {
                    for (Predicate<Player> pred : playerList) {
                        try {
                            if (pred.test(player)) return true;
                        } catch (Throwable ignored) {}
                    }
                }
            } catch (Throwable ignored) {}
        }
        return false;
    }

    default boolean isPredicateBypassing(@NotNull UUID uuid, @NotNull CheckType checkType) {
        if (isPredicateBypassing(uuid, checkType.category())) return true;
        List<Predicate<UUID>> uuidList = PredicateHolder.CHECK_TYPE_UUID.get(checkType);
        if (uuidList != null) {
            for (Predicate<UUID> pred : uuidList) {
                try {
                    if (pred.test(uuid)) return true;
                } catch (Throwable ignored) {}
            }
        }
        List<Predicate<DarknessUser>> userList = PredicateHolder.CHECK_TYPE_USER.get(checkType);
        if (userList != null && !userList.isEmpty()) {
            DarknessUser user = DarknessAPI.INSTANCE != null ? DarknessAPI.INSTANCE.getUser(uuid) : DarknessAPI.getUser(uuid);
            if (user != null) {
                for (Predicate<DarknessUser> pred : userList) {
                    try {
                        if (pred.test(user)) return true;
                    } catch (Throwable ignored) {}
                }
            }
        }
        List<Predicate<Player>> playerList = PredicateHolder.CHECK_TYPE.get(checkType);
        if (playerList != null && !playerList.isEmpty()) {
            try {
                Player player = org.bukkit.Bukkit.getPlayer(uuid);
                if (player != null) {
                    for (Predicate<Player> pred : playerList) {
                        try {
                            if (pred.test(player)) return true;
                        } catch (Throwable ignored) {}
                    }
                }
            } catch (Throwable ignored) {}
        }
        return false;
    }

    default boolean isPredicateBypassing(@NotNull DarknessUser user) {
        return isPredicateBypassing(user.getUniqueId());
    }

    default boolean isPredicateBypassing(@NotNull DarknessUser user, @NotNull CheckCategory category) {
        return isPredicateBypassing(user.getUniqueId(), category);
    }

    default boolean isPredicateBypassing(@NotNull DarknessUser user, @NotNull CheckType checkType) {
        return isPredicateBypassing(user.getUniqueId(), checkType);
    }

    default boolean isPredicateBypassing(@NotNull Player player) {
        for (Predicate<Player> pred : PredicateHolder.GLOBAL) {
            try {
                if (pred.test(player)) return true;
            } catch (Throwable ignored) {}
        }
        return isPredicateBypassing(player.getUniqueId());
    }

    default boolean isPredicateBypassing(@NotNull Player player, @NotNull CheckCategory category) {
        if (isPredicateBypassing(player)) return true;
        List<Predicate<Player>> list = PredicateHolder.CATEGORY.get(category);
        if (list != null) {
            for (Predicate<Player> pred : list) {
                try {
                    if (pred.test(player)) return true;
                } catch (Throwable ignored) {}
            }
        }
        return isPredicateBypassing(player.getUniqueId(), category);
    }

    default boolean isPredicateBypassing(@NotNull Player player, @NotNull CheckType checkType) {
        if (isPredicateBypassing(player, checkType.category())) return true;
        List<Predicate<Player>> list = PredicateHolder.CHECK_TYPE.get(checkType);
        if (list != null) {
            for (Predicate<Player> pred : list) {
                try {
                    if (pred.test(player)) return true;
                } catch (Throwable ignored) {}
            }
        }
        return isPredicateBypassing(player.getUniqueId(), checkType);
    }

    class PredicateHolder {
        public static final List<Predicate<Player>> GLOBAL = new CopyOnWriteArrayList<>();
        public static final Map<CheckCategory, List<Predicate<Player>>> CATEGORY = new ConcurrentHashMap<>();
        public static final Map<CheckType, List<Predicate<Player>>> CHECK_TYPE = new ConcurrentHashMap<>();

        public static final List<Predicate<DarknessUser>> GLOBAL_USER = new CopyOnWriteArrayList<>();
        public static final Map<CheckCategory, List<Predicate<DarknessUser>>> CATEGORY_USER = new ConcurrentHashMap<>();
        public static final Map<CheckType, List<Predicate<DarknessUser>>> CHECK_TYPE_USER = new ConcurrentHashMap<>();

        public static final List<Predicate<UUID>> GLOBAL_UUID = new CopyOnWriteArrayList<>();
        public static final Map<CheckCategory, List<Predicate<UUID>>> CATEGORY_UUID = new ConcurrentHashMap<>();
        public static final Map<CheckType, List<Predicate<UUID>>> CHECK_TYPE_UUID = new ConcurrentHashMap<>();
    }
}
