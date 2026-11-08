package ac.darkness.darknessac.api;

import ac.darkness.darknessac.api.annotation.ThreadSafe;
import ac.darkness.darknessac.api.common.BasicReloadable;
import ac.darkness.darknessac.api.config.ConfigReloadable;
import ac.darkness.darknessac.api.custom.CheckData;
import ac.darkness.darknessac.api.feature.FeatureManager;
import ac.darkness.darknessac.api.handler.ResyncHandler;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@ThreadSafe
public interface DarknessUser extends ConfigReloadable, BasicReloadable, DarknessIdentity {

    String getName();

    String getBrand();

    @Nullable String getWorldName();

    @Nullable UUID getWorldUID();

    int getTransactionPing();

    int getKeepAlivePing();

    String getVersionName();

    default int getProtocolVersion() {
        return 0;
    }

    InetSocketAddress getAddress();

    double getHorizontalSensitivity();

    double getVerticalSensitivity();

    default int getHumanSensitivityX() {
        return (int) Math.round(getHorizontalSensitivity() * 200.0);
    }

    default int getHumanSensitivityY() {
        return (int) Math.round(getVerticalSensitivity() * 200.0);
    }

    default TrustLevel getTrustLevel() {
        return TrustLevel.GREEN;
    }

    default int getTrustScore() {
        return 100;
    }

    default int getTrustPoints() {
        return 0;
    }

    default double getX() {
        return 0.0;
    }

    default double getY() {
        return 0.0;
    }

    default double getZ() {
        return 0.0;
    }

    default float getYaw() {
        return 0.0f;
    }

    default float getPitch() {
        return 0.0f;
    }

    boolean isVanillaMath();

    void updatePermissions();

    Collection<? extends AbstractCheck> getChecks();

    @Nullable
    default AbstractCheck getCheck(String name) {
        if (name == null) return null;
        for (AbstractCheck check : getChecks()) {
            if (name.equalsIgnoreCase(check.getCheckName()) || name.equalsIgnoreCase(check.getAlternativeName())) {
                return check;
            }
        }
        return null;
    }

    @Nullable
    default AbstractCheck getCheck(String check, String type) {
        if (check == null) return null;
        String full = (type == null || type.isEmpty()) ? check : check + type;
        return getCheck(full);
    }

    void runSafely(Runnable runnable);

    int getLastTransactionReceived();

    int getLastTransactionSent();

    void addRealTimeTask(int transaction, Runnable runnable);

    default void addRealTimeTaskNow(Runnable runnable) {
        addRealTimeTask(getLastTransactionSent(), runnable);
    }

    default void addRealTimeTaskNext(Runnable runnable) {
        addRealTimeTask(getLastTransactionSent() + 1, runnable);
    }

    FeatureManager getFeatureManager();

    default ResyncHandler getResyncHandler() {
        return null;
    }

    default void setResyncHandler(ResyncHandler handler) {}

    void sendMessage(String message);

    boolean hasPermission(String permission);

    default boolean isEnabled() {
        return true;
    }

    default void setTempBypass(long seconds) {}

    default void setTempBypassTicks(long ticks) {
        setTempBypass(Math.max(1L, ticks / 20L));
    }

    default void setTempBypass(String checkName, long seconds) {
        setTempBypassTicks(checkName, seconds * 20L);
    }

    default void setTempBypassTicks(String checkName, long ticks) {
        AbstractCheck check = getCheck(checkName);
        if (check != null) {
            check.setTempBypassTicks(ticks);
        }
    }

    default void setCategoryTempBypass(String category, long seconds) {
        setCategoryTempBypassTicks(category, seconds * 20L);
    }

    default void setCategoryTempBypassTicks(String category, long ticks) {
        if (category == null) return;
        for (AbstractCheck check : getChecks()) {
            if (category.equalsIgnoreCase(check.getCategory())) {
                check.setTempBypassTicks(ticks);
            }
        }
    }

    default boolean isBypassing() {
        return false;
    }

    default boolean isBypassing(String checkName) {
        if (isBypassing()) return true;
        AbstractCheck check = getCheck(checkName);
        return check != null && check.isBypassing();
    }

    default boolean isTempBypassing() {
        return false;
    }

    default void resetBypass() {
        setTempBypass(0);
    }

    default double getTotalViolations() {
        double total = 0.0;
        for (AbstractCheck check : getChecks()) {
            total += check.getViolations();
        }
        return total;
    }

    default double getViolations(String checkName) {
        AbstractCheck check = getCheck(checkName);
        return check != null ? check.getViolations() : 0.0;
    }

    default double getViolations(String check, String type) {
        AbstractCheck c = getCheck(check, type);
        return c != null ? c.getViolations() : 0.0;
    }

    default void setViolations(String check, String type, double vl) {
        AbstractCheck c = getCheck(check, type);
        if (c != null) {
            c.setViolations(vl);
        }
    }

    default double getCombatViolations() {
        return 0.0;
    }

    default double getMovementViolations() {
        return 0.0;
    }

    default double getWorldViolations() {
        return 0.0;
    }

    default Collection<CheckData> getCheckData() {
        List<CheckData> list = new ArrayList<>();
        for (AbstractCheck c : getChecks()) {
            String fullName = c.getCheckName();
            String name = fullName;
            String type = "";
            if (fullName.length() > 1 && Character.isUpperCase(fullName.charAt(fullName.length() - 1))) {
                name = fullName.substring(0, fullName.length() - 1);
                type = fullName.substring(fullName.length() - 1);
            }
            list.add(new CheckData(name, type, fullName, c.getViolations()));
        }
        return list;
    }

    default boolean resetViolations(String check) {
        AbstractCheck c = getCheck(check);
        if (c != null) {
            c.resetViolations();
            return true;
        }
        return false;
    }

    default boolean resetViolations(String check, String type) {
        AbstractCheck c = getCheck(check, type);
        if (c != null) {
            c.resetViolations();
            return true;
        }
        return false;
    }

    default void resetAllViolations() {
        for (AbstractCheck check : getChecks()) {
            check.resetViolations();
        }
    }

    default boolean flag(String checkName, String verbose) {
        AbstractCheck check = getCheck(checkName);
        return check != null && check.flag(verbose);
    }

    default boolean flag(String check, String type, String verbose) {
        String full = (type == null || type.isEmpty()) ? check : check + type;
        return flag(full, verbose);
    }

    default boolean flag(String checkName) {
        return flag(checkName, "");
    }
    default void punish(String reason) {
        if (DarknessAPI.INSTANCE != null) {
            DarknessAPI.INSTANCE.punish(getUniqueId(), reason);
        } else if (DarknessAPIProvider.isInitialized()) {
            DarknessAPIProvider.get().punish(getUniqueId(), reason);
        }
    }
}
