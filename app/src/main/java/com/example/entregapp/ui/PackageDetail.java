package com.example.entregapp.ui;

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
import android.graphics.ImageDecoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.entregapp.MainActivity;
import com.example.entregapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


public class PackageDetail extends Fragment {

    ImageButton imagen;
    String fotoname;
    String fotonameaux;

    Bitmap bitmap;
    public PackageDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_package_detail, container, false);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        EditText code = view.findViewById(R.id.code);
        EditText cordenadas = view.findViewById(R.id.cordenadas);
        EditText decripcin = view.findViewById(R.id.des);
         imagen = view.findViewById(R.id.imageTomar);
        cordenadas.setEnabled(false);
        Button getCordenadas = view.findViewById(R.id.getcord);
        Bundle datosRecuperados = getArguments();
        Button enviar = view.findViewById(R.id.enviar);
        Button eliminar = view.findViewById(R.id.eliminar);




        eliminar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String url = "https://ventanilla.softwaredatab.com/api/gabo/" + datosRecuperados.getInt("id");
                RequestQueue queue = Volley.newRequestQueue(getContext());

                // on below line we are calling a string
                // request method to post the data to our API
                // in this we are calling a post method.
                StringRequest request = new StringRequest(Request.Method.DELETE, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_SHORT)
                                        .show();

                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // method to handle errors.
                        Toast.makeText(getContext(), "Error con api = " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // below line we are creating a map for
                        // storing our values in key and value pair.
                        Map<String, String> params = new HashMap<String, String>();

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
        fotoname = "";

        enviar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                fotonameaux=fotoname;
                try {
                    guardarFoto(bitmap);
                }catch (Exception e){
    fotoname=fotonameaux;
                }
                String url = "https://ventanilla.softwaredatab.com/api/gabo/" + datosRecuperados.getInt("id");
                RequestQueue queue = Volley.newRequestQueue(getContext());
                Log.d("gabo", "esta es la url"+url);
                // on below line we are calling a string
                // request method to post the data to our API
                // in this we are calling a post method.
                StringRequest request = new StringRequest(Request.Method.PATCH, url,
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
                        // method to handle errors.
                        Toast.makeText(getContext(), "Error con api = " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // below line we are creating a map for
                        // storing our values in key and value pair.
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("codigo", code.getText().toString());
                        params.put("foto", fotoname);
                        params.put("cordenadas", cordenadas.getText().toString());
                        params.put("descripcion", decripcin.getText().toString());
                        Log.d("gaboFOTO", "uriImagen "+"/storage/emulated/0/" + fotoname);
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
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("gabo", "voy a llamar a la funcion");
                    tomarfoto();
                } else {
                    Log.d("gabo", "no puede  a llamar a la funcion");
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA },
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
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                            100);
                    Log.d("gabo", "no tengo permisos de cordenadas");
                    // TODO: Consider calling
                    // ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    // int[] grantResults)
                    // to handle the case where the user grants the permission. See the
                    // documentation
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

        Log.d("gabo", "llego el intent el code:  " +datosRecuperados.getInt("id"));
        String url = "https://ventanilla.softwaredatab.com/api/gabo/" + datosRecuperados.getInt("id");

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

                    code.setText(jsonObjec2.getString("codigo"));
                    decripcin.setText(jsonObjec2.getString("descripcion"));
                    cordenadas.setText(jsonObjec2.getString("cordenadas"));
                    fotoname=jsonObjec2.getString("foto");

                    File imgFile = new File("/storage/emulated/0/" + jsonObjec2.getString("foto"));
                    Log.d("gaboFOTO", "uriImagen "+"/storage/emulated/0/" + jsonObjec2.getString("foto"));
                    Uri imageUri = Uri.fromFile(imgFile);

                    Bitmap bitmap = null;
                    ContentResolver contentResolver = getContext().getContentResolver();
                    try {

                        ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, imageUri);
                        bitmap = ImageDecoder.decodeBitmap(source);

                    } catch (Exception e) {
                        Log.d("gabo", "encontra errror al vonvertir a image");
                    }

                    imagen.setImageBitmap(bitmap);
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
        Volley.newRequestQueue(getContext()).add(postRequest);

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
        // if(intent.resolveActivity(getPackageManager())!=null){
        Log.d("gabo", "entre al if de la funcion ");
        startActivityForResult(intent, 101);

        // }
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
                    new String[] { file.toString() }, null,
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
                .setContentText("Editaste la entrega un nuevo paquete clic para verlo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
        managerCompat.notify(1, builder.build());

    }
}