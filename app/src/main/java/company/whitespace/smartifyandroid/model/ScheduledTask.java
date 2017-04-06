package company.whitespace.smartifyandroid.model;

/**
 * Created by Alchemistake on 06/04/2017.
 */
public class ScheduledTask extends Task{
    private int hour;
    private int minute;
    private boolean[] repeatdays;

    public ScheduledTask(String deviceName, String actionName, int hour, int minute, boolean[] repeatdays) {
        super(deviceName, actionName, "Scheduled Task");
        this.hour = hour;
        this.minute = minute;
        this.repeatdays = repeatdays;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean[] getRepeatdays() {
        return repeatdays;
    }

    public void setRepeatdays(boolean[] repeatdays) {
        this.repeatdays = repeatdays;
    }

    @Override
    public String toString() {
        return "Do " + getActionName() + " when " + getHour() + ":" + getMinute();
    }
}
