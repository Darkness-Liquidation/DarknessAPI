package ac.darkness.darknessac.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that on Folia / multi-threaded Paper regions, the annotated method
 * operates on regional Bukkit entity state and must be invoked on the player's region thread.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface RegionThreadOnly {
}
