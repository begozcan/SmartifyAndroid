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

public class ControlDeviceFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner devicesSpinner;
    private TextView powerButton;
    private LinearLayout powerLayout;
    private List<Device> devices = new ArrayList<Device>();
    private OnFragmentInteractionListener mListener;
    private int deviceId = -1;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control_device, container, false);

        devicesSpinner = (Spinner) view.findViewById(R.id.device_spinner);
        powerButton = (TextView) view.findViewById(R.id.power_button);
        powerLayout = (LinearLayout) view.findViewById(R.id.power_layout);

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


        powerButton.setOnClickListener(new powerOnClickListener());

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
            powerLayout.setVisibility(View.VISIBLE);
        }
        else {
            powerLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        devicesSpinner.setSelection(0);
        powerLayout.setVisibility(View.INVISIBLE);
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
    private class powerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (deviceId > -1) {
                Log.d("POWER_BUTTON", "Toggle power for " + devices.get(deviceId).toString());
            }
        }
    }
}
