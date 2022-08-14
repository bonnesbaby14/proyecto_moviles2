package com.example.entregapp.ui.gallery;

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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.entregapp.AddPackage;
import com.example.entregapp.PackageDetail;
import com.example.entregapp.R;
import com.example.entregapp.databinding.FragmentGalleryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //  final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        ScrollView scrollView = root.findViewById(R.id.scroll);
        FloatingActionButton button = root.findViewById(R.id.nuevo);
        TextView clima = root.findViewById(R.id.clima);
        EditText buscar=root.findViewById(R.id.buscar);

        LinearLayout linearLayout = new LinearLayout(getContext());
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

                    clima.setText(jsonObjec2.getString("temperature") + "°C");
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
        Volley.newRequestQueue(getContext()).add(postRequest);
        getInfo(linearLayout,buscar.getText().toString());

        //lo mas seguido
        //pa que to
        //el mundo
        //vea
        //lo rika k tu esta

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment addPackage = new AddPackage();


                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();


                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, addPackage).commit();
                fragmentTransaction.addToBackStack(null);

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
                linearLayout.removeAllViews();
                linearLayout.removeAllViewsInLayout();
                getInfo(linearLayout,buscar.getText().toString());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

        //card.setCardBackgroundColor(Color.parseColor("#FFFF"));

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(item);
        linearLayout.addView(item2);

        card.addView(linearLayout);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("gabo", "se cliqueo el " + card.getId());
                Fragment packageDetail = new PackageDetail();


                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();


                Bundle parmetros = new Bundle();
                parmetros.putInt("id", card.getId());
                packageDetail.setArguments(parmetros);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, packageDetail).commit();
                fragmentTransaction.addToBackStack(null);

                //intent.putExtras(parmetros);
                //startActivity(intent);

            }
        });
        return card;


    }

    public void getInfo(LinearLayout linearLayout, String query){
        String url2 = "https://ventanilla.softwaredatab.com/api/gabo?query="+query;
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
        Volley.newRequestQueue(getContext()).add(postRequest2);


    }
}