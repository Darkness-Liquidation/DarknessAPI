package ac.darkness.darknessac.api.custom;

public class CheckData {
    private final String check;
    private final String type;
    private final String fullCheckName;
    private final double violations;

    public CheckData(String check, String type, String fullCheckName, double violations) {
        this.check = check;
        this.type = type;
        this.fullCheckName = fullCheckName;
        this.violations = violations;
    }

    public String getCheck() {
        return check;
    }

    public String getType() {
        return type;
    }

    public String getFullCheckName() {
        return fullCheckName;
    }

    public double getViolations() {
        return violations;
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
