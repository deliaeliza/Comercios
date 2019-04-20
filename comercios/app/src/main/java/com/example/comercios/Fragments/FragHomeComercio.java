package com.example.comercios.Fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Modelo.Categorias;
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
public class FragHomeComercio extends Fragment {

    JsonObjectRequest jsonObjectRequest;
    JsonObjectRequest jsonObjectRequest2;

    ImageView fotoComercioHome;
    TextView Usuario, Descripcion,Telefono,Categoria;
    ArrayList<Categorias> categorias2;
    String SUrlImagen;

    public FragHomeComercio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_act_info_comercio, container, false);

        fotoComercioHome = (ImageView) view.findViewById(R.id.FHomComercio_ImgLocal);
        Usuario = (TextView) view.findViewById(R.id.FHomComercio_viewUsuario);
        Descripcion =(TextView) view.findViewById(R.id.FHomComercio_viewDescripcion);
        Categoria = (TextView)view.findViewById(R.id.FHomComercio_viewCategoria);
        Telefono = (TextView) view.findViewById(R.id.FHomComercio_viewTelefono);
        //cargarCategorias2(view);
        //cargarDatosAnteriores2(view);
        return view;
    }
    public void cargarCategorias2(View view){

        categorias2 = new ArrayList<>();
        String url = Util.urlWebService + "/categoriasObtener.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonCategorias= response.getJSONArray("categoria");
                    JSONObject obj;
                    for(int i= 0;i<jsonCategorias.length();i++) {
                        obj = jsonCategorias.getJSONObject(i);
                        categorias2.add(new Categorias(obj.getInt("id"),obj.getString("nombre")));
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
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
    private void cargarDatosAnteriores2(View view) {

        //GlobalComercios.getInstance().getComercio().getId();
        String url = Util.urlWebService + "/obtenerInfoComercio.php?id="+"6";

        jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String r= String.valueOf(response.getJSONObject("comercio"));
                    if(r!="") {
                        JSONObject jsonComercio = response.getJSONObject("comercio");
                        Usuario.setText(jsonComercio.getString("usuario"));
                        Descripcion.setText(jsonComercio.getString("descripcion"));
                        Telefono.setText(jsonComercio.getString("telefono"));

                        for(int i=0;i<categorias2.size();i++){
                            if(categorias2.get(i).getId() == Integer.parseInt(jsonComercio.getString("idCategoria"))){
                                //Categoria.setText(categorias2.get(i).getNombre());
                            }
                        }

                        SUrlImagen =jsonComercio.getString("urlImagen");
                        String ruta_foto= Util.urlWebService +"/"+SUrlImagen;
                        cargarWebServicesImagen2(ruta_foto);
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
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest2);

    }
    private void cargarWebServicesImagen2(String ruta_foto) {
        ImageRequest imagR = new ImageRequest(ruta_foto, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                fotoComercioHome.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Mensaje("error al cargar la imagen");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(imagR);
    }
    public void Mensaje(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
