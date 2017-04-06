package company.whitespace.smartifyandroid.fragment;

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
import company.whitespace.smartifyandroid.activity.MainActivity;
import company.whitespace.smartifyandroid.model.Device;
import company.whitespace.smartifyandroid.networking.DeviceAsyncTask;
import company.whitespace.smartifyandroid.networking.GetUpdatesAsyncTask;
import company.whitespace.smartifyandroid.networking.TaskAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static company.whitespace.smartifyandroid.model.Devices.getDevices;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddConditionFragment extends Fragment {
    private int deviceId;
    private String action;
    private String condition;

    private List<Device> devices = new ArrayList<Device>();
    private List<String> actions = new ArrayList<String>();
    private List<String> conditions = new ArrayList<String>();

    private Spinner devicesSpinner;
    private Spinner actionSpinner;
    private Spinner conditionSpinner;
    private EditText value;
    private Button submit;

    private OnFragmentInteractionListener mListener;

    public AddConditionFragment() {
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
        devices  = getDevices(getContext());

        deviceId = -1;
        action = null;
        condition = null;

        // TODO: Add other actions
        actions.add("Select one...");
        actions.add("Turn On/Off");

        conditions.add("Select one...");
        conditions.add("Temperature");
        conditions.add("Light");
        conditions.add("Humidity");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_condition, container, false);

        devicesSpinner = (Spinner) view.findViewById(R.id.device_spinner);
        actionSpinner = (Spinner) view.findViewById(R.id.action_spinner);
        conditionSpinner = (Spinner) view.findViewById(R.id.condition_spinner);
        value = (EditText) view.findViewById(R.id.value);
        submit = (Button) view.findViewById(R.id.button_submit);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            deviceId = Integer.parseInt(bundle.getString("device_id"));
        }

        List<String> devices_str = toStringList(devices);
        devicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 if (position > 0) {
                     deviceId = position - 1;
                 }
                 else if (position == 0) {
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

        if (deviceId != -1) {
            devicesSpinner.setSelection(deviceId + 1);
        }

        actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    action = parent.getItemAtPosition(position).toString();
                }
                else if (position == 0) {
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

        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    condition = parent.getItemAtPosition(position).toString();
                }
                else if (position == 0) {
                    condition = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                conditionSpinner.setSelection(0);
            }
        });

        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, conditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Null check
                sendToServer(condition, value.getText().toString(), devices.get(deviceId).getName(), action);
            }
        });

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

        for (int i = 0; i < list.size() ; i++) {
            stringList.add(list.get(i).toString());
        }

        stringList.add(0, "Select one...");
        return stringList;
    }

    private void sendToServer(String conditionType, String value, String deviceId, String actionName) {
        JSONArray condition = new JSONArray();
        JSONObject conditionObject = new JSONObject();

        try {
            conditionObject.put("type", "condition");
            conditionObject.put("Condition_Type", conditionType);
            conditionObject.put("Value", value);
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
        new GetUpdatesAsyncTask(AddConditionFragment.this).execute(pairs);

        pairs = new Pair[5];
        pairs[0] = new Pair<>("type", "Conditional Task");
        pairs[1] = new Pair<>("device_name", deviceId);
        pairs[2] = new Pair<>("action_name", actionName);
        pairs[3] = new Pair<>("sensor_type", conditionType);
        pairs[4] = new Pair<>("threshold", value);
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
}
