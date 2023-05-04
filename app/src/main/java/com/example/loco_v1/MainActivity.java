
package com.example.loco_v1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    FirebaseAuth firebaseAuth;

    private Context mContext;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mContext=this;

        firebaseAuth = FirebaseAuth.getInstance();

        //ToDo When app will open it should show it's user's name

        // Create an adapter that will return a fragment for each of the four
        // primary sections of the app.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        // Attach the ViewPager to the TabLayout
        mTabLayout.setupWithViewPager(mViewPager);

        findViewById(R.id.profile_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the Profile button click
                Toast.makeText(MainActivity.this, "Profile button clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });
        findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the Logout button click
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, SplashScreen.class));
                MainActivity.this.finish();
            }
        });
        findViewById(R.id.notification_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the Notification button click
                Toast.makeText(MainActivity.this, "Notification button clicked", Toast.LENGTH_SHORT).show();

            }
        });
        findViewById(R.id.explore_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the Notification button click
                Toast.makeText(MainActivity.this, "Notification button clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,Experimental_Activity.class));
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ChatListFragment();
                case 1:
                    return new PostFragment();
                case 2:
                    return new LostAndFoundFragment();
                case 3:
                    return new MarketPlaceFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable icon = null;
            String title = null;
            switch (position) {
                case 0:
                    icon = ContextCompat.getDrawable(mContext, R.drawable.ic_chat_normal);
                    //title = mContext.getString(R.string.tab1_title);
                    break;
                case 1:
                    icon = ContextCompat.getDrawable(mContext, R.drawable.ic_posts);
                    //title = mContext.getString(R.string.tab2_title);
                    break;
                case 2:
                    icon = ContextCompat.getDrawable(mContext, R.drawable.ic_lost);
                    //title = mContext.getString(R.string.tab3_title);
                    break;
                case 3:
                    icon = ContextCompat.getDrawable(mContext, R.drawable.ic_marketplace);
                    //title = mContext.getString(R.string.tab4_title);
                    break;
            }
            SpannableStringBuilder sb = new SpannableStringBuilder("   "); // add whitespace to the left of title for padding
            if (icon != null) {
                icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                ImageSpan imageSpan = new ImageSpan(icon, ImageSpan.ALIGN_BOTTOM);
                sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return sb;
        }

    }
}
