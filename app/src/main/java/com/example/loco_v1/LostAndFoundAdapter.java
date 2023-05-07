package com.example.loco_v1;

import android.content.Context;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class LostAndFoundAdapter extends RecyclerView.Adapter<LostAndFoundAdapter.ItemViewHolder>{

    private static List<LostAndFoundItem> itemList;
    private static Context context;

    public LostAndFoundAdapter(Context context,List<LostAndFoundItem> itemList) {
        this.itemList = itemList;
        this.context=context;
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
                deleteItem(item.getId());
            }
        });
    }

    private void deleteItem(String id) {
        if (id == null) {
            Toast.makeText(context,"Id is Null",Toast.LENGTH_SHORT);
            // Handle the error here
            return;
        }
        Toast.makeText(context,"Id is "+id,Toast.LENGTH_SHORT);

        // Get a reference to the Firebase Realtime Database node that contains the post data
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("lost_and_found_items").child(id);

        final String[] image_link = {null};
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Parse the data from the DataSnapshot object into a Model_MarketPlaceItem object
                    LostAndFoundItem item = snapshot.getValue(LostAndFoundItem.class);
                    image_link[0] = item.getImageUrl();

                    Toast.makeText(context,"Item Name "+item.getItemName(),Toast.LENGTH_SHORT);

                    // Do something with the item data, for example display it in a view
                    // textView.setText(item.getTitle());
                } else {
                    // Item does not exist in the Realtime Database
                    Toast.makeText(context,"Item does not exist in the Realtime Database",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(context,"database error",Toast.LENGTH_SHORT);
            }
        });

        // Delete the post data from the Realtime Database
        postRef.removeValue();

        Toast.makeText(context,"Image Link "+image_link[0],Toast.LENGTH_SHORT);
        if (image_link[0] != null) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(image_link[0]);
            // Delete the image from Firebase Storage
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Image deleted successfully
                    Toast.makeText(context,"// Image deleted successfully",Toast.LENGTH_SHORT);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Failed to delete image
                    Toast.makeText(context,"Failed to delete image",Toast.LENGTH_SHORT);
                }
            });
        }
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
