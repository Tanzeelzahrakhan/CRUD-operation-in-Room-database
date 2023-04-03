package com.example.roomdatabasecrudoperation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.roomdatabasecrudoperation.databinding.ActivitySplashBinding;

public class Splash_Activity extends AppCompatActivity {
 ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash_Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}