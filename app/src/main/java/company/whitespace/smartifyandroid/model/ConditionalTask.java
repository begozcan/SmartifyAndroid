package company.whitespace.smartifyandroid.model;

public class ConditionalTask extends Task {
    private String sensorType;
    private double threshold;

    public ConditionalTask(String deviceName, String actionName, String sensorType, double threshold) {
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

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "Do " + getActionName() + " if " + sensorType + " is " + threshold;
    }
}