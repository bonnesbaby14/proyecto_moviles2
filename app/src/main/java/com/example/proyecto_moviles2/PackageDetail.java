package com.example.proyecto_moviles2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import javax.net.ssl.ManagerFactoryParameters;

public class PackageDetail extends AppCompatActivity {
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);

        TextView code = findViewById(R.id.code);
        TextView cordenadas = findViewById(R.id.cordenadas);
        TextView decripcin = findViewById(R.id.descripcion);
        ImageView imageView = findViewById(R.id.image);


        Log.d("gabo", "llego el intent el code:  " + getIntent().getExtras().getInt("id"));
        String url = "https://ventanilla.softwaredatab.com/api/gabo/" + getIntent().getExtras().getInt("id");

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onResponse(String response) {
                try {
                    //TU eres BEBESITA eres BEBESOTAAAA
                    //Mami sube algo
                    //dame contenido
                    //ese CULO
                    //sube
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjec2 = new JSONObject(jsonObject.getString("entrega"));

                    code.setText("Codigo: " + jsonObjec2.getString("codigo"));
                    decripcin.setText("Descripción: " + jsonObjec2.getString("descripcion"));
                    cordenadas.setText("Cordenadas: " + jsonObjec2.getString("cordenadas"));

                    File imgFile = new File("/storage/emulated/0/" + jsonObjec2.getString("foto"));
                    Uri imageUri = Uri.fromFile(imgFile);

                    Bitmap bitmap = null;
                    ContentResolver contentResolver = getContentResolver();
                    try {

                        ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, imageUri);
                        bitmap = ImageDecoder.decodeBitmap(source);

                    } catch (Exception e) {
                        Log.d("gabo", "encontra errror al vonvertir a image");
                    }

                    imageView.setImageBitmap(bitmap);
                    Log.d("gabo", imageUri.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                    // txt_login.setText("no se puedo");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("gabo", error.toString());
            }
        });
        Volley.newRequestQueue(PackageDetail.this).add(postRequest);
    }




}