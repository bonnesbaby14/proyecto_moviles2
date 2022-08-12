package com.example.proyecto_moviles2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class PackageDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);

        Log.d("gabo", "llego el intent el code:  "+getIntent().getExtras().getInt("id"));
    }
}