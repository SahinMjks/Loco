package com.example.loco_v1;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class LostAndFoundFragment extends Fragment {
    FloatingActionButton fab;
    private ArrayList<LostAndFoundItem> mLostAndFoundItems;
    private LostAndFoundAdapter mAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    //SearchView is to search the item
    SearchView searchView;

    //Initial lost value is All so that's why it's 2
    int lost=2;
    public LostAndFoundFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lost_and_found, container, false);

        fab=rootView.findViewById(R.id.fab_create_item);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(),Add_lost_and_found.class));
                }
            });
        }

        //To show the user in a recycler view
        mLostAndFoundItems = new ArrayList<>();
        mAdapter = new LostAndFoundAdapter(mLostAndFoundItems);
        recyclerView = rootView.findViewById(R.id.lost_and_found_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);


        Spinner spinner = rootView.findViewById(R.id.filter_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.filter_options,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                // Do something with the selected category
                Toast.makeText(getActivity(),selectedCategory,Toast.LENGTH_SHORT).show();
                if(selectedCategory.equals("Lost Items")){
                    lost=0;
                } else if (selectedCategory.equals("Found Items")) {
                    lost=1;
                } else if (selectedCategory.equals("All")) {
                    lost=2;
                }
                loadPosts();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
                Toast.makeText(getActivity(),"Nothing Selected",Toast.LENGTH_SHORT).show();

            }
        });


        //TODO I need to merge both Search Item and Load item in one place
        searchView=rootView.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    Toast.makeText(getActivity(),"Search View is not empty",Toast.LENGTH_SHORT).show();
                    SearchItem(query);
                } else {
                    Toast.makeText(getActivity(),"Search View is empty",Toast.LENGTH_SHORT).show();
                    loadPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchItem(newText);
                return false;
            }
        });

        return rootView;
    }
    private void loadPosts() {
        mDatabase = FirebaseDatabase.getInstance().getReference("lost_and_found_items");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mLostAndFoundItems.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    LostAndFoundItem item = itemSnapshot.getValue(LostAndFoundItem.class);

                    if(lost==0 && item.get_isLost() ==true){
                        mLostAndFoundItems.add(item);
                    }
                    else if ((lost == 1) && item.get_isLost() ==false) {
                        mLostAndFoundItems.add(item);
                    }
                    else if (lost==2) {
                        mLostAndFoundItems.add(item);
                    }

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SearchItem(String query) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("lost_and_found_items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLostAndFoundItems.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    LostAndFoundItem item = dataSnapshot1.getValue(LostAndFoundItem.class);

                    if (item.getItemName().toLowerCase().contains(query.toLowerCase()) ||
                            item.getItemDescription().toLowerCase().contains(query.toLowerCase())||item.getItemLocation().toLowerCase().contains(query.toLowerCase())) {
                        mLostAndFoundItems.add(item);
                    }
                    mAdapter = new LostAndFoundAdapter(mLostAndFoundItems);
                    recyclerView.setAdapter(mAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}