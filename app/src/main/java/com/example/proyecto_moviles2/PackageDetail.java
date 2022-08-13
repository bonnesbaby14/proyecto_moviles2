package com.example.proyecto_moviles2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PackageDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);

        TextView code=findViewById(R.id.code);
        TextView cordenadas=findViewById(R.id.cordenadas);
        TextView decripcin=findViewById(R.id.descripcion);



        Log.d("gabo", "llego el intent el code:  "+getIntent().getExtras().getInt("id"));
        String url = "https://ventanilla.softwaredatab.com/api/gabo/"+getIntent().getExtras().getInt("id");

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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

                    code.setText("Codigo: "+jsonObjec2.getString("codigo"));
                    decripcin.setText("Descripción: "+jsonObjec2.getString("descripcion"));
                    cordenadas.setText("Cordenadas: "+jsonObjec2.getString("cordenadas"));
                    Log.d("gabo", response.toString());

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