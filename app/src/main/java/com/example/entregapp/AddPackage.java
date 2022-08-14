package com.example.entregapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddPackage extends Fragment {



    public AddPackage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_package, container, false);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        EditText codigo = view.findViewById(R.id.codigo);
        EditText nombre = view.findViewById(R.id.nombre);
        EditText peso = view.findViewById(R.id.peso);




        Button enviar = view.findViewById(R.id.enviar);

        Log.d("gabo", "cargue el intent ");

        enviar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String url = "https://ventanilla.softwaredatab.com/api/paquete";
                RequestQueue queue = Volley.newRequestQueue(getContext());
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject jsonObjec2 = new JSONObject(jsonObject.getString("paquete"));

                                    notificaacion(jsonObjec2.getInt("idpaquete"));

                                    Toast.makeText(getContext(), "Datos enviados al servidor", Toast.LENGTH_SHORT)
                                            .show();

                                } catch (JSONException e) {

                                }

                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "Error con api = " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();

                        params.put("codigo", codigo.getText().toString());

                        params.put("nombre", nombre.getText().toString());
                        params.put("peso", peso.getText().toString());

                        return params;
                    }
                };

                queue.add(request);
            }
        });




        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data != null) {


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }






    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notificaacion(int id) {

        Intent intent = new Intent(getContext(), MainActivity.class);

        Bundle parmetros = new Bundle();
        parmetros.putInt("id", id);
        intent.putExtras(parmetros);
        TaskStackBuilder builder2 = TaskStackBuilder.create(getContext());
        builder2.addParentStack(MainActivity.class);
        builder2.addNextIntent(intent);
        PendingIntent pendingIntent = builder2.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel notificationChannel = new NotificationChannel("canal", "nuevo",
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "canal")
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setContentTitle("Nuevo paquete agregafo")
                .setContentText("Agrgaste un paquete")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
        managerCompat.notify(1, builder.build());

    }


}