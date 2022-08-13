package com.example.proyecto_moviles2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ScrollView scrollView = findViewById(R.id.scroll);
        FloatingActionButton button = findViewById(R.id.nuevo);
        TextView clima=findViewById(R.id.clima);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        scrollView.addView(linearLayout);

        try {
            scrollView.addView(cards("hossla", "dossss", "wsssss", 2));
        } catch (Exception e) {
            Log.d("gabo", e.toString());
        }


        String url = "http://api.weatherstack.com/current?access_key=3c0041e95cf6d4489f0c1f9ace158f48&query=guadalajara";
        String url2 = "https://ventanilla.softwaredatab.com/api/gabo";

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
                    JSONObject jsonObjec2 = new JSONObject(jsonObject.getString("current"));

                      clima.setText(jsonObjec2.getString("temperature")+"°C");
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
        Volley.newRequestQueue(Home.this).add(postRequest);

        StringRequest postRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //TU eres BEBESITA eres BEBESOTAAAA
                    //Mami sube algo
                    //dame contenido
                    //ese CULO
                    //sube
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonObjec2 = new JSONArray(jsonObject.getString("entregas"));
                    for (int i = 0; i < jsonObjec2.length(); i++) {
                        JSONObject obj = jsonObjec2.getJSONObject(i);
                        linearLayout.addView(cards(obj.getString("codigo"), obj.getString("cordenadas"), obj.getString("descripcion"), obj.getInt("identrega")));

                    }


                    //  txt_login.setText(jsonObjec2.getString("temperature"));
                    Log.d("gabo", response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("gabo", "error en segunda peticion" + e.toString());
                    // txt_login.setText("no se puedo");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("gabo", error.toString());
            }
        });
        Volley.newRequestQueue(Home.this).add(postRequest2);
        //lo mas seguido
        //pa que to
        //el mundo
        //vea
        //lo rika k tu esta

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AddPackage.class);


                startActivity(intent);
            }
        });

    }

    private CardView cards(String codigo, String cordenada, String descripcion, int id) {
        TextView item = new TextView(this);
        item.setText(codigo);
        item.setPadding(15, 15, 15, 15);

        TextView item2 = new TextView(this);
        item2.setText(cordenada);
        item2.setPadding(15, 15, 15, 15);

        CardView card = new CardView(this);

        card.setMinimumWidth(700);
        card.setMinimumHeight(90);
        card.setPadding(20, 5, 20, 5);
        card.setId(id);

        //card.setCardBackgroundColor(Color.parseColor("#FFFF"));

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(item);
        linearLayout.addView(item2);

        card.addView(linearLayout);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("gabo", "se cliqueo el " + card.getId());
                Intent intent = new Intent(Home.this, PackageDetail.class);

                Bundle parmetros = new Bundle();
                parmetros.putInt("id", card.getId());
                intent.putExtras(parmetros);
                startActivity(intent);

            }
        });
        return card;


    }


}