package ac.darkness.darknessac.api;

import ac.darkness.darknessac.api.common.BasicReloadable;

/**
 * Base processor contract for Darkness analysis and detection pipelines.
 */
public interface AbstractProcessor extends BasicReloadable {

    /**
     * The configuration key identifying this processor.
     */
    String getConfigName();
}


