package com.example.comercios.Fragments;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.Seccion;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragVerComercio extends Fragment {

    TabLayout tabLayout;
    ConstraintLayout contenedor;
    ArrayList<Seccion> secciones;

    public FragVerComercio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.frag_ver_comercio, container, false);
        GlobalUsuarios.getInstance().setVentanaActual(R.layout.frag_ver_comercio);
        tabLayout = (TabLayout)view.findViewById(R.id.frag_ver_comercio_tablayout);
        contenedor = (ConstraintLayout)view.findViewById(R.id.frag_ver_comercio_contenedor);
        secciones = new ArrayList();
        cargarSecciones();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                if((int)tab.getTag() == -1){
                    FragVerComercioInfo mifrag = new FragVerComercioInfo();
                    fragmentTransaction.replace(R.id.frag_ver_comercio_contenedor, mifrag, "vercomercioinfo_usuarioEstandar");
                } else {
                    GlobalUsuarios.getInstance().setIdSeccion((int)tab.getTag());
                    FragVerProductosGrid mifrag = new FragVerProductosGrid();
                    fragmentTransaction.replace(R.id.frag_ver_comercio_contenedor, mifrag, "vercomercioproductos_usuarioEstandar");
                }
                fragmentTransaction.commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }

    private void cargarSecciones(){
        String query = "SELECT s.*, COUNT(sp.idSeccion) cantidad FROM Secciones s INNER JOIN SeccionesProductos sp ON s.id = sp.idSeccion GROUP BY s.id;";
        String url = Util.urlWebService + "/seccionesObtener.php?query="+query;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Seccion porDefecto = null;
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if(mensajeError.equalsIgnoreCase("")){
                        if(jsonOb.has("secciones")) {
                            JSONArray users = jsonOb.getJSONArray("secciones");
                            for (int i = 0; i < users.length(); i++) {
                                JSONObject usuario = users.getJSONObject(i);
                                if(!usuario.getString("nombre").equalsIgnoreCase("DEFAULT")){
                                    secciones.add(new Seccion(usuario.getInt("id"), usuario.getString("nombre")));
                                } else {
                                    porDefecto = new Seccion(usuario.getInt("id"), usuario.getString("nombre"));
                                }
                            }
                            if(porDefecto != null)
                                secciones.add(porDefecto);
                        }
                    } else {
                        mensajeToast(mensajeError);
                    }
                    cargarTabLayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Error, intentelo más tarde");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
    private void cargarTabLayout(){
        if(secciones != null){
            TabLayout.Tab inicio = tabLayout.newTab();
            inicio.setIcon(R.drawable.home);
            inicio.setTag(-1);
            tabLayout.addTab(inicio);
            if(secciones.size() > 0){
                for(Seccion s: secciones){
                    TabLayout.Tab t = tabLayout.newTab();
                    if(s.getNombre().equalsIgnoreCase("DEFAULT")){
                        if(secciones.size() > 1){
                            t.setText("Más");
                        } else {
                            t.setText(nombreDefaultPorCategoria(GlobalUsuarios.getInstance().getComercio().getCategoria()));
                        }
                    } else {
                        t.setText(s.getNombre());
                    }
                    t.setTag(s.getId());
                    tabLayout.addTab(t);
                }
            }
        }
    }
    private String nombreDefaultPorCategoria(String categoria){
        switch (categoria){
            case "Restaurante": return "Menú";
            case "Bar": return "Bebidas";
            case "Libreria": return "Libros";
            default:return "Productos";
        }
    }

    public void mensajeToast(String msg){ Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};
}
