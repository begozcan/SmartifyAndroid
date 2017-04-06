package company.whitespace.smartifyandroid.model;

/**
 * Created by Alchemistake on 31/03/2017.
 */
public abstract class Task {
    private String deviceName;
    private String actionName;
    private String type;

    public Task(String deviceName, String actionName, String type) {
        this.deviceName = deviceName;
        this.actionName = actionName;
        this.type = type;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
