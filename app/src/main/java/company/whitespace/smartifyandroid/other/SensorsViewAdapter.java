package company.whitespace.smartifyandroid.other;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.fragment.SensorsFragment.OnListFragmentInteractionListener;
import company.whitespace.smartifyandroid.fragment.dummy.DummyContent.DummyItem;
import company.whitespace.smartifyandroid.model.Room;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SensorsViewAdapter extends RecyclerView.Adapter<SensorsViewAdapter.ViewHolder> {

    private final List<Room> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SensorsViewAdapter(List<Room> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mRoomView.setText(mValues.get(position).getName());
        holder.mLightView.setText(mValues.get(position).getLight());
        holder.mTempView.setText(mValues.get(position).getTemperature() + "\u00B0C");
        holder.mHumidityView.setText(mValues.get(position).getHumidity() + "%");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mRoomView;
        public final TextView mLightView;
        public final TextView mTempView;
        public final TextView mHumidityView;
        public Room mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRoomView = (TextView) view.findViewById(R.id.room_name);
            mLightView = (TextView) view.findViewById(R.id.light_value);
            mTempView = (TextView) view.findViewById(R.id.temperature_value);
            mHumidityView = (TextView) view.findViewById(R.id.humidity_value);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mRoomView.getText() + "'";
        }
    }
}
