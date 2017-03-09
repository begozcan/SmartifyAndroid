package company.whitespace.smartifyandroid.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.model.Device;

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
public class AddScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters
    private int deviceId;
    private String action;
    private List<Device> devices = new ArrayList<Device>();
    private List<String> actions = new ArrayList<String>();

    private Spinner devicesSpinner;
    private Spinner actionSpinner;
    private EditText time;

    private ToggleButton mon;
    private ToggleButton tue;
    private ToggleButton wed;
    private ToggleButton thu;
    private ToggleButton fri;
    private ToggleButton sat;
    private ToggleButton sun;

    private Button submit;

    private OnFragmentInteractionListener mListener;

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
        devices  = getDevices(getContext());
        deviceId = -1;
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
        time = (EditText) view.findViewById(R.id.time);
        submit = (Button) view.findViewById(R.id.button_submit);

        mon = (ToggleButton) view.findViewById(R.id.mon_button);
        tue = (ToggleButton) view.findViewById(R.id.tue_button);
        wed = (ToggleButton) view.findViewById(R.id.wed_button);
        thu = (ToggleButton) view.findViewById(R.id.thu_button);
        fri = (ToggleButton) view.findViewById(R.id.fri_button);
        sat = (ToggleButton) view.findViewById(R.id.sat_button);
        sun = (ToggleButton) view.findViewById(R.id.sun_button);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            deviceId = Integer.parseInt(bundle.getString("device_id"));
        }

        Log.d("DEVICE_ID", String.valueOf(deviceId));

        List<String> devices_str = toStringList(devices);
        devicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deviceId = position - 1;
                Log.d("DEVICE_ID", String.valueOf(deviceId));
                String item = parent.getItemAtPosition(position).toString();
                if (position > 0) {
                    // Showing selected spinner item
                    Toast.makeText(parent.getContext(), "Device: " + item, Toast.LENGTH_LONG).show();
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
                action = parent.getItemAtPosition(position).toString();
                if (position > 0) {
                    // Showing selected spinner item
                    Toast.makeText(parent.getContext(), "Action: " + action, Toast.LENGTH_LONG).show();
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
            //TODO: Check and send to server
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
