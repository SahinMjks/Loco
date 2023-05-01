package com.example.loco_v1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MarketPlace_Sell extends AppCompatActivity {

    private ImageView itemImage;
    private EditText itemNameEditText;
    private EditText itemDescriptionEditText;
    private EditText itemPriceEditText;
    private Spinner itemCategorySpinner;
    private Button sellButton;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressDialog;

    private ArrayList<String> categoryOptions;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int requestCode;
    private int resultCode;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_sell);

        itemImage = findViewById(R.id.item_image);
        itemNameEditText = findViewById(R.id.item_title);
        itemDescriptionEditText = findViewById(R.id.item_description);
        itemPriceEditText = findViewById(R.id.item_price);
        itemCategorySpinner = findViewById(R.id.category_spinner);
        sellButton = findViewById(R.id.sell_button);


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        // Show progress dialog when image is uploading
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Uploading Image...");
        mProgressDialog.setCancelable(false);

        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selling();
            }
        });
    }

    private void Selling() {
        // Get values from the form
        String itemName = itemNameEditText.getText().toString().trim();

        String itemDescription = itemDescriptionEditText.getText().toString().trim();


        String price = itemPriceEditText.getText().toString().trim();
        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Create date and time formatter
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Convert date and time to string
        String dateString = dateFormat.format(currentDate);


        // Validate form inputs
        if (itemName.isEmpty()) {
            itemNameEditText.setError("Item name is required");
            itemNameEditText.requestFocus();
            return;
        }

        if (itemDescription.isEmpty()) {
            itemDescriptionEditText.setError("Location is required");
            itemDescriptionEditText.requestFocus();
            return;
        }

        if (price.isEmpty()) {
            itemPriceEditText.setError("Time is required");
            itemPriceEditText.requestFocus();
            return;
        }

        // Create a new LostAndFoundItem object
        Model_MarketPlaceItem item = new Model_MarketPlaceItem();
        item.setItemName(itemName);
        item.setItemDescription(itemDescription);
        item.setPrice(price);
        //item.setTime(time);
        item.setImageUrl(String.valueOf(mImageUri));

        // Add the item to the Firebase Realtime Database
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("MarketPlace");
        String itemId = itemsRef.push().getKey();
        itemsRef.child(itemId).setValue(item);

        // Show a success message
        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();

        // Clear the form
        itemNameEditText.setText("");
        itemDescriptionEditText.setText("");
        itemPriceEditText.setText("");
        //timeEditText.setText("");
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            // Load image into ImageView
            itemImage.setImageURI(mImageUri);
        }
        uploadFile();
    }
    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the uploaded image URL from Firebase Storage
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mImageUri = Uri.parse(uri.toString());
                                    // Use imageUrl to create the LostAndFoundItem object and save to database
                                    //createLostAndFoundItem(String.valueOf(mImageUri));
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle errors
                        }
                    });
        } else {
            // Handle case where no image is selected
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}