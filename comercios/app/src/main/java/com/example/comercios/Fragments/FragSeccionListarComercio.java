package com.example.comercios.Fragments;


import android.app.Dialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.VoiceInteractor;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Filtros.FiltrosSeccion;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Seccion;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragSeccionListarComercio extends Fragment {
    //private final int TAM_PAGINA = 10;

    //private boolean inicial = true;
    //private boolean cargando = false;
    //private boolean userScrolled = false;
    private View vistaInferior;
    private ListView listView;
    private SeccionListAdapter adapter;
    //private Handler manejador;
    private List<Seccion> secciones;
    private Dialog dialog;
    public FragSeccionListarComercio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mensajeAB("Secciones");
        View view =inflater.inflate(R.layout.frag_seccion_listar_comercio, container, false);
        //LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //manejador = new MyHandler();
        secciones = new ArrayList<Seccion>();
        listView = (ListView) view.findViewById(R.id.sec_listar_listview);
        vistaInferior = view.findViewById(R.id.sec_listar_cargando);
        DialogoFiltros();
        obtenerMasDatos();
        OnclickDelMaterialButton(view.findViewById(R.id.sec_listar_MaterialButtonFiltrar));
        OnclickDelMaterialButton(view.findViewById(R.id.sec_listar_MaterialButtonTodos));

        /*listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentFirstVisibleItem;
            private int totalItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && scrollState == SCROLL_STATE_IDLE) {
                    Thread thread = new ThreadMoreData();
                    thread.start();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                currentFirstVisibleItem = firstVisibleItem;
                currentVisibleItemCount = visibleItemCount;
                totalItem = totalItemCount;
            }

        });*/
        return view;
    }



    /*private class MyHandler extends Handler {
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
    }*/

    /*private class ThreadMoreData extends Thread {
        @Override
        public  void run(){
            //Agrega la vista inferior
            manejador.sendEmptyMessage(0);
            //Se buscan mas datos
            obtenerMasDatos();
        }
    }*/

    private class SeccionListAdapter extends ArrayAdapter<Seccion> {
        public SeccionListAdapter() {
            super(getActivity(), R.layout.item_seccion_comercio, secciones);
        }
        public void actualizarDatos(){
            this.notifyDataSetChanged();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_seccion_comercio, parent, false);
            }
            Seccion actual = secciones.get(position);
            // Fill the view
            TextView nombreTV = (TextView)itemView.findViewById(R.id.item_sec_comercio_seccion);
            nombreTV.setText(actual.getNombre());
            TextView cantidadTV = (TextView) itemView.findViewById(R.id.item_sec_comercio_cantidad);
            cantidadTV.setText(actual.getCantProductos() + " productos");
            MaterialCardView panel = (MaterialCardView) itemView.findViewById(R.id.item_sec_comercio_panel);
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
                Seccion escogida = secciones.get((int)v.getTag());
                GlobalComercios.getInstance().setSeccion(escogida);
                GlobalComercios.getInstance().setPosSeccion((int)v.getTag());
                FragmentManager fm = getFragmentManager();
                FragMenuInferiorComercio actual = (FragMenuInferiorComercio) fm.findFragmentByTag("comercios_secciones");
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                FragSeccionModificar  seccion = new FragSeccionModificar ();
                fragmentTransaction.hide(actual);
                fragmentTransaction.add(R.id.comercio_contenedor, seccion, "comercios_modificar_seccion");
                fragmentTransaction.commit();
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialCardView

    public void OnclickDelMaterialButton(View view) {
        MaterialButton miMaterialButton = (MaterialButton)  view;
        miMaterialButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sec_listar_MaterialButtonFiltrar:
                        dialog.show();
                        break;
                    case R.id.sec_listar_MaterialButtonTodos:
                        FiltrosSeccion.getInstance().setUsarFiltros(false);
                        secciones.clear();
                        obtenerMasDatos();
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialButton

    private void DialogoFiltros(){
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.filtros_secciones);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        MaterialCardView limpiar = (MaterialCardView) dialog.findViewById(R.id.filtros_secciones_MaterialCardViewLimpiar);
        MaterialCardView buscar = (MaterialCardView) dialog.findViewById(R.id.filtros_secciones_MaterialCardViewBuscar);
        MaterialCardView cancelar = (MaterialCardView) dialog.findViewById(R.id.filtros_secciones_MaterialCardViewCancelar);
        final TextInputEditText nombre = (TextInputEditText) dialog.findViewById(R.id.filtros_secciones_nombre);
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltrosSeccion.getInstance().reiniciarFiltros();
                nombre.setText("");
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltrosSeccion.getInstance().setNombre(nombre.getText().toString());
                FiltrosSeccion.getInstance().setUsarFiltros(true);
                secciones.clear();
                obtenerMasDatos();
                dialog.cancel();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    };

    public void obtenerMasDatos() {
        vistaInferior.setVisibility(View.VISIBLE);
        //Consultar a la base
        //int idMinimo = (secciones.size() == 0 ? 0 : (secciones.get(secciones.size()-1)).getId());
        String query = "SELECT s.id, s.nombre, (SELECT COUNT(*) FROM SeccionesProductos se WHERE se.idSeccion = s.id) AS cantidad FROM Secciones s WHERE s.idComercio='"+ GlobalComercios.getInstance().getComercio().getId() +"' AND s.nombre <> 'DEFAULT'";// AND s.id>'" + idMinimo +"'";
        //String query = "SELECT s.id, s.nombre, (SELECT COUNT(*) FROM SeccionesProductos se WHERE se.idSeccion = s.id) AS cantidad FROM Secciones s WHERE s.idComercio='4' AND s.id>'" + idMinimo +"' AND s.nombre <> 'DEFAULT'";
        //Agregar fitros
        if(FiltrosSeccion.getInstance().isUsarFiltros()) {
            if (!FiltrosSeccion.getInstance().getNombre().equals("")) {
                query += " AND s.nombre LIKE '%" + FiltrosSeccion.getInstance().getNombre() + "%'";
            }
        }
        //Fin filtros
        //Limite despues de los filtros
        query += " ORDER BY s.id";
                //" LIMIT " + TAM_PAGINA;
        String url = Util.urlWebService + "/seccionesObtener.php?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if(mensajeError.equalsIgnoreCase("")){
                        if(jsonOb.has("secciones")) {
                            JSONArray users = jsonOb.getJSONArray("secciones");
                            for (int i = 0; i < users.length(); i++) {
                                JSONObject usuario = users.getJSONObject(i);
                                secciones.add(new Seccion(
                                        usuario.getInt("id"),
                                        usuario.getInt("cantidad"),
                                        usuario.getString("nombre")));
                            }
                        }
                        if(secciones.size() == 0){
                            mensajeToast("No se encontraron secciones");
                        }
                        if(listView.getAdapter() == null){
                            adapter = new SeccionListAdapter();
                            listView.setAdapter(adapter);
                        } else {
                            adapter.actualizarDatos();
                            //Message msg = manejador.obtainMessage(1);
                            //manejador.sendMessage(msg);
                        }
                    } else {
                        mensajeToast(mensajeError);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                vistaInferior.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                vistaInferior.setVisibility(View.GONE);
                mensajeToast("Error, inténtelo más tarde");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    public void actualizarSeccion(){
        if(GlobalComercios.getInstance().getPosSeccion() != -1){
            if(GlobalComercios.getInstance().getSeccion() == null){
                secciones.remove(GlobalComercios.getInstance().getPosSeccion());
                adapter.actualizarDatos();
            } else {
                secciones.set(GlobalComercios.getInstance().getPosSeccion(), GlobalComercios.getInstance().getSeccion());
                adapter.actualizarDatos();
            }
            GlobalComercios.getInstance().setPosSeccion(-1);
            GlobalComercios.getInstance().setSeccion(null);
        }

    }
    private void mensajeToast(String msg){ Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};
}
