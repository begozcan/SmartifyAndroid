package company.whitespace.smartifyandroid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by begum on 22/12/16.
 */
public class Devices {
    private static List<Device> devices;

    public static List<Device> getDevices() {
        devices = new ArrayList<Device>();

        // TODO: Replace with real values from the server
        devices.add(new Device("Reading Lamp", "Living Room"));
        devices.add(new Device("AC", "Living Room"));
        devices.add(new Device("TV", "Living Room"));
        devices.add(new Device("Computer", "Study Room"));

        return devices;
    }
}
