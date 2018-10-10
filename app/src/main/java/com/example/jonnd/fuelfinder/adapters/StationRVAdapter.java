package com.example.jonnd.fuelfinder.adapters;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonnd.fuelfinder.databinding.StationListItemBinding;
import com.example.jonnd.fuelfinder.entities.Station;
import com.example.jonnd.fuelfinder.listeners.StationClickListener;

import java.util.List;

public class StationRVAdapter extends RecyclerView.Adapter<StationRVAdapter.StationViewHolder> {
    /**
     * Click listener that will get fired when a station items is selected
     */
    private final StationClickListener mListener;

    public class StationViewHolder extends RecyclerView.ViewHolder {
        /**
         * Binding class for the station list item.
         */
        private StationListItemBinding mBinding;

        public StationViewHolder(StationListItemBinding binding) {
            // Pass the binding classes root view into the ViewHolder's constructor.
            super(binding.getRoot());
            mBinding = binding;
            // Set the click listener that will get bound to the list item.
            mBinding.setClickListener(mListener);
        }

        public void bind(Station station) {
            // Set the station item that will get bound to the list item layout.
            mBinding.setStation(station);
        }
    }

    public StationRVAdapter(StationClickListener listener) {
        mListener = listener;
    }

    private List<Station> mStations;

    /**
     * Set's the List of Station items as the adapters data set.
     * @param newStations
     */
    public void setStations(final List<Station> newStations) {
        if(mStations != null) {
            // Calculate the differences between the two list objects... This will determine the
            // transformations necessary to update the adapter recyclerview.
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mStations.size();
                }

                @Override
                public int getNewListSize() {
                    return newStations.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mStations.get(oldItemPosition).getId() == newStations.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return mStations.get(oldItemPosition).equals(newStations.get(newItemPosition));
                }
            });
            mStations = newStations;
            // Dispatch the diff results to th the adapter which will cause the recycler view to be updated.
            diffResult.dispatchUpdatesTo(this);
        }
        else {
            mStations = newStations;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StationListItemBinding binding = StationListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public Station getItem(int position) {
        return mStations.get(position);
    }

    @Override
    public int getItemCount() {
        return mStations != null ? mStations.size() : 0;
    }
}
