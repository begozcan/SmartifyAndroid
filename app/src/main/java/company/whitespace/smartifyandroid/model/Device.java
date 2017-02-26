package company.whitespace.smartifyandroid.model;

/**
 * Created by begum on 09/12/16.
 */
public class Device {

    private String name;
    private String room;
    private String type;

    public Device(String name, String room) {
        this.name = name;
        this.room = room;
        this.type = null;
    }

    public Device(String name, String room, String type) {
        this.name = name;
        this.room = room;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getRoom() {
        return room;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return name + " @ " + room;
    }

}
