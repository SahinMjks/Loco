package com.example.loco_v1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUp_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText email, password, name;
    private ImageView mRegister;
    private TextView existaccount;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    public SignUp_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUp_Fragment newInstance(String param1, String param2) {
        SignUp_Fragment fragment = new SignUp_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up_, container, false);

        email = view.findViewById(R.id.email_field);
        name = view.findViewById(R.id.name_field);
        password = view.findViewById(R.id.password_field);
        mRegister = view.findViewById(R.id.signup_button);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Register");

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaill = email.getText().toString().trim();
                String uname = name.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()) {
                    email.setError("Invalid Email");
                    email.setFocusable(true);
                } else if (pass.length() < 6) {
                    password.setError("Length Must be greater than 6 character");
                    password.setFocusable(true);
                } else {
                    registerUser(emaill, pass, uname);
                }
            }
        });



        return view;
    }

    private void registerUser(String emaill, final String pass, final String uname) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emaill, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String email = user.getEmail();
                    String uid = user.getUid();
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("name", uname);
                    hashMap.put("onlineStatus", "online");
                    hashMap.put("typingTo", "noOne");
                    hashMap.put("image", "");
                    hashMap.put("bio","");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(uid).setValue(hashMap);
                    Toast.makeText(getActivity(), "Registered User " + user.getEmail(), Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_LONG).show();
            }
        });
    }
}