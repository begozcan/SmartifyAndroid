package company.whitespace.smartifyandroid.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.activity.IRSetupActivity;
import company.whitespace.smartifyandroid.activity.MainActivity;
import company.whitespace.smartifyandroid.model.ConditionalTask;
import company.whitespace.smartifyandroid.model.Device;
import company.whitespace.smartifyandroid.model.ScheduledTask;
import company.whitespace.smartifyandroid.model.Task;
import company.whitespace.smartifyandroid.networking.DeviceAsyncTask;
import company.whitespace.smartifyandroid.networking.GetUpdatesAsyncTask;
import company.whitespace.smartifyandroid.networking.TaskAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static company.whitespace.smartifyandroid.model.Devices.getDevices;
import static company.whitespace.smartifyandroid.model.Tasks.getTasks;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters
    private int deviceId;
    private int taskId;
    private String action;
    private List<Device> devices = new ArrayList<Device>();
    private List<Task> tasks = new ArrayList<Task>();
    private List<String> actions = new ArrayList<String>();
    private String currentRoom;

    private Spinner devicesSpinner;
    private Spinner actionSpinner;
    private TextView time;

    private ToggleButton mon;
    private ToggleButton tue;
    private ToggleButton wed;
    private ToggleButton thu;
    private ToggleButton fri;
    private ToggleButton sat;
    private ToggleButton sun;

    private Button submit;

    private OnFragmentInteractionListener mListener;

    private boolean[] week;

    public AddScheduleFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddScheduleFragment newInstance(String param1, String param2) {
        AddScheduleFragment fragment = new AddScheduleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        devices = getDevices(getContext());
        tasks = getTasks(getContext());
        deviceId = -1;
        taskId = -1;
        action = null;
        // TODO: Add other actions
        actions.add("Select one...");
        actions.add("Turn On/Off");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_schedule, container, false);

        devicesSpinner = (Spinner) view.findViewById(R.id.device_spinner);
        actionSpinner = (Spinner) view.findViewById(R.id.action_spinner);
        time = (TextView) view.findViewById(R.id.time);
        submit = (Button) view.findViewById(R.id.button_submit);

        mon = (ToggleButton) view.findViewById(R.id.mon_button);
        tue = (ToggleButton) view.findViewById(R.id.tue_button);
        wed = (ToggleButton) view.findViewById(R.id.wed_button);
        thu = (ToggleButton) view.findViewById(R.id.thu_button);
        fri = (ToggleButton) view.findViewById(R.id.fri_button);
        sat = (ToggleButton) view.findViewById(R.id.sat_button);
        sun = (ToggleButton) view.findViewById(R.id.sun_button);

        week = new boolean[7];

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.getId() == R.id.mon_button)
                    week[0] = b;
                if (compoundButton.getId() == R.id.tue_button)
                    week[1] = b;
                if (compoundButton.getId() == R.id.wed_button)
                    week[2] = b;
                if (compoundButton.getId() == R.id.thu_button)
                    week[3] = b;
                if (compoundButton.getId() == R.id.fri_button)
                    week[4] = b;
                if (compoundButton.getId() == R.id.sat_button)
                    week[5] = b;
                if (compoundButton.getId() == R.id.sun_button)
                    week[6] = b;
            }
        };
        mon.setOnCheckedChangeListener(onCheckedChangeListener);
        tue.setOnCheckedChangeListener(onCheckedChangeListener);
        wed.setOnCheckedChangeListener(onCheckedChangeListener);
        thu.setOnCheckedChangeListener(onCheckedChangeListener);
        fri.setOnCheckedChangeListener(onCheckedChangeListener);
        sat.setOnCheckedChangeListener(onCheckedChangeListener);
        sun.setOnCheckedChangeListener(onCheckedChangeListener);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getString("device_id") != null)
                deviceId = Integer.parseInt(bundle.getString("device_id"));
            else if (bundle.getString("task_id") != null)
                taskId = Integer.parseInt(bundle.getString("task_id"));
        }

        Log.d("DEVICE_ID", String.valueOf(deviceId));

        List<String> devices_str = toStringList(devices);
        devicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    deviceId = position - 1;
                    currentRoom = devices.get(deviceId).getRoom();
                } else if (position == 0) {
                    deviceId = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                devicesSpinner.setSelection(0);
            }
        });

        ArrayAdapter<String> deviceAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, devices_str);
        deviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devicesSpinner.setAdapter(deviceAdapter);

        actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    action = parent.getItemAtPosition(position).toString();
                } else if (position == 0) {
                    action = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                actionSpinner.setSelection(0);
            }
        });
        ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, actions);
        actionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionSpinner.setAdapter(actionAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeValue = time.getText().toString();
                if (deviceId != -1 && ! timeValue.isEmpty() && week != null &&
                        action != null && !action.isEmpty())
                sendToServer(timeValue, week, devices.get(deviceId).getName(), action);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker = new TimePickerDialog(getActivity(), R.style.AppTheme_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        time.setText(i + ":" + i1);
                    }
                }, 10, 10, true);

                timePicker.show();
            }
        });

        if (deviceId != -1) {
            devicesSpinner.setSelection(deviceId + 1);
        }
        else if (taskId != -1) {
            ScheduledTask task = (ScheduledTask) tasks.get(taskId);
            deviceId = devices_str.indexOf(task.getDeviceName());
            Log.d("ADD CONDITION", String.valueOf(deviceId));
            devicesSpinner.setSelection(deviceId);

            action = task.getActionName();
            actionSpinner.setSelection(actions.indexOf(task.getActionName()));

            time.setText(task.getHour() + ":" + task.getMinute());

            ToggleButton[] w = {mon,tue,wed,thu,fri,sat,sun};
            String weekStr = task.getRepeatdays();
            weekStr = weekStr.replace("[", "");
            String[] weekArr = weekStr.split(", ");

            for (int i = 0; i < weekArr.length; i++) {
                week[i] = Boolean.parseBoolean(weekArr[i]);
                w[i].setChecked(week[i]);
            }
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Convert list to String list for spinner
    public List<String> toStringList(List list) {
        List<String> stringList = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            stringList.add(list.get(i).toString());
        }

        stringList.add(0, "Select one...");
        return stringList;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void sendToServer(String time, boolean[] week, String deviceId, String actionName) {
        JSONArray condition = new JSONArray();
        JSONObject conditionObject = new JSONObject();

        try {
            conditionObject.put("type", "time");
            conditionObject.put("time", time);
            for (int i = 0; i < 7; i++) {
                conditionObject.put("day#" + i, week[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        condition.put(conditionObject);

        JSONObject action = new JSONObject();

        try {
            action.put("Device_ID", deviceId);
            action.put("Action", actionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject data = new JSONObject();

        try {
            data.put("Condition", condition);
            data.put("Action", action);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("Header", "add_command");
            obj.put("Data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray message = new JSONArray();
        message.put(obj);

        Pair<String, String>[] pairs = new Pair[1];
        pairs[0] = new Pair<>("message", message.toString());
        Log.i("Message", message.toString());
        new GetUpdatesAsyncTask(AddScheduleFragment.this).execute(pairs);

        String[] times = time.split(":");
        pairs = new Pair[7];
        pairs[0] = new Pair<>("type", "Scheduled Task");
        pairs[1] = new Pair<>("device_name", deviceId);
        pairs[2] = new Pair<>("room_name", currentRoom);
        pairs[3] = new Pair<>("action_name", actionName);
        pairs[4] = new Pair<>("hour", times[0]);
        pairs[5] = new Pair<>("minute", times[1]);
        pairs[6] = new Pair<>("repeatdays", Arrays.toString(week));
        new TaskAsyncTask(getContext(), "tasks_add").execute(pairs);
    }

    public void onSuccess() {
        MainActivity.CURRENT_TAG = MainActivity.TAG_DEVICES;
        // update the main content by replacing fragments
        Fragment fragment = new DevicesFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, "devices");
        fragmentTransaction.commit();
    }
}
