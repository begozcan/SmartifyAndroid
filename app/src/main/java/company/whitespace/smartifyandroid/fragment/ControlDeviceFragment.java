package company.whitespace.smartifyandroid.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.model.Device;
import company.whitespace.smartifyandroid.networking.GetUpdatesAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static company.whitespace.smartifyandroid.model.Devices.getDevices;

public class ControlDeviceFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner devicesSpinner;
    private RelativeLayout remoteButtons;
    private TextView screen;
    private Button powerButton;
    private Button deleteButton;
    private Button okButton;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button0;

    private List<Device> devices = new ArrayList<Device>();
    private OnFragmentInteractionListener mListener;
    private int deviceId;
    private StringBuilder channelNo = new StringBuilder("");

    public ControlDeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ControlDeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static ControlDeviceFragment newInstance() {
//        ControlDeviceFragment fragment = new ControlDeviceFragment();
//        //Bundle args = new Bundle();
//        //fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        devices  = getDevices();
        deviceId = -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control_device, container, false);

        devicesSpinner = (Spinner) view.findViewById(R.id.device_spinner);
        remoteButtons = (RelativeLayout) view.findViewById(R.id.remote_buttons);
        screen = (TextView) view.findViewById(R.id.screen);
        powerButton = (Button) view.findViewById(R.id.power_button);
        deleteButton = (Button) view.findViewById(R.id.button_delete);
        okButton = (Button) view.findViewById(R.id.button_ok);
        // Numpad
        button1 = (Button) view.findViewById(R.id.button_1);
        button2 = (Button) view.findViewById(R.id.button_2);
        button3 = (Button) view.findViewById(R.id.button_3);
        button4 = (Button) view.findViewById(R.id.button_4);
        button5 = (Button) view.findViewById(R.id.button_5);
        button6 = (Button) view.findViewById(R.id.button_6);
        button7 = (Button) view.findViewById(R.id.button_7);
        button8 = (Button) view.findViewById(R.id.button_8);
        button9 = (Button) view.findViewById(R.id.button_9);
        button0 = (Button) view.findViewById(R.id.button_0);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            deviceId = Integer.parseInt(bundle.getString("device_id"));
        }

        Log.d("DEVICE_ID", String.valueOf(deviceId));

        List<String> devices_str = toStringList(devices);
        devicesSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, devices_str);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devicesSpinner.setAdapter(dataAdapter);

        if (deviceId != -1) {
            devicesSpinner.setSelection(deviceId + 1);
        }

        powerButton.setOnClickListener(new buttonOnClickListener());
        deleteButton.setOnClickListener(new buttonOnClickListener());
        okButton.setOnClickListener(new buttonOnClickListener());
        button1.setOnClickListener(new buttonOnClickListener());
        button2.setOnClickListener(new buttonOnClickListener());
        button3.setOnClickListener(new buttonOnClickListener());
        button4.setOnClickListener(new buttonOnClickListener());
        button5.setOnClickListener(new buttonOnClickListener());
        button6.setOnClickListener(new buttonOnClickListener());
        button7.setOnClickListener(new buttonOnClickListener());
        button8.setOnClickListener(new buttonOnClickListener());
        button9.setOnClickListener(new buttonOnClickListener());
        button0.setOnClickListener(new buttonOnClickListener());

        return view;
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

    // Listeners for Devices Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        deviceId = position - 1;
        Log.d("DEVICE_ID", String.valueOf(deviceId));
        String item = parent.getItemAtPosition(position).toString();
        if (position > 0) {
            // Showing selected spinner item
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            //TODO: Check type of device, show accordingly
            powerButton.setVisibility(View.VISIBLE);
            remoteButtons.setVisibility(View.VISIBLE);
        }
        else {
            powerButton.setVisibility(View.INVISIBLE);
            remoteButtons.setVisibility(View.INVISIBLE);
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        devicesSpinner.setSelection(0);
        powerButton.setVisibility(View.INVISIBLE);
        remoteButtons.setVisibility(View.INVISIBLE);
    }

    public void sendToServer(String value) {
        JSONObject data = new JSONObject();

        try {
            data.put("name", devices.get(deviceId).getName());
            data.put("room", devices.get(deviceId).getRoom());
            data.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("Header", "infra");
            obj.put("Data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray message = new JSONArray();
        message.put(obj);

        Pair<String, String>[] pairs = new Pair[1];
        pairs[0] = new Pair<>("message", message.toString());
        new GetUpdatesAsyncTask(getContext(), "messages").execute(pairs);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // Listener for Toggle Power button
    private class buttonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //TODO: Send command to server
            if (deviceId > -1)
                switch (view.getId()) {
                    case R.id.power_button:
                        sendToServer("p");
                        break;
                    case R.id.button_1:
                        channelNo.append('1');
                        screen.setText(channelNo);
                        break;
                    case R.id.button_2:
                        channelNo.append('2');
                        screen.setText(channelNo);
                        break;
                    case R.id.button_3:
                        channelNo.append('3');
                        screen.setText(channelNo);
                        break;
                    case R.id.button_4:
                        channelNo.append('4');
                        screen.setText(channelNo);
                        break;
                    case R.id.button_5:
                        channelNo.append('5');
                        screen.setText(channelNo);
                        break;
                    case R.id.button_6:
                        channelNo.append('6');
                        screen.setText(channelNo);
                        break;
                    case R.id.button_7:
                        channelNo.append('7');
                        screen.setText(channelNo);
                        break;
                    case R.id.button_8:
                        channelNo.append('8');
                        screen.setText(channelNo);
                        break;
                    case R.id.button_9:
                        channelNo.append('9');
                        screen.setText(channelNo);
                        break;
                    case R.id.button_0:
                        channelNo.append('0');
                        screen.setText(channelNo);
                        break;
                    case R.id.button_delete:
                        if (channelNo.length() > 0) {
                            channelNo.setLength(channelNo.length() - 1);
                            screen.setText(channelNo);
                        }
                        break;
                    case R.id.button_ok:
                        if (channelNo.length() > 0) {
                            sendToServer(channelNo.toString());
                        }
                        break;

            }
        }
    }
}