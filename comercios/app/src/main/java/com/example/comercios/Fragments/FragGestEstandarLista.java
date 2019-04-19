package com.example.comercios.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.UsuarioEstandar;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragGestEstandarLista extends Fragment {
    private final int TAM_PAGINA = 10;
    private View vistaInferior;
    private ListView listView;
    private EstandarListAdapter adapter;
    private Handler manejador;
    private boolean inicial = true;
    private boolean cargando = false;
    private List<UsuarioEstandar> usuarios;
    public FragGestEstandarLista() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.frag_gest_estandar_lista, container, false);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vistaInferior = li.inflate(R.layout.vista_inferior_cargando, null);
        manejador = new MyHandler();
        usuarios = new ArrayList<UsuarioEstandar>();
        listView = (ListView) view.findViewById(R.id.gest_estandar_listview);
        obtenerMasDatos();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Revisa si el scroll llego al ultimo item
                if(view.getLastVisiblePosition() == usuarios.size()-1 && listView.getCount() >= TAM_PAGINA && cargando == false){
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
                    adapter.agregarUsuarios();
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
            //Agrega la vista inferior
            manejador.sendEmptyMessage(0);
            //Se buscan mas datos
            obtenerMasDatos();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = manejador.obtainMessage(1);
            manejador.sendMessage(msg);

        }
    }

    private class EstandarListAdapter extends ArrayAdapter<UsuarioEstandar> {
        public EstandarListAdapter() {
            super(getActivity(), R.layout.item_gest_estandar, usuarios);
        }
        public void agregarUsuarios(){
            this.notifyDataSetChanged();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_gest_estandar, parent, false);
            }
            UsuarioEstandar actual = usuarios.get(position);
            // Fill the view
            TextView userTV = (TextView)itemView.findViewById(R.id.item_gest_estandar_user);
            userTV.setText(actual.getUsuario());
            TextView correoTV = (TextView) itemView.findViewById(R.id.item_gest_estandar_correo);
            correoTV.setText(actual.getCorreo());
            TextView edadTV = (TextView) itemView.findViewById(R.id.item_gest_estandar_edad);
            edadTV.setText(actual.getEdad() + " años");
            MaterialCardView panel = (MaterialCardView) itemView.findViewById(R.id.item_gest_estandar_panel);
            panel.setTag(position);
            OnclickDelMaterialCardView(panel);
            return itemView;
        }
    }


    public void OnclickDelMaterialCardView(View view) {
        MaterialCardView miMaterialCardView = (MaterialCardView) view;
        miMaterialCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                UsuarioEstandar escogido = usuarios.get((int)v.getTag());
                GlobalUsuarios.getInstance().setUserE(escogido);
                //Reemplazo de fragment
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialCardView

    public void obtenerMasDatos() {
        //Consultar a la base
        int idMinimo;
        if(usuarios.size() == 0){
            idMinimo = 0;
        } else {
            idMinimo = (usuarios.get(usuarios.size()-1)).getId();
        }
        String query = "SELECT u.id, u.tipo, u.correo, u.usuario, u.estado, ue.fechaNac, TIMESTAMPDIFF(YEAR, ue.fechaNac, CURDATE()) as edad FROM Usuarios u, UsuariosEstandar ue WHERE u.id = ue.idUsuario AND u.id > '" + idMinimo + "'";
        //Agregar fitros
        //Limite despues de los filtros
        query += " ORDER BY u.id LIMIT " + TAM_PAGINA;
        String url = Util.urlWebService + "/usuariosEstandarObtener.php?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if(mensajeError.equalsIgnoreCase("")){
                        if(jsonOb.has("usuarios")) {
                            JSONArray users = jsonOb.getJSONArray("usuarios");
                            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                            if (users.length() != 0) {
                                for (int i = 0; i < users.length(); i++) {
                                    JSONObject usuario = users.getJSONObject(i);
                                usuarios.add(new UsuarioEstandar(
                                        usuario.getInt("id"),
                                        usuario.getInt("tipo"),
                                        usuario.getInt("edad"),
                                        usuario.getInt("estado") != 0,
                                        usuario.getString("correo"),
                                        usuario.getString("usuario"),
                                        formatoFecha.parse(usuario.getString("fechaNac"))));
                                }
                            }

                        }
                        if(inicial){
                            adapter = new EstandarListAdapter();;
                            listView.setAdapter(adapter);
                            inicial = false;
                        }
                    } else {
                        mensajeToast(mensajeError);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
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

    public void mensajeToast(String msg){ Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
