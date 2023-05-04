package com.example.loco_v1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterMarketPlace_Item extends RecyclerView.Adapter<AdapterMarketPlace_Item.ItemViewHolder> {

    private static List<Model_MarketPlaceItem> itemList;
    private Context context;

    public AdapterMarketPlace_Item(Context context,List<Model_MarketPlaceItem> itemList) {
        this.itemList = itemList;
        this.context=context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_marketplace_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Model_MarketPlaceItem item = itemList.get(position);
        holder.itemNameTextView.setText(item.getItemName());
        holder.itemDescriptionTextView.setText(item.getItemDescription());
        holder.itemPriceTextView.setText(String.valueOf(item.getPrice()));
        holder.itemDateTextView.setText(item.getTime());

        final String uid=item.getUserId();
        Boolean sold= Boolean.valueOf(item.getSold());
        // Load image into ImageView using Glide library
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_lost)
                .error(R.drawable.ic_error)
                .into(holder.itemImageView);


        holder.sold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Delete this Item",Toast.LENGTH_SHORT);
            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Chat for more information",Toast.LENGTH_SHORT);
                Intent intent = new Intent(context, ChatActivity.class);

                // putting uid of user in extras
                intent.putExtra("uid", uid);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder  extends RecyclerView.ViewHolder{
        public TextView itemNameTextView;
        public TextView itemDescriptionTextView;
        public ImageView itemImageView;
        public TextView itemPriceTextView;
        public TextView itemDateTextView;

        public ImageButton sold,chat;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item_title);
            itemDescriptionTextView = itemView.findViewById(R.id.item_description);
            itemImageView = itemView.findViewById(R.id.item_image);
            itemPriceTextView = itemView.findViewById(R.id.item_price);
            itemDateTextView = itemView.findViewById(R.id.item_date);
            sold=itemView.findViewById(R.id.sold_button);
            chat=itemView.findViewById(R.id.chat_button);
        }
    }
}
