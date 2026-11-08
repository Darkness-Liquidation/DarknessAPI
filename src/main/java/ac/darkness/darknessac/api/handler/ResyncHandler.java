package ac.darkness.darknessac.api.handler;

public interface ResyncHandler {
    void resync(int minBlockX, int minBlockY, int minBlockZ, int maxBlockX, int maxBlockY, int maxBlockZ);

    void resyncPosition(int x, int y, int z, int sequence);
}