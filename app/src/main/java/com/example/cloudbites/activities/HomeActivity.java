package com.example.cloudbites.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cloudbites.MainActivity;
import com.example.cloudbites.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fullscreen
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        //Start login activity after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() != null){
                    progressBar.setVisibility(View.VISIBLE);
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    Toast.makeText(HomeActivity.this, "Please wait you are already logged in", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
            }
        }, 2000);
    }
}