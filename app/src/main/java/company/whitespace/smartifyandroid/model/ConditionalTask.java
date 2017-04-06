package company.whitespace.smartifyandroid.model;

public class ConditionalTask extends Task {
    private String sensorType;
    private String threshold;

    public ConditionalTask(String deviceName, String actionName, String sensorType, String threshold) {
        super(deviceName, actionName, "Conditional Task");
        this.sensorType = sensorType;
        this.threshold = threshold;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "Do " + getActionName() + " if " + sensorType + " is " + threshold;
    }
}