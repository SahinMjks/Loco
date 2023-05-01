
package com.example.loco_v1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private ViewPager mViewPagerIntro;
    private IntroPagerAdapter mIntroPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mViewPagerIntro = findViewById(R.id.view_pager_intro);
        mIntroPagerAdapter = new IntroPagerAdapter(getSupportFragmentManager());
        mViewPagerIntro.setAdapter(mIntroPagerAdapter);

        // Set up indicator dots for the intro screen
        TabLayout tabLayoutIntroDots = findViewById(R.id.tab_layout_intro_dots);
        tabLayoutIntroDots.setupWithViewPager(mViewPagerIntro, true);

        // Set up "Get Started" button
        ImageButton buttonGetStarted = findViewById(R.id.button_get_started);
        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start main activity
                Intent intent = new Intent(SplashScreen.this,RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });



        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            currentUser = mAuth.getCurrentUser();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(SplashScreen.this, RegistrationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, 5000);
    }
}
