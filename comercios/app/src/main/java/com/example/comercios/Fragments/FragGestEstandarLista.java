package com.example.comercios.Fragments;


import android.app.DatePickerDialog;
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
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.UsuarioEstandar;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

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
    private View vistaInferior;
    private ListView listView;
    private EstandarListAdapter adapter;
    private Handler manejador;
    private List<UsuarioEstandar> usuarios;
    private int posicion = -1;
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
        OnclickDelMaterialButton(view.findViewById(R.id.gest_estandar_MaterialButtonFiltrar));
        OnclickDelMaterialButton(view.findViewById(R.id.gest_estandar_MaterialButtonTodos));
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int first = view.getFirstVisiblePosition();
                int count = view.getChildCount();

                /*if (scrollState == SCROLL_STATE_FLING || (view.getLastVisiblePosition() == usuarios.size()-1) ) {
                    userScrolled = true;
                } else {
                    userScrolled = false;
                }*/
                 //userScrolled = scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Revisa si el scroll llego al ultimo item
                if(/*userScrolled &&*/ view.getLastVisiblePosition() == usuarios.size()-1 && listView.getCount() >= TAM_PAGINA && cargando == false){
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
            //MaterialCardView panel = (MaterialCardView) itemView.findViewById(R.id.item_gest_estandar_panel);
            MaterialButton estado = (MaterialButton) itemView.findViewById(R.id.item_gest_estandar_MaterialButtonEstado);
            estado.setText(actual.isEstado()? "Desactivar" : "Activar");
            MaterialButton eliminar = (MaterialButton) itemView.findViewById(R.id.item_gest_estandar_MaterialButtonEliminar);
            //panel.setTag(position);
            estado.setTag(position);
            eliminar.setTag(position);
            //OnclickDelMaterialCardView(panel);
            OnclickDelMaterialButton(estado);
            OnclickDelMaterialButton(eliminar);
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
                mensajeToast(escogido.getCorreo());
                //Reemplazo de fragment
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialCardView

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
                        DailogoFiltros();
                        break;
                    case R.id.gest_estandar_MaterialButtonTodos:

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

    private void DailogoFiltros(){
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filtros_u_estandar);
        dialog.setTitle("Filtrar");
        dialog.show();
    };

    private void obtenerMasDatos() {
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
                        } else {
                            /*try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                            Message msg = manejador.obtainMessage(1);
                            manejador.sendMessage(msg);
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
                    parametros.put("id", Integer.toString(GlobalComercios.getInstance().getSeccion().getId()));
                    return parametros;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
        }
    }

    public void mensajeToast(String msg){ Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
