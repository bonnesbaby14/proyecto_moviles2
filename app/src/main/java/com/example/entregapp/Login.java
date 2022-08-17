package com.example.entregapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView usertx = findViewById(R.id.usuariovav);
        TextView passwordtx = findViewById(R.id.password);





        Log.d("Warm", "uno");



        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String user = preferences.getString("usuario", "no existe");
        String password = preferences.getString("password", "no existe");

        if(!user.equals("no existe")){
            usertx.setText(user);
            passwordtx.setText(password);
            passwordtx.setEnabled(false);
            usertx.setEnabled(false);



        }


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
                    break;
                default:
                    break;
            }
        }
        Executor excecutor = ContextCompat.getMainExecutor(this);

        androidx.biometric.BiometricPrompt biometricPrompt = new BiometricPrompt(Login.this, excecutor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(Login.this, "No se pudo iniciar sesion", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(Login.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final androidx.biometric.BiometricPrompt.PromptInfo promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Usar huella para iniciar session")
                .setNegativeButtonText("Cancelar")
                .build();


        btn_login.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             Log.d("gabo", "se dio clic para iniciar sesion"+passwordtx.getText().toString()+" "+usertx.getText().toString());

                                             if (user.equals("no existe")) {
                                                 Log.d("gabo", "no ecnontre preferencias");
                                                 String url = "https://ventanilla.softwaredatab.com/api/logingabo";
                                                 RequestQueue queue = Volley.newRequestQueue(Login.this);
                                                 StringRequest request = new StringRequest(Request.Method.POST, url,
                                                         new com.android.volley.Response.Listener<String>() {
                                                             @Override
                                                             public void onResponse(String response) {

                                                                 try {
                                                                     JSONObject jsonObject = new JSONObject(response);
                                                                     Log.d("gabo", "respondi");
                                                                     if (jsonObject.getString("mensaje").toString().equals("si")) {

                                                                         SharedPreferences preferences=getSharedPreferences("credenciales",Context.MODE_PRIVATE);
                                                                         SharedPreferences.Editor editor=preferences.edit();
                                                                         editor.putString("usuario",usertx.getText().toString());
                                                                         editor.putString("password",passwordtx.getText().toString());
                                                                         editor.commit();

                                                                         Intent intent = new Intent(Login.this, MainActivity.class);
                                                                         startActivity(intent);


                                                                     } else {
                                                                         Toast.makeText(Login.this, "Datos icorrectos", Toast.LENGTH_SHORT)
                                                                                 .show();
                                                                     }


                                                                 } catch (JSONException e) {
                                                                     Log.d("gabo", "error de cath de peticion");
                                                                 }

                                                             }
                                                         }, new com.android.volley.Response.ErrorListener() {
                                                     @Override
                                                     public void onErrorResponse(VolleyError error) {

                                                         Toast.makeText(Login.this, "Error con api = " + error, Toast.LENGTH_SHORT).show();
                                                     }
                                                 }) {
                                                     @Override
                                                     protected Map<String, String> getParams() {

                                                         Map<String, String> params = new HashMap<String, String>();

                                                         params.put("usuario", usertx.getText().toString());
                                                         params.put("password", passwordtx.getText().toString());


                                                         return params;
                                                     }
                                                 };

                                                 queue.add(request);
                                                 Log.d("gabo", "mande la peticion");

                                             } else {
                                                 Log.d("gabo", "entre la huella");
                                                 biometricPrompt.authenticate(promptInfo);
                                             }


                                         }
                                     }
        );


    }
}