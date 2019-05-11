package com.example.comercios.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Categorias;
import com.example.comercios.Modelo.Comercio;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragVerComerciosLista extends Fragment {

    private final int TAM_PAGINA = 10;
    private boolean inicial = true;
    private boolean userScrolled = false;
    private boolean cargando = false;
    private Handler manejador;

    private TabLayout tabLayout;
    private ListView listView;
    private ComercioListAdapter adapter;
    private View vistaInferior;

    private ArrayList<Categorias> categorias;
    private ArrayList<Comercio> comercios;

    public FragVerComerciosLista() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.frag_ver_comercios_lista, container, false);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vistaInferior = li.inflate(R.layout.vista_inferior_cargando, null);
        tabLayout = (TabLayout)view.findViewById(R.id.frag_ver_comercios_lista_tablayout);
        listView = (ListView)view.findViewById(R.id.frag_ver_comercios_lista_listview);
        categorias = new ArrayList();
        cargarCategorias();
        comercios = new ArrayList();
        manejador = new MyHandler();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                } else {
                    userScrolled = false;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(userScrolled && view.getLastVisiblePosition() == comercios.size()-1 && cargando == false){
                    cargando = true;
                    Thread thread = new ThreadMoreData();
                    thread.start();
                }
            }
        });
        return view;
    }
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    //Se agrega la vista de cargar mientras se busca mas datos
                    listView.addFooterView(vistaInferior);
                    break;
                case 1:
                    //Se actualizan los datos del adaptador y de la interfaaz
                    adapter.actualizarDatos();
                    listView.removeFooterView(vistaInferior);
                    cargando = false;
                    break;
                default:
                    break;

            }
        }
    }
    private class ThreadMoreData extends Thread {
        @Override
        public  void run(){
            manejador.sendEmptyMessage(0);
            obtenerMasDatos();
        }
    }

    private void obtenerMasDatos() {
        //Consultar a la base
        int idMinimo;
        if(comercios.size() == 0){
            idMinimo = 0;
        } else {
            idMinimo = (comercios.get(comercios.size()-1)).getId();
        }
        String query = "SELECT u.*, c.*, COUNT(ca.calificacion) cantidad, AVG(ca.calificacion) calificacion" +
                " FROM Comercios c INNER JOIN Usuarios u ON c.idUsuario = u.id" +
                " LEFT OUTER JOIN Calificaciones ca ON c.idUsuario = ca.idComercio WHERE c.idUsuario>'"+idMinimo+"' AND u.estado='1'";
        //Agregar fitros
        int idCategoria = (int)tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getTag();
        if(idCategoria != -1){
            query += " AND c.idCategoria='"+idCategoria+"'";
        }
        //Limite despues de los filtros
        query += " GROUP BY c.idUsuario ORDER BY u.usuario LIMIT " + TAM_PAGINA;
        String url = Util.urlWebService + "/comerciosObtener.php?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if(mensajeError.equalsIgnoreCase("")){
                        if(jsonOb.has("usuarios")) {
                            JSONArray users = jsonOb.getJSONArray("usuarios");
                            if (users.length() != 0) {
                                for (int i = 0; i < users.length(); i++) {
                                    JSONObject usuario = users.getJSONObject(i);
                                    comercios.add(new Comercio(
                                            usuario.getInt("id"),
                                            usuario.getInt("tipo"),
                                            usuario.getInt("calificacion"),
                                            usuario.getInt("cantidad"),
                                            usuario.getInt("verificado") == 1,
                                            usuario.getInt("estado") == 1,
                                            usuario.getString("correo"),
                                            usuario.getString("usuario"),
                                            usuario.getString("nombre"),
                                            usuario.getString("urlImagen") != null ? Util.urlWebService + "/" +usuario.getString("urlImagen") : null,
                                            usuario.getString("imagen") != null ? convertirStringToImg(usuario.getString("imagen")) : null));
                                }
                            }

                        }
                        if(inicial){
                            adapter = new ComercioListAdapter();;
                            listView.setAdapter(adapter);
                            inicial = false;
                        } else {
                            Message msg = manejador.obtainMessage(1);
                            manejador.sendMessage(msg);
                        }
                    } else {
                        mensajeToast(mensajeError);
                    }
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
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    private Bitmap convertirStringToImg(String imgCodificada) {
        byte[] stringDecodificado = Base64.decode(imgCodificada, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(stringDecodificado, 0, stringDecodificado.length);
        return decodedByte;
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
            case "Famarcia": return R.drawable.capsules;
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

    private class ComercioListAdapter extends ArrayAdapter<Comercio> {
        public ComercioListAdapter() {
            super(getActivity(), R.layout.item_gest_comercio, comercios);
        }
        public void actualizarDatos(){
            this.notifyDataSetChanged();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_gest_comercio, parent, false);
            }
            Comercio actual = comercios.get(position);
            // Fill the view
            TextView nombreTV = (TextView) itemView.findViewById(R.id.item_ver_comercio_nombre);
            TextView correoTV = (TextView) itemView.findViewById(R.id.item_ver_comercio_correo);
            TextView telefonoTV = (TextView) itemView.findViewById(R.id.item_ver_comercio_telefono);
            ImageView verificado = (ImageView) itemView.findViewById(R.id.item_ver_comercio_verificado);
            RatingBar rating = (RatingBar) itemView.findViewById(R.id.item_ver_comercio_rating);
            if(actual.getUrlImagen() != null){
                ImageView imagen = (ImageView) itemView.findViewById(R.id.item_ver_comercio_imageview);
                imagen.setImageBitmap(actual.getImagen());
            }
            if(!actual.isVerificado()){
                verificado.setVisibility(View.GONE);
            } else {
                verificado.setVisibility(View.VISIBLE);
            }

            return itemView;
        }
    }

    private void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};
}
