package company.whitespace.smartifyandroid.model;

/**
 * Created by Alchemistake on 06/04/2017.
 */
public class ScheduledTask extends Task{
    private String hour;
    private String minute;
    private String repeatdays;

    public ScheduledTask(String deviceName, String actionName, String hour, String minute, String repeatdays) {
        super(deviceName, actionName, "Scheduled Task");
        this.hour = hour;
        this.minute = minute;
        this.repeatdays = repeatdays;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getRepeatdays() {
        return repeatdays;
    }

    public void setRepeatdays(String repeatdays) {
        this.repeatdays = repeatdays;
    }

    @Override
    public String toString() {
        return "Do " + getActionName() + " when " + getHour() + ":" + getMinute();
    }
}
