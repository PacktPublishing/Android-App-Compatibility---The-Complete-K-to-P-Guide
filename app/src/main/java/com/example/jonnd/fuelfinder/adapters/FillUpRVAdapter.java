package com.example.jonnd.fuelfinder.adapters;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonnd.fuelfinder.databinding.ListItemBinding;
import com.example.jonnd.fuelfinder.entities.FillUp;
import com.example.jonnd.fuelfinder.listeners.FillUpClickListener;

import java.util.List;

public class FillUpRVAdapter extends RecyclerView.Adapter<FillUpRVAdapter.FillUpViewHolder> {
    /**
     * Click listener that will get fired when a fill-up items is selected
     */
    FillUpClickListener mClickListener;

    public FillUpRVAdapter(FillUpClickListener clickListener) {
        mClickListener = clickListener;
    }

    public class FillUpViewHolder extends RecyclerView.ViewHolder {
        /**
         * Binding class for the fill_up list item.
         */
        ListItemBinding mBinding;

        public FillUpViewHolder(ListItemBinding binding) {
            // Pass the binding classes root view into the ViewHolder's constructor.
            super(binding.getRoot());
            mBinding = binding;
            // Set the click listener that will get bound to the list item.
            mBinding.setClickListener(mClickListener);
        }

        public void bind(FillUp fillUp) {
            // Set the fillup item that will get bound to the list item layout.
            mBinding.setFillUp(fillUp);
        }
    }

    private List<FillUp> mFillUps;

    /**
     * Set's the List of FillUp items as the adapters data set.
     * @param newFillUps
     */
    public void setFillUps(final List<FillUp> newFillUps) {
        if(mFillUps != null) {
            // Calculate the differences between the two list objects... This will determine the
            // transformations necessary to update the adapter recyclerview.
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mFillUps.size();
                }

                @Override
                public int getNewListSize() {
                    return newFillUps.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mFillUps.get(oldItemPosition).getId() == newFillUps.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return mFillUps.get(oldItemPosition).equals(newFillUps.get(newItemPosition));
                }
            });

            mFillUps = newFillUps;
            // Dispatch the diff results to th the adapter which will cause the recycler view to be updated.
            diffResult.dispatchUpdatesTo(this);
        }
        else {
            mFillUps = newFillUps;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public FillUpRVAdapter.FillUpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the ListItemBinding object.
        ListItemBinding binding = ListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        // Create a new FillUpViewHolder and pass in the binding clas.
        return new FillUpViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FillUpRVAdapter.FillUpViewHolder holder, int position) {
        // Pass in the FillUp for position, to bind it to the ViewHolder's binding class.
        holder.bind(getItem(position));
    }

    public FillUp getItem(int position) {
        return mFillUps.get(position);
    }

    @Override
    public int getItemCount() {
        return mFillUps != null ? mFillUps.size() : 0;
    }
}
