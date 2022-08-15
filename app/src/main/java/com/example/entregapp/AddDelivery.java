package com.example.entregapp;

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
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddDelivery extends Fragment {

    ImageButton imagen;
    String fotoname;
    Bitmap bitmap;

    String clienteSelected;
    String paqueteSelected;



    public AddDelivery() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_add_delivery, container, false);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Spinner clientessp=view.findViewById(R.id.cliente);
        Spinner paquetessp=view.findViewById(R.id.paquete);
        ArrayList<String> clientes;
        ArrayList<String> paquetes;
        clientes = new ArrayList<String>();
        paquetes = new ArrayList<String>();

        getClinetes("",clientessp,clientes);
        getPaquetes("",paquetessp,paquetes);

        EditText cordenadas = view.findViewById(R.id.direcion);

        EditText descripcion = view.findViewById(R.id.Telefono);


        cordenadas.setEnabled(false);

        Button getCordenadas = view.findViewById(R.id.getcord);
        imagen = view.findViewById(R.id.imageTomar);
        Button enviar = view.findViewById(R.id.enviar);
        fotoname = "";

        Log.d("gabo", "cargue el intent ");

        enviar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                guardarFoto(bitmap);
                String url = "https://ventanilla.softwaredatab.com/api/gabo";
                RequestQueue queue = Volley.newRequestQueue(getContext());
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject jsonObjec2 = new JSONObject(jsonObject.getString("entrega"));

                                    notificaacion(jsonObjec2.getInt("identrega"));

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

                        params.put("cliente", clienteSelected);
                        params.put("codigo", paqueteSelected);

                        params.put("foto", fotoname);
                        params.put("cordenadas", cordenadas.getText().toString());
                        params.put("descripcion", descripcion.getText().toString());

                        return params;
                    }
                };

                queue.add(request);
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("gabo", "entre al boton imagen");
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("gabo", "voy a llamar a la funcion");
                    tomarfoto();
                } else {
                    Log.d("gabo", "no puede  a llamar a la funcion");
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                            100);

                }
            }
        });

        getCordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("gabo", "entre al boton cordenadas");
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            100);
                    Log.d("gabo", "no tengo permisos de cordenadas");
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
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            /*
             * int ancho = 500;
             * int alto = 500;
             * LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho,
             * alto);
             * imagen.setLayoutParams(params);
             */
            imagen.setImageBitmap(bitmap);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
        Log.d("gabo", "entre al if de la funcion ");
        startActivityForResult(intent, 101);
    }

    public void guardarFoto(Bitmap bitmap) {
        OutputStream outputStream = null;
        File file = null;

        ContentResolver contentResolver = getActivity().getContentResolver();
        ContentValues contentValues = new ContentValues();
        String filename = System.currentTimeMillis() + "_image";
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/proyecto_mobiles");
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 1);
        fotoname = "Pictures/proyecto_mobiles/" + filename + ".jpg";
        Uri colection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri imageuri = contentResolver.insert(colection, contentValues);
        Log.d("gabo", "uri de imagen " + imageuri.toString());
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
            Toast.makeText(getContext(), "la imagen se guardo en el dispositivo", Toast.LENGTH_SHORT).show();
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

            MediaScannerConnection.scanFile(getContext(),
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri_local) {
                            Log.i("gabo", "Scanned " + path + ":");
                            Log.i("gabo", "-> uri=" + uri_local);
                        }
                    });
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
                .setContentTitle("Nuevo paquete entregado")
                .setContentText("entregaste un nuevo paquete clic para verlo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
        managerCompat.notify(1, builder.build());

    }

    public void getClinetes(String query, Spinner clientessp,ArrayList<String> clientes) {
        Log.d("gaboF", "SE CARGO la info");


        String url2 = "https://ventanilla.softwaredatab.com/api/cliente?query=" + query;
        StringRequest postRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonObjec2 = new JSONArray(jsonObject.getString("clientes"));
                    clientes.clear();
                    clientes.add("Selecciona cliente");
                    for (int i = 0; i < jsonObjec2.length(); i++) {
                        JSONObject obj = jsonObjec2.getJSONObject(i);

                        clientes.add(obj.getString("nombre"));


                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,clientes);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clientessp.setAdapter(arrayAdapter);
                    clientessp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                         clienteSelected=adapterView.getItemAtPosition(i).toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    Log.d("gabo", response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("gabo", "error en segunda peticion" + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("gabo", error.toString());
            }
        });
        Volley.newRequestQueue(getContext()).add(postRequest2);


    }
    public void getPaquetes(String query, Spinner paquetessp,ArrayList<String> paquetes) {
        Log.d("gaboF", "SE CARGO la info");


        String url2 = "https://ventanilla.softwaredatab.com/api/paquete?query=" + query;
        StringRequest postRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonObjec2 = new JSONArray(jsonObject.getString("paquete"));
                    paquetes.clear();
                    paquetes.add("Selecciona paquete");
                    for (int i = 0; i < jsonObjec2.length(); i++) {
                        JSONObject obj = jsonObjec2.getJSONObject(i);

                        paquetes.add(obj.getString("codigo"));


                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,paquetes);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    paquetessp.setAdapter(arrayAdapter);
                    paquetessp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            paqueteSelected=adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    Log.d("gabo", response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("gabo", "error en segunda peticion" + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("gabo", error.toString());
            }
        });
        Volley.newRequestQueue(getContext()).add(postRequest2);


    }

}