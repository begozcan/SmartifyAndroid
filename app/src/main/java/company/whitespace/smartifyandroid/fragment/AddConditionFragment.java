package company.whitespace.smartifyandroid.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import company.whitespace.smartifyandroid.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddConditionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters
    private int deviceId;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();

        if(bundle != null){
            deviceId = Integer.parseInt(bundle.getString("device_id"));
        }

        Log.d("DEVICE_ID", String.valueOf(deviceId));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_condition, container, false);
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
