package com.example.entregapp.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.entregapp.AddDelivery;
import com.example.entregapp.AddPackage;
import com.example.entregapp.DeliveryDetail;
import com.example.entregapp.PackageDetail;
import com.example.entregapp.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.entregapp.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public ScrollView scrollView ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("gaboF", "SE CARGO EL FRAGMANENT");

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

         scrollView = root.findViewById(R.id.scroll);
        FloatingActionButton nuevo = root.findViewById(R.id.nuevo);
        TextView clima = root.findViewById(R.id.clima);
        EditText buscar = root.findViewById(R.id.buscar);

       LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);

        String url = "http://api.weatherstack.com/current?access_key=3c0041e95cf6d4489f0c1f9ace158f48&query=guadalajara";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjec2 = new JSONObject(jsonObject.getString("current"));

                    clima.setText(jsonObjec2.getString("temperature") + "°C");
                    Log.d("gabo", response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("gabo", error.toString());
            }
        });
        Volley.newRequestQueue(getContext()).add(postRequest);
            getInfo(linearLayout, buscar.getText().toString());
        Log.d("gaboF", "SE CARGO la info desde caga de fragmnet");

        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment addDelivery = new AddDelivery();
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, addDelivery).commit();



            }
        });
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {




                getInfo(linearLayout, buscar.getText().toString());
                Log.d("gaboF", "SE CARGO la info desde after text");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scrollView.removeAllViews();
        scrollView.removeAllViewsInLayout();

        binding = null;
    }

    private CardView cards(String codigo, String cordenada, String descripcion, int id) {
        TextView item = new TextView(getContext());
        item.setText(codigo);
        item.setPadding(15, 15, 15, 15);

        TextView item2 = new TextView(getContext());
        item2.setText(cordenada);
        item2.setPadding(15, 15, 15, 15);

        CardView card = new CardView(getContext());

        card.setMinimumWidth(700);
        card.setMinimumHeight(90);
        card.setPadding(20, 5, 20, 5);
        card.setId(id);


        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(item);
        linearLayout.addView(item2);

        card.addView(linearLayout);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("gabo", "se cliqueo el " + card.getId());
                Fragment deliveryDetail = new DeliveryDetail();


                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

                Bundle parmetros = new Bundle();
                parmetros.putInt("id", card.getId());
                deliveryDetail.setArguments(parmetros);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, deliveryDetail ).commit();



            }
        });
        return card;


    }

    public void getInfo(LinearLayout linearLayout, String query) {
        Log.d("gaboF", "SE CARGO la info");
        linearLayout.removeAllViews();
        linearLayout.removeAllViewsInLayout();

        String url2 = "https://ventanilla.softwaredatab.com/api/gabo?query=" + query;
        StringRequest postRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonObjec2 = new JSONArray(jsonObject.getString("entregas"));
                    scrollView.removeAllViewsInLayout();
                    linearLayout.removeAllViewsInLayout();
                    for (int i = 0; i < jsonObjec2.length(); i++) {
                        JSONObject obj = jsonObjec2.getJSONObject(i);



                        linearLayout.addView(cards(obj.getString("codigo"), obj.getString("cordenadas"), obj.getString("descripcion"), obj.getInt("identrega")));

                    }
                    scrollView.addView(linearLayout);

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