package com.example.comercios.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Modelo.Categorias;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragEmpresasMaps extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mapView;
    private ArrayList<Categorias> categorias;
    private TabLayout tabLayout;

    public FragEmpresasMaps() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_empresas_maps, container, false);
        categorias = new ArrayList<>();
        cargarCategorias();
        tabLayout = (TabLayout)view.findViewById(R.id.fragEmpMap_menuNavigationTab);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.mapa_contenedor);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        MapsInitializer.initialize(getActivity());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247, -74.044502)).title("Estatua de la libertad").snippet("Espero ir un dia"));
        CameraPosition liberty =  CameraPosition.builder().target(new LatLng(40.689247, -74.044502)).zoom(16).bearing(0).tilt(45).build();
    }

    private void cargarCategorias(){
        String url = Util.urlWebService + "/categoriasObtener.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonCategorias= response.getJSONArray("categoria");
                    JSONObject obj;
                    for(int i= 0;i<jsonCategorias.length();i++) {
                        obj = jsonCategorias.getJSONObject(i);
                        categorias.add(new Categorias(obj.getInt("id"),obj.getString("nombre")));
                    }
                    cargarTabLayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("No se puede conectar " + error.toString());
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void cargarTabLayout(){
        if(categorias != null){
            TabLayout.Tab todos = tabLayout.newTab();
            todos.setText("Todos");
            todos.setIcon(recuperarIcono("Todos"));
            todos.setTag(-1);
            tabLayout.addTab(todos);
            for(Categorias c: categorias){
                TabLayout.Tab t = tabLayout.newTab();
                t.setText(c.getNombre());
                t.setIcon(recuperarIcono(c.getNombre()));
                t.setTag(c.getId());
                tabLayout.addTab(t);
            }
        }
    }
    private int recuperarIcono(String categoria){
        switch (categoria){
            case "Todos": return R.drawable.store_alt;
            case "Bar": return R.drawable.glass_martini_alt;
            case "Cafe": return R.drawable.coffee;
            case "Deportes": return R.drawable.bicycle;
            case "Farmacia": return R.drawable.capsules;
            case "Ferreteria": return R.drawable.hammer;
            case "Hotel": return R.drawable.hotel;
            case "Jugueteria": return R.drawable.robot;
            case "Libreria": return R.drawable.book;
            case "Musica": return R.drawable.guitar;
            case "Restaurante": return R.drawable.utensils;
            case "Ropa": return R.drawable.tshirt;
            case "Tecnologia": return R.drawable.laptop;
            case "Videojuegos": return R.drawable.gamepad;
            case "Zapateria": return R.drawable.shoe_prints;
            case "Otro": return R.drawable.shopping_cart;
            default: return -1;
        }
    }

    private void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};

}
