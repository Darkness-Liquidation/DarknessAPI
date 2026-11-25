package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.event.DarknessEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DarknessPunishWaveEvent extends DarknessEvent {
    private final Collection<UUID> playerUUIDs;
    private final Collection<Player> onlinePlayers;

    public DarknessPunishWaveEvent(@NotNull Collection<UUID> playerUUIDs, @NotNull Collection<Player> onlinePlayers) {
        super(false);
        this.playerUUIDs = new ArrayList<>(playerUUIDs);
        this.onlinePlayers = new ArrayList<>(onlinePlayers);
    }

    public DarknessPunishWaveEvent(@NotNull Collection<UUID> playerUUIDs) {
        this(playerUUIDs, Collections.emptyList());
    }

    @NotNull
    public Collection<UUID> getPlayerUUIDs() {
        return Collections.unmodifiableCollection(playerUUIDs);
    }

    @NotNull
    public Collection<Player> getOnlinePlayers() {
        if (onlinePlayers != null && !onlinePlayers.isEmpty()) {
            return Collections.unmodifiableCollection(onlinePlayers);
        }
        List<Player> list = new ArrayList<>();
        for (UUID uuid : playerUUIDs) {
            try {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) list.add(p);
            } catch (Throwable ignored) {}
        }
        return Collections.unmodifiableCollection(list);
    }

    @NotNull
    public Collection<DarknessUser> getUsers() {
        List<DarknessUser> list = new ArrayList<>();
        if (DarknessAPI.INSTANCE != null) {
            DarknessAPI.INSTANCE.getUserManager();
            for (UUID uuid : playerUUIDs) {
                DarknessUser user = DarknessAPI.INSTANCE.getUserManager().getUser(uuid);
                if (user != null) list.add(user);
            }
        }
        return Collections.unmodifiableCollection(list);
    }

    public int getPlayerCount() {
        return playerUUIDs.size();
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
