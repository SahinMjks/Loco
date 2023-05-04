package com.example.loco_v1;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class LostAndFoundAdapter extends RecyclerView.Adapter<LostAndFoundAdapter.ItemViewHolder>{

    private static List<LostAndFoundItem> itemList;

    public LostAndFoundAdapter(List<LostAndFoundItem> itemList) {
        this.itemList = itemList;
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
        LostAndFoundItem item =itemList.get(position);
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


        final String uid= item.getUid();
        final Boolean founded=item.getFounded();

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Chat for more information",Toast.LENGTH_SHORT);
                Intent intent = new Intent(view.getContext(), ChatActivity.class);

                // putting uid of user in extras
                intent.putExtra("uid", uid);
                view.getContext().startActivity(intent);
            }
        });

        holder.founded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Already Founded",Toast.LENGTH_SHORT);
            }
        });
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
        public ImageButton chat,founded;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item_name_textview);
            itemDescriptionTextView = itemView.findViewById(R.id.item_description_textview);
            itemImageView = itemView.findViewById(R.id.item_image_imageview);
            itemLocationTextView = itemView.findViewById(R.id.item_location_textview);
            itemDateTextView = itemView.findViewById(R.id.item_date_textview);
            chat=itemView.findViewById(R.id.chat_button);
            founded=itemView.findViewById(R.id.cancel_button);
        }
    }
}
