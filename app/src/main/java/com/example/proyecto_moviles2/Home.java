package com.example.proyecto_moviles2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ScrollView scrollView=findViewById(R.id.scroll);

        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(cards("hola","dos","wss",1));
        linearLayout.addView(cards("hola","dos","wss",2));
        linearLayout.addView(cards("hola","dos","wss",3));
        linearLayout.addView(cards("hola","dos","wss",4));
        linearLayout.addView(cards("hola","dos","wss",5));
        linearLayout.addView(cards("hola","dos","wss",6));

        scrollView.addView(linearLayout);

        try {
            scrollView.addView(cards("hossla","dossss","wsssss",2));
        }catch (Exception e)
        {
            Log.d("gabo", e.toString());
        }



        String url="http://api.weatherstack.com/current?access_key=3c0041e95cf6d4489f0c1f9ace158f48&query=guadalajara";

        StringRequest postRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //TU eres BEBESITA eres BEBESOTAAAA
                    //Mami sube algo
                    //dame contenido
                    //ese CULO
                    //sube
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject jsonObjec2=new JSONObject(jsonObject.getString("current"));

                    //  txt_login.setText(jsonObjec2.getString("temperature"));
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
        //lo mas seguido
        //pa que to
        //el mundo
        //vea
        //lo rika k tu esta


    }

    private CardView cards(String uno, String dos, String tres,int id){
        TextView item =new TextView(this);
        item.setText("holiiii");
        item.setPadding(15,15,15,15);

        TextView item2 =new TextView(this);
        item2.setText("holiiii12");
        item2.setPadding(15,15,15,15);

        CardView card=new CardView(this);

        card.setMinimumWidth(700);
        card.setMinimumHeight(90);
        card.setPadding(20,5,20,5);
        card.setId(id);

        //card.setCardBackgroundColor(Color.parseColor("#FFFF"));

        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(item);
        linearLayout.addView(item2);

        card.addView(linearLayout);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("gabo", "se cliqueo el "+card.getId());
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