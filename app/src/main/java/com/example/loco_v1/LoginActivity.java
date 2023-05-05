package com.example.loco_v1;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

// Main Activity implements Adapter view
public class LoginActivity extends AppCompatActivity {

    private Context mContext;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mContext=this;


        SectionsPagerAdapter_1 mSectionsPagerAdapter = new SectionsPagerAdapter_1(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mTabLayout.setupWithViewPager(mViewPager);

    }
    public class SectionsPagerAdapter_1 extends FragmentPagerAdapter {

        public SectionsPagerAdapter_1(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Login_Fragment();
                case 1:
                    return new SignUp_Fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable icon = null;
            String title = null;
            switch (position) {
                case 0:
                    icon = ContextCompat.getDrawable(mContext, R.drawable.ic_login);
                    title = mContext.getString(R.string.login_title);

                    break;
                case 1:
                    icon = ContextCompat.getDrawable(mContext, R.drawable.ic_signup);
                    title = mContext.getString(R.string.Signup_title);
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