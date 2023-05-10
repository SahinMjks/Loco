package com.example.loco_v1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

// Main Activity implements Adapter view
public class Experimental_Activity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimental);

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


    }

    private void showDialog(){

        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout);

        LinearLayout add_post=dialog.findViewById(R.id.linearLayout_add_post);
        LinearLayout add_lost_and_found=dialog.findViewById(R.id.linearLayout_add_lost_and_found);
        LinearLayout add_marketplace=dialog.findViewById(R.id.linearLayout_add_marketplace);

        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Experimental_Activity.this,"Add Post is Clicked",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Experimental_Activity.this,AddBlogsFragment.class));
            }
        });

        add_lost_and_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Experimental_Activity.this,"Add Lost and Found is Clicked",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Experimental_Activity.this, Add_lost_and_found.class));
            }
        });

        add_marketplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Experimental_Activity.this,"Add marketplace is Clicked",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Experimental_Activity.this, MarketPlace_Sell.class));
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

}
