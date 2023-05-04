package com.example.loco_v1;



import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Add_lost_and_found extends AppCompatActivity {

    private ImageButton mCreateButton,back_button;
    private ImageView mItemImageView;
    private DatabaseReference mDatabaseReference;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressDialog;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int requestCode;
    private int resultCode;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String uid;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lost_and_found);


        mItemImageView=findViewById(R.id.choose_image_button);
        mCreateButton=findViewById(R.id.create_button);
        back_button=findViewById(R.id.back_button);

        firebaseAuth = FirebaseAuth.getInstance();
        //Getting the user list

        // getting current user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        uid=firebaseUser.getUid();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Add_lost_and_found.this,LostAndFoundFragment.class));
            }
        });

        // Initialize Firebase Storage and Database;
        //mDatabaseReference = FirebaseDatabase.getInstance().getReference("lost_and_found_items");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        // Show progress dialog when image is uploading
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Uploading Image...");
        mProgressDialog.setCancelable(false);

        // Choose image from gallery
        mItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        // Create Lost and Found Item
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLostAndFoundItem();
            }
        });
    }




    // Create a new lost and found item and store it in the real-time database
    private void createLostAndFoundItem() {
        // Get values from the form
        RadioButton lostRadioBtn = findViewById(R.id.lost_radio_button);
        boolean isLost = lostRadioBtn.isChecked();
        EditText itemNameEditText = findViewById(R.id.item_name_edit_text);
        String itemName = itemNameEditText.getText().toString().trim();
        EditText itemDescriptionEditText = findViewById(R.id.item_description_edit_text);
        String itemDescription = itemDescriptionEditText.getText().toString().trim();
        EditText locationEditText = findViewById(R.id.location_edit_text);
        String location = locationEditText.getText().toString().trim();
        EditText timeEditText = findViewById(R.id.time_edit_text);
        String time = timeEditText.getText().toString().trim();


        // Validate form inputs
        if (itemName.isEmpty()) {
            itemNameEditText.setError("Item name is required");
            itemNameEditText.requestFocus();
            return;
        }

        if (location.isEmpty()) {
            locationEditText.setError("Location is required");
            locationEditText.requestFocus();
            return;
        }

        if (time.isEmpty()) {
            timeEditText.setError("Time is required");
            timeEditText.requestFocus();
            return;
        }

        // Create a new LostAndFoundItem object
        LostAndFoundItem item = new LostAndFoundItem();
        item.setItemName(itemName);
        item.setItemDescription(itemDescription);
        item.setLocation(location);
        item.setTime(time);
        item.setImageUrl(String.valueOf(mImageUri));
        item.setUid(uid);
        item.setFounded(false);
        if(isLost==true){
            item.set_isLost(true);
        }
        else{
            item.set_isLost(false);
        }


        // Add the item to the Firebase Realtime Database
        DatabaseReference itemsRef =FirebaseDatabase.getInstance().getReference("lost_and_found_items");
        String itemId = itemsRef.push().getKey();
        itemsRef.child(itemId).setValue(item);

        // Show a success message
        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();

        // Clear the form
        itemNameEditText.setText("");
        itemDescriptionEditText.setText("");
        locationEditText.setText("");
        timeEditText.setText("");
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
            mItemImageView.setImageURI(mImageUri);
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