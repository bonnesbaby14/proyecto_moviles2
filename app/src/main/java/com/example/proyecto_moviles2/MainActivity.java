package com.example.proyecto_moviles2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView txt_login = findViewById(R.id.txt_login);
        Button btn_login = findViewById(R.id.btnLogin);


        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissioncheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);

            switch (biometricManager.canAuthenticate()) {
                case BiometricManager.BIOMETRIC_SUCCESS:

                    txt_login.setText("Si se puedo");
                    break;

                default:
                    txt_login.setText("no se puedo");
                    break;

            }
            Executor excecutor = ContextCompat.getMainExecutor(this);

            androidx.biometric.BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, excecutor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            });

            final androidx.biometric.BiometricPrompt.PromptInfo promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                    .setTitle("login")
                    .setDescription("user login with finger")
                    .setNegativeButtonText("cancel")
                    .build();
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            txt_login.setText("" + location.getLatitude() + " " + location.getLongitude());
                        }
                    };
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                    //biometricPrompt.authenticate(promptInfo);
                }
            });
        }

    }
}