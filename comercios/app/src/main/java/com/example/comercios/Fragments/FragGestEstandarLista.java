package com.example.comercios.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Filtros.FiltrosUsuarioEstandar;
import com.example.comercios.Global.GlobalAdmin;
import com.example.comercios.Modelo.UsuarioEstandar;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragGestEstandarLista extends Fragment {
    private final int TAM_PAGINA = 10;

    private boolean inicial = true;
    private boolean cargando = false;
    private boolean userScrolled = false;
    private int posicion = -1;
    private View vistaInferior;
    private ListView listView;
    private EstandarListAdapter adapter;
    private Handler manejador;
    private List<UsuarioEstandar> usuarios;
    private Dialog dialog;
    public FragGestEstandarLista() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mensajeAB("Gestionar usuarios");
        GlobalAdmin.getInstance().setVentanaActual(R.layout.frag_gest_estandar_lista);
        View view =inflater.inflate(R.layout.frag_gest_estandar_lista, container, false);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vistaInferior = view.findViewById(R.id.fragGestEstandar_carganfo);
        manejador = new MyHandler();
        usuarios = new ArrayList<UsuarioEstandar>();
        listView = (ListView) view.findViewById(R.id.gest_estandar_listview);
        DialogoFiltros();
        obtenerMasDatos();
        OnclickDelMaterialButton(view.findViewById(R.id.gest_estandar_MaterialButtonFiltrar));
        OnclickDelMaterialButton(view.findViewById(R.id.gest_estandar_MaterialButtonTodos));
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int first = view.getFirstVisiblePosition();
                int count = view.getChildCount();

               // if (scrollState == SCROLL_STATE_FLING || (view.getLastVisiblePosition() == usuarios.size()-1) ) {
                if (scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                } else {
                    userScrolled = false;
                }
                 //userScrolled = scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(userScrolled && view.getLastVisiblePosition() == usuarios.size()-1 && cargando == false){
                    cargando = true;
                    //Thread thread = new ThreadMoreData();
                    //thread.start();
                    obtenerMasDatos();
                }
            }
        });
/*
        listView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view == listView && motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    userScrolled = true;
                    mensajeAB(listView.getLastVisiblePosition() +"");
                    if(userScrolled && listView.getLastVisiblePosition() == usuarios.size()-1 && cargando == false){
                        mensajeAB(listView.getLastVisiblePosition() +"");
                        cargando = true;
                        Thread thread = new ThreadMoreData();
                        thread.start();
                    }
                } else {
                    userScrolled = false;
                }
                return false;
            }
        });*/
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
        //obtenerMasDatos();
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
            //Agrega la vista inferior
            manejador.sendEmptyMessage(0);
            //Se buscan mas datos
            obtenerMasDatos();


        }
    }

    private class EstandarListAdapter extends ArrayAdapter<UsuarioEstandar> {
        public EstandarListAdapter() {
            super(getActivity(), R.layout.item_gest_estandar, usuarios);
        }
        public void actualizarDatos(){
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
            TextView estadoTV = (TextView) itemView.findViewById(R.id.item_gest_estandar_estado);
            estadoTV.setText(actual.isEstado()? "Activado" : "Desactivado");
            MaterialButton estado = (MaterialButton) itemView.findViewById(R.id.item_gest_estandar_MaterialButtonEstado);
            estado.setText(actual.isEstado()? "Desactivar" : "Activar");
            MaterialButton eliminar = (MaterialButton) itemView.findViewById(R.id.item_gest_estandar_MaterialButtonEliminar);
            estado.setTag(position);
            eliminar.setTag(position);
            OnclickDelMaterialButton(estado);
            OnclickDelMaterialButton(eliminar);
            return itemView;
        }
    }

    public void OnclickDelMaterialButton(View view) {
        MaterialButton miMaterialButton = (MaterialButton)  view;
        miMaterialButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.item_gest_estandar_MaterialButtonEstado:
                        posicion = (int)v.getTag();
                        UsuarioEstandar usuarioModificar = usuarios.get(posicion);
                        String contenido = "Usuario: " + usuarioModificar.getUsuario() + "\nCorreo: " + usuarioModificar.getCorreo();
                        DialogSiNO(usuarioModificar.isEstado()? "¿Desactivar usuario?" : "¿Activar usuario?",
                                contenido, usuarioModificar.isEstado() ? "DESACTIVAR" : "ACTIVAR");
                        break;
                    case R.id.item_gest_estandar_MaterialButtonEliminar:
                        posicion = (int)v.getTag();
                        usuarioModificar = usuarios.get(posicion);
                        contenido = "Usuario: " + usuarioModificar.getUsuario() + "\nCorreo: " + usuarioModificar.getCorreo();
                        DialogSiNO("¿Eliminar usuario?", contenido, "ELIMINAR");
                        break;
                    case R.id.gest_estandar_MaterialButtonFiltrar:
                        dialog.show();
                        break;
                    case R.id.gest_estandar_MaterialButtonTodos:
                        FiltrosUsuarioEstandar.getInstance().setUsarFiltros(false);
                        usuarios.clear();
                        obtenerMasDatos();
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialButton

    private void DialogSiNO(String titulo, String contenido, final String accion){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle(titulo);
        builder1.setMessage(contenido);
        builder1.setCancelable(true);
        builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        posicion = -1;
                    } });
        builder1.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (accion){
                            case "ACTIVAR": //"ACTIVAR" || "DESACTIVAR"
                            case "DESACTIVAR":
                                actualizarEstadoUsuario();
                                break;
                            case "ELIMINAR":
                                eliminarUsuario();
                                break;
                            default:
                                break;
                        }
                    } });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };

    private void DialogoFiltros(){
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.filtros_u_estandar);
        MaterialCardView limpiar = (MaterialCardView) dialog.findViewById(R.id.filtros_estandar_MaterialCardViewLimpiar);
        MaterialCardView buscar = (MaterialCardView) dialog.findViewById(R.id.filtros_estandar_MaterialCardViewBuscar);
        MaterialCardView cancelar = (MaterialCardView) dialog.findViewById(R.id.filtros_estandar_MaterialCardViewCancelar);
        final TextInputEditText usuario = (TextInputEditText) dialog.findViewById(R.id.filtros_comercio_usuario);
        final TextInputEditText correo = (TextInputEditText) dialog.findViewById(R.id.filtros_estandar_correo);
        final TextInputEditText edadMin = (TextInputEditText) dialog.findViewById(R.id.filtros_estandar_edadMin);
        final TextInputEditText edadMax = (TextInputEditText) dialog.findViewById(R.id.filtros_estandar_edadMax);
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.filtros_estandar_radioG);
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltrosUsuarioEstandar.getInstance().reiniciarFiltros();
                usuario.setText("");
                correo.setText("");
                edadMin.setText("");
                edadMax.setText("");
                rg.clearCheck();
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltrosUsuarioEstandar.getInstance().setUsuario(usuario.getText().toString());
                FiltrosUsuarioEstandar.getInstance().setCorreo(correo.getText().toString());
                switch (rg.getCheckedRadioButtonId()){
                    case R.id.filtros_estandar_radioActivo:
                        FiltrosUsuarioEstandar.getInstance().setRadioEstado(1);
                        break;
                    case R.id.filtros_estandar_radioDesactivo:
                        FiltrosUsuarioEstandar.getInstance().setRadioEstado(0);
                        break;
                    default:
                        FiltrosUsuarioEstandar.getInstance().setRadioEstado(-1);
                        break;
                }
                try{
                    FiltrosUsuarioEstandar.getInstance().setEdadMin(Integer.parseInt(edadMin.getText().toString()));
                } catch (NumberFormatException ex){
                    FiltrosUsuarioEstandar.getInstance().setEdadMin(-1);
                }
                try{
                    FiltrosUsuarioEstandar.getInstance().setEdadMax(Integer.parseInt(edadMax.getText().toString()));
                } catch (NumberFormatException ex){
                    FiltrosUsuarioEstandar.getInstance().setEdadMax(-1);
                }
                FiltrosUsuarioEstandar.getInstance().setUsarFiltros(true);
                usuarios.clear();
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

    private void obtenerMasDatos() {
        vistaInferior.setVisibility(View.VISIBLE);
        //Consultar a la base
        int idMinimo = (usuarios.size() == 0 ? 0 : (usuarios.get(usuarios.size()-1)).getId());
        String query = "SELECT u.id, u.tipo, u.correo, u.usuario, u.estado, ue.fechaNac, TIMESTAMPDIFF(YEAR, ue.fechaNac, CURDATE()) as edad FROM Usuarios u, UsuariosEstandar ue WHERE u.id = ue.idUsuario AND u.id > '" + idMinimo + "'";
        //Agregar fitros
        if(FiltrosUsuarioEstandar.getInstance().isUsarFiltros()) {
            if (!FiltrosUsuarioEstandar.getInstance().getUsuario().equals("")) {
                query += " AND u.usuario LIKE '%" + FiltrosUsuarioEstandar.getInstance().getUsuario() + "%'";
            }
            if (!FiltrosUsuarioEstandar.getInstance().getCorreo().equals("")) {
                query += " AND u.correo LIKE '%" + FiltrosUsuarioEstandar.getInstance().getCorreo() + "%'";
            }
            if (FiltrosUsuarioEstandar.getInstance().getEdadMin() != -1) {
                query += " AND TIMESTAMPDIFF(YEAR, ue.fechaNac, CURDATE()) >= '" + FiltrosUsuarioEstandar.getInstance().getEdadMin() + "'";
            }
            if (FiltrosUsuarioEstandar.getInstance().getEdadMax() != -1) {
                query += " AND TIMESTAMPDIFF(YEAR, ue.fechaNac, CURDATE()) <= '" + FiltrosUsuarioEstandar.getInstance().getEdadMax() + "'";
            }
            if(FiltrosUsuarioEstandar.getInstance().getRadioEstado() != -1){
                query += " AND u.estado = '" + FiltrosUsuarioEstandar.getInstance().getRadioEstado() + "'";
            }
        }
        //Fin filtros
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
                        if(usuarios.size() == 0){
                            mensajeToast("No se encontraron usuarios");
                        }
                        if(inicial){
                            adapter = new EstandarListAdapter();
                            listView.setAdapter(adapter);
                            inicial = false;
                        } else {
                            /*try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                            //Message msg = manejador.obtainMessage(1);
                            //manejador.sendMessage(msg);

                            adapter.actualizarDatos();
                            //listView.removeFooterView(vistaInferior);

                            cargando = false;
                        }
                    } else {
                        mensajeToast(mensajeError);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                vistaInferior.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("No se puede conectar " + error.toString());
                vistaInferior.setVisibility(View.GONE);
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void actualizarEstadoUsuario(){
        if(posicion != -1){
            String url = Util.urlWebService + "/usuarioActualizarEstado.php?";
            StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("")) {
                        mensajeToast("Exito: Se actualizo correctamente");
                        UsuarioEstandar u = usuarios.get(posicion);
                        u.setEstado(!u.isEstado());
                        usuarios.set(posicion, u);
                        adapter.actualizarDatos();
                        posicion = -1;
                        //Enviar correo al usuario
                    } else {
                        mensajeToast(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mensajeToast("No se puede conectar " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("estado", "" + (usuarios.get(posicion).isEstado() == true ? 0 : 1));
                    parametros.put("id",Integer.toString(usuarios.get(posicion).getId()));
                    return parametros;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
        }
    }

    private void eliminarUsuario(){
        if(posicion != -1){
            String url = Util.urlWebService + "/usuarioEstandarEliminar.php?";
            StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("")) {
                        mensajeToast("Exito: Se elimino correctamente");
                        usuarios.remove(posicion);
                        posicion = -1;
                        adapter.actualizarDatos();
                        //Enviar correo al usuario
                    } else {
                        mensajeToast(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mensajeToast("No se puede conectar " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("id", Integer.toString(usuarios.get(posicion).getId()));
                    return parametros;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
        }
    }

    public void mensajeToast(String msg){ Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};

}
