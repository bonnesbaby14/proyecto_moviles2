package com.example.proyecto_moviles2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.hardware.biometrics.BiometricManager;
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


        TextView txt_login=findViewById(R.id.txt_login);
        Button btn_login=findViewById(R.id.btnLogin);

        androidx.biometric.BiometricManager biometricManager= androidx.biometric.BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:

                txt_login.setText("Si se puedo");
                break;

            default:
                txt_login.setText("no se puedo");
                break;

        }
        Executor excecutor= ContextCompat.getMainExecutor(this);

        androidx.biometric.BiometricPrompt biometricPrompt= new BiometricPrompt(MainActivity.this, excecutor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
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

        final androidx.biometric.BiometricPrompt.PromptInfo promptInfo=new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("login")
                .setDescription("user login with finger")
                .setNegativeButtonText("cancel")
                .build();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

}