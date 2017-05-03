package company.whitespace.smartifyandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.activity.IRSetupActivity;
import company.whitespace.smartifyandroid.activity.MainActivity;
import company.whitespace.smartifyandroid.networking.DeviceAsyncTask;
import company.whitespace.smartifyandroid.networking.GetUpdatesAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDeviceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String[] types = {"Select type...", "AC Plug Unit", "Sensor Unit", "Universal Remote Unit", "Radiator Control Unit", "Pet Feeder Unit"};

    private OnFragmentInteractionListener mListener;
    //private final FragmentManager fragmentManager;

    private EditText deviceName;
    private EditText deviceRoom;
    private Spinner typeSpinner;
    private Button submitButton, configButton;
    private int type;

    public AddDeviceFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddDeviceFragment newInstance(String param1, String param2) {
        AddDeviceFragment fragment = new AddDeviceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = -1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_device, container, false);

        deviceName = (EditText) view.findViewById(R.id.input_device_name);
        deviceRoom = (EditText) view.findViewById(R.id.input_room);
        typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
        submitButton = (Button) view.findViewById(R.id.button_submit);
        configButton = (Button) view.findViewById(R.id.button_configure);

        typeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(dataAdapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = deviceName.getText().toString();
                String room = deviceRoom.getText().toString();
                if (!name.isEmpty() && !room.isEmpty() && type > -1)
                    sendToServer(name, room);
            }
        });

        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setup = new Intent(getActivity(), IRSetupActivity.class);
                startActivity(setup);
            }
        });

        return view;
    }

    public void onSuccess(){
        MainActivity.CURRENT_TAG = MainActivity.TAG_DEVICES;
        // update the main content by replacing fragments
        Fragment fragment = new DevicesFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, "devices");
        fragmentTransaction.commit();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //type = position;
        //if (type == 3){
        //    configButton.setVisibility(View.VISIBLE);
        //}
        //else{
        //    configButton.setVisibility(View.GONE);
        //}
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        typeSpinner.setSelection(0);
        type = -1;
    }

    public void sendToServer(String name, String room) {
        if (type != -1 && !name.isEmpty() && !room.isEmpty()) {
            Pair<String, String>[] pairs = new Pair[3];
            pairs[0] = new Pair<>("name", name);
            pairs[1] = new Pair<>("room", room);
            pairs[2] = new Pair<>("type", types[type]);
            DeviceAsyncTask deviceAsyncTask = new DeviceAsyncTask(getContext(), "devices_add");
            deviceAsyncTask.setAddDeviceFragment(AddDeviceFragment.this);
            deviceAsyncTask.execute(pairs);
        }
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
