package com.example.comercios.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.Categorias;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragComercioCalificacion extends Fragment {
    JsonObjectRequest jsonObjectRequest;
    ArrayList<Integer> calificaciones;
    RatingBar ratingBarCali;

    public FragComercioCalificacion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_comercio_calificacion, container, false);
        ratingBarCali = (RatingBar)view.findViewById(R.id.FCalificacion_ratingBar);
        recuperarCalificacionesComercio();
        //calcularPromedioEstrellas();
        return view;
    }
    public void recuperarCalificacionesComercio(){
        calificaciones = new ArrayList<>();
        //GlobalComercios.getInstance().getComercio().getId();
        String url = Util.urlWebService + "/obtenerCalificaciones.php?id="+"6";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonCalificaciones= response.getJSONArray("calificaciones");
                    JSONObject objeto = jsonCalificaciones.getJSONObject(0);
                    String objeto2 = jsonCalificaciones.getJSONObject(1).getString("califiacion");
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Mensaje("Error al cargar las calificaciones");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
    public void calcularPromedioEstrellas(){
        double suma=0.0;
        double total=0.0;
        for(int i=0;i<calificaciones.size();i++){
            suma=suma+calificaciones.get(i);
        }
        total= suma/calificaciones.size();
        double redondeado = new BigDecimal(total).setScale(0, RoundingMode.HALF_EVEN).doubleValue();
        int x = (int)redondeado;
        ratingBarCali.setNumStars(x);

    }
    public void Mensaje(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
