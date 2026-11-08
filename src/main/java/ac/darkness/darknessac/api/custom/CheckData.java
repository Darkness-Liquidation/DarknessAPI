package ac.darkness.darknessac.api.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckData {
    private final String check;
    private final String type;
    private final String fullCheckName;
    private final double violations;

    public CheckData(String check, String type, String fullCheckName, int violations) {
        this(check, type, fullCheckName, (double) violations);
    }

    public int getVl() {
        return (int) violations;
    }

    public String check() {
        return check;
    }

    public String type() {
        return type;
    }

    public String fullCheckName() {
        return fullCheckName;
    }

    public double violations() {
        return violations;
    }
}
