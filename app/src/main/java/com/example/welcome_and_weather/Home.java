package com.example.welcome_and_weather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class Home extends AppCompatActivity {

    TextView welcomeTextView;
    Button exitt, theen;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcomeTextView = findViewById(R.id.welcome);
        exitt = findViewById(R.id.exit);
        theen = findViewById(R.id.then);

        String email = getIntent().getStringExtra("email");
        welcomeTextView.setText("Welcome, " + email + "!");

        exitt.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();

        });
        theen.setOnClickListener(v -> {
            startActivity(new Intent(this, Weather.class));
            finish();
        });


    }
}