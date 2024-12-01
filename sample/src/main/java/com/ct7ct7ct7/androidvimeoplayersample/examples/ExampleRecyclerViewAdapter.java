
package com.ct7ct7ct7.androidvimeoplayersample.examples;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ItemRecyclerviewBinding;
import java.util.ArrayList;

public class ExampleRecyclerViewAdapter extends RecyclerView.Adapter<ExampleRecyclerViewAdapter.ViewHolder> {

    private final Lifecycle lifecycle;
    private final ArrayList<Integer> items;

    public ExampleRecyclerViewAdapter(Lifecycle lifecycle, ArrayList<Integer> items) {
        this.lifecycle = lifecycle;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecyclerviewBinding binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        lifecycle.addObserver(binding.vimeoPlayerView);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int id = items.get(position);
        holder.binding.vimeoPlayerView.initialize(true, id);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ItemRecyclerviewBinding binding;

        public ViewHolder(ItemRecyclerviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}