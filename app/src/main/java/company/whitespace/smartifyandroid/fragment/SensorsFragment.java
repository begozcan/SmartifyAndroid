package company.whitespace.smartifyandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.model.Room;
import company.whitespace.smartifyandroid.other.SensorsViewAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static company.whitespace.smartifyandroid.model.Rooms.getRooms;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SensorsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TextView lastRefreshed;
    private OnListFragmentInteractionListener mListener;
    private List<Room> rooms;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SensorsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SensorsFragment newInstance(int columnCount) {
        SensorsFragment fragment = new SensorsFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rooms = getRooms(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.sensor_list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        lastRefreshed = (TextView) view.findViewById(R.id.last_refreshed);

        // Set the adapters
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new SensorsViewAdapter(rooms, mListener));

        refreshLayout.setOnRefreshListener(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Listener for Swipe to Refresh
    @Override
    public void onRefresh() {
        Log.d("SENSORS_FREAGMENT", "Refreshed");

        //TODO: Fix this it does not update rooms
        rooms = getRooms(getContext());
        ((SensorsViewAdapter) recyclerView.getAdapter()).updateList(rooms);

        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date refreshDate = new Date();
        String str = "Last refreshed on " + sdf.format(refreshDate);
        lastRefreshed.setText(str);

        refreshLayout.setRefreshing(false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Room room);
    }
}
