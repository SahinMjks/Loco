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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MarketPlaceFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ImageButton fab;

    private ArrayList<Model_MarketPlaceItem> items;
    private AdapterMarketPlace_Item mAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    SearchView searchView;
    String selectedCategory;
    private String mSortOption = "price";
    public MarketPlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarketPlace.
     */
    // TODO: Rename and change types and number of parameters
    public static MarketPlaceFragment newInstance(String param1, String param2) {
        MarketPlaceFragment fragment = new MarketPlaceFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_market_place, container, false);

        items = new ArrayList<>();
        mAdapter = new AdapterMarketPlace_Item(rootView.getContext(),items);
        recyclerView = rootView.findViewById(R.id.item_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);


        //loadPosts(selectedCategory);
        fab=rootView.findViewById(R.id.add_item_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MarketPlace_Sell.class));
            }
        });


        searchView =rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchPosts(query);
                } else {
                    loadPosts("");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchPosts(s);
                return false;
            }

        });

        Spinner spinner = rootView.findViewById(R.id.category_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.category_options,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                // Do something with the selected category
                loadPosts(selectedCategory);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
                Toast.makeText(getActivity(),"Nothing Selected",Toast.LENGTH_SHORT).show();
                loadPosts("");
            }
        });

        Spinner sort_spinner = rootView.findViewById(R.id.sort_spinner);

        ArrayAdapter<CharSequence> sort_adapter = ArrayAdapter.createFromResource(getActivity(),R.array.sort_options,android.R.layout.simple_spinner_item);
        sort_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_spinner.setAdapter(sort_adapter);
        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_option = parent.getItemAtPosition(position).toString();
                // Do something with the selected category
                //Toast.makeText(getActivity(),selected_option,Toast.LENGTH_SHORT).show();
                if(selected_option=="Price:High to Low"){
                    mSortOption="price";
                } else if (selected_option=="Price:Low to High") {
                    mSortOption="-"+"price";
                } else if (selected_option=="Newest First") {
                    mSortOption="time";
                }
                else{
                    mSortOption="-"+"time";
                }
                loadPosts(selectedCategory);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
                //Toast.makeText(getActivity(),"Nothing Selected",Toast.LENGTH_SHORT).show();
                loadPosts(selectedCategory);
            }
        });
        return rootView;
    }

    private void loadPosts(String selectedCategory) {
        mDatabase =FirebaseDatabase.getInstance().getReference();

        Query query=mDatabase.child("MarketPlace").orderByChild(mSortOption);
        //Toast.makeText(getActivity(),selectedCategory,Toast.LENGTH_SHORT).show();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Model_MarketPlaceItem item = itemSnapshot.getValue(Model_MarketPlaceItem.class);


                    //Strings comparison should be done using equals not == using this
                    if(selectedCategory!=null && selectedCategory.equals(item.getCategory())){
                        items.add(item);
                    } else if (selectedCategory==null) {
                        items.add(item);
                    }

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchPosts(String search) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MarketPlace");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Model_MarketPlaceItem item = dataSnapshot1.getValue(Model_MarketPlaceItem.class);
                    if (item.getItemName().toLowerCase().contains(search.toLowerCase()) ||
                            item.getItemDescription().toLowerCase().contains(search.toLowerCase())) {
                        items.add(item);
                    }
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}