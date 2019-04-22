package com.example.comercios.Fragments;


        import android.graphics.Bitmap;
        import android.os.Bundle;
        import android.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.ImageRequest;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.example.comercios.Global.GlobalComercios;
        import com.example.comercios.Modelo.Categorias;
        import com.example.comercios.Modelo.Util;
        import com.example.comercios.Modelo.VolleySingleton;
        import com.example.comercios.R;
        import com.jaredrummler.materialspinner.MaterialSpinner;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.math.BigDecimal;
        import java.math.RoundingMode;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

        import androidx.appcompat.app.AppCompatActivity;

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

    JsonObjectRequest jsonObjectRequest3;
    ArrayList<Double> calificaciones;
    RatingBar ratingBarCali;

    public FragHomeComercio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mensajeAB("Shop World");
        View view = inflater.inflate(R.layout.frag_home_comercio, container, false);

        fotoComercioHome = (ImageView) view.findViewById(R.id.FHomComercio_ImgLocal);
        Usuario = (TextView) view.findViewById(R.id.FHomComercio_viewUsuario);
        Descripcion =(TextView) view.findViewById(R.id.FHomComercio_viewDescripcion);
        Categoria = (TextView)view.findViewById(R.id.FHomComercio_viewCategoria);
        Telefono = (TextView) view.findViewById(R.id.FHomComercio_viewTelefono);
        cargarCategorias2(view);
        cargarDatosAnteriores2(view);
        ratingBarCali = (RatingBar)view.findViewById(R.id.FHomComercio_ratingBar);
        recuperarCalificacionesComercio();
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
                Mensaje("Categorias-- " + error.toString());
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
    private void cargarDatosAnteriores2(View view) {
        //GlobalComercios.getInstance().getComercio().getId();
        String url = Util.urlWebService + "/obtenerInfoComercio.php?id="+ GlobalComercios.getInstance().getComercio().getId();

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
                                Categoria.setText(categorias2.get(i).getNombre());
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

    public void recuperarCalificacionesComercio(){
        calificaciones = new ArrayList<>();
        //GlobalComercios.getInstance().getComercio().getId();
        String url = Util.urlWebService + "/obtenerCalificaciones.php?id="+GlobalComercios.getInstance().getComercio().getId();

        jsonObjectRequest3 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonCalificaciones = response.getJSONArray("calificaciones");
                    JSONObject objeto;
                    for(int i= 0;i<jsonCalificaciones.length();i++) {
                        objeto= jsonCalificaciones.getJSONObject(i);
                        calificaciones.add(objeto.getDouble("calificacion"));
                    }
                    double suma=0;
                    for(int i=0;i<calificaciones.size();i++){
                        suma=suma+calificaciones.get(i);
                    }
                    float prueba = (float)(suma/calificaciones.size());
                    ratingBarCali.setRating((float)prueba);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Mensaje("Calificacioness" + error.toString());
            }
        });

        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest3);

    }

    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};

}
