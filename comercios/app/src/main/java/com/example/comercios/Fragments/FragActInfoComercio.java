package com.example.comercios.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragActInfoComercio extends Fragment {
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    MaterialSpinner spinner;
    ArrayList<String> categoriasArray = new ArrayList<>();

    public FragActInfoComercio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_frag_act_info_comercio, container, false);
        cargarCategorias(view);
        OnclickDelButton(view.findViewById(R.id.fActInfoComercio_btnAct));
        EditText MiEditText = (EditText) view.findViewById(R.id.fActInfoComercio_edtUbicacion);
        MiEditText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Mensaje("aaaaaa");
            }
        });

        return view; // debe comentar el otro return

    }
    public void OnclickDelButton(View view) {

        Button miButton = (Button)  view;
        miButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.fActInfoComercio_btnAct:
                        Mensaje("Implementar Button1");

                        break;
                    default:break; }// fin de casos
            }
        });
    }
    public void Mensaje(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void cargarCategorias(View view){
        String url = Util.urlWebService + "/categoriasObtener.php";

       spinner = (MaterialSpinner) view.findViewById(R.id.fActInfoComercio_SPcategorias);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonCategorias= response.getJSONArray("categoria");
                    JSONObject obj;
                    for(int i= 0;i<jsonCategorias.length();i++) {
                        obj = jsonCategorias.getJSONObject(i);
                        String atr1 = obj.getString("id");
                        String atr2 = obj.getString("nombre");
                        categoriasArray.add(atr2);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Mensaje("No se puede conectar " + error.toString());
            }
        });
        spinner.setItems(categoriasArray);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Mensaje(item);
            }
        });

        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

}
