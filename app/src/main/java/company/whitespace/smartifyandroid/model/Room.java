package company.whitespace.smartifyandroid.model;

/**
 * Created by begum on 16/12/16.
 */
public class Room {
    private String name;
    private String light;
    private int temperature;
    private int humidity;

    public Room(String name, String light, int temperature, int humidity) {
        this.name = name;
        this.light = light;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getName() {
        return name;
    }

    public String getLight() {
        return light;
    }

    public String getTemperature() {
        return String.valueOf(temperature);
    }

    public String getHumidity() {
        return String.valueOf(humidity);
    }

    public void setLight(String light) {
        this.light = light;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return name;
    }
}
