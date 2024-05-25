package com.example.welcome_and_weather;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.welcome_and_weather.Task_2.Login;
import com.example.welcome_and_weather.Task_2.Register;
import com.example.welcome_and_weather.Task_2.VPAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tableLayout;
    private ViewPager viewPager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tableLayout = findViewById(R.id.tablelayout);
        viewPager = findViewById(R.id.viewpager);

        tableLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new Register(), "Register");
        vpAdapter.addFragment(new Login(),"Login");
        vpAdapter.addFragment(new Login(), "Login");
        vpAdapter.addFragment(new Register(),"Register");
        viewPager.setAdapter(vpAdapter);

    }
}