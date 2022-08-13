package com.example.proyecto_moviles2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddPackage extends AppCompatActivity {
    ImageButton imagen;
    String fotoname;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);
        LocationManager locationManager = (LocationManager) AddPackage.this.getSystemService(Context.LOCATION_SERVICE);

        EditText cordenadas = findViewById(R.id.cordenadas);
        EditText codigo = findViewById(R.id.code);
        EditText descripcion = findViewById(R.id.des);


        cordenadas.setEnabled(false);

        Button getCordenadas = findViewById(R.id.getcord);

        imagen = findViewById(R.id.imageTomar);
        Button enviar = findViewById(R.id.enviar);
        fotoname = "";


        Log.d("gabo", "cargue el intent ");

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarFoto(bitmap);
                String url = "https://ventanilla.softwaredatab.com/api/gabo";
                RequestQueue queue = Volley.newRequestQueue(AddPackage.this);

                // on below line we are calling a string
                // request method to post the data to our API
                // in this we are calling a post method.
                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        // on below line we are displaying a success toast message.
                        Toast.makeText(AddPackage.this, "Datos enviados al servidor", Toast.LENGTH_SHORT).show();

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // method to handle errors.
                        Toast.makeText(AddPackage.this, "Error con api = " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // below line we are creating a map for
                        // storing our values in key and value pair.
                        Map<String, String> params = new HashMap<String, String>();


                        params.put("codigo", codigo.getText().toString());
                        params.put("foto", fotoname);
                        params.put("cordenadas", cordenadas.getText().toString());
                        params.put("descripcion", descripcion.getText().toString());


                        // at last we are
                        // returning our params.
                        return params;
                    }
                };
                // below line is to make
                // a json object request.
                queue.add(request);


            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("gabo", "entre al boton imagen");
                if (ActivityCompat.checkSelfPermission(AddPackage.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("gabo", "voy a llamar a la funcion");
                    tomarfoto();
                } else {
                    Log.d("gabo", "no puede  a llamar a la funcion");
                    ActivityCompat.requestPermissions(AddPackage.this, new String[]{Manifest.permission.CAMERA}, 100);

                }
            }
        });


        getCordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("gabo", "entre al boton cordenadas");
                if (ActivityCompat.checkSelfPermission(AddPackage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddPackage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        cordenadas.setText("" + location.getLatitude() + " " + location.getLongitude());
                    }
                };
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            int ancho = 500;
            int alto = 500;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
            imagen.setLayoutParams(params);

            imagen.setImageBitmap(bitmap);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tomarfoto();

            }
        }
    }

    public void tomarfoto() {
        Log.d("gabo", "entre a la funcion ");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if(intent.resolveActivity(getPackageManager())!=null){
        Log.d("gabo", "entre al if de la funcion ");
        startActivityForResult(intent, 101);

        //}
    }

    public void guardarFoto(Bitmap bitmap) {
        OutputStream outputStream = null;
        File file = null;

        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        String filename = System.currentTimeMillis() + "_image";
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/proyecto_mobiles");
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 1);
        fotoname = "Pictures/proyecto_mobiles/" + filename + ".jpg";
        Uri colection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri imageuri = contentResolver.insert(colection, contentValues);
        Log.d("gabo", "llegue aqui");
        try {
            outputStream = contentResolver.openOutputStream(imageuri);
        } catch (Exception e) {
            Log.d("gabo", "error guardando la foto " + e.toString());
        }
        contentValues.clear();
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0);
        contentResolver.update(imageuri, contentValues, null, null);
        boolean save = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        if (save) {
            Toast.makeText(AddPackage.this, "la imagen se guardo en el dispositivo", Toast.LENGTH_SHORT).show();
        }

        if (outputStream != null) {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                Log.d("gabo", "error cerarndo el bufer la foto " + e.toString());
            }


        }
        if (file != null) {

            MediaScannerConnection.scanFile(this,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri_local) {
                            Log.i("gabo", "Scanned " + path + ":");
                            Log.i("gabo", "-> uri=" + uri_local);
                        }
                    });
        }
    }

}