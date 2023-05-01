package com.example.loco_v1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class LostAndFoundAdapter extends RecyclerView.Adapter<LostAndFoundAdapter.ItemViewHolder>{

    private static List<LostAndFoundItem> itemList;
    private static  List<LostAndFoundItem> filteredItemList;
    private static ItemFilter itemFilter;

    public LostAndFoundAdapter(List<LostAndFoundItem> itemList) {
        this.itemList = itemList;
        this.filteredItemList = itemList;
        this.itemFilter = new ItemFilter();
    }

    public Filter getFilter() {
        return itemFilter;
    }
    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<LostAndFoundItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList = itemList;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (LostAndFoundItem item : itemList) {
                    if (item.getItemName().toLowerCase().contains(filterPattern) ||
                            item.getItemDescription().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItemList = (List<LostAndFoundItem>) results.values;
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_lostandfound, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        LostAndFoundItem item = filteredItemList.get(position);
        holder.itemNameTextView.setText(item.getItemName());
        holder.itemDescriptionTextView.setText(item.getItemDescription());
        holder.itemLocationTextView.setText(item.getLocation());
        holder.itemDateTextView.setText(item.getTime());
        // Load image into ImageView using Glide library
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_lost)
                .error(R.drawable.ic_error)
                .into(holder.itemImageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemNameTextView;
        public TextView itemDescriptionTextView;
        public ImageView itemImageView;
        public TextView itemLocationTextView;
        public TextView itemDateTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item_name_textview);
            itemDescriptionTextView = itemView.findViewById(R.id.item_description_textview);
            itemImageView = itemView.findViewById(R.id.item_image_imageview);
            itemLocationTextView = itemView.findViewById(R.id.item_location_textview);
            itemDateTextView = itemView.findViewById(R.id.item_date_textview);
        }
    }
}
