package com.example.comercios.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Global.GlobalAdmin;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.Administrador;
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

import androidx.appcompat.app.AlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragGestAdminLista extends Fragment {
    private final int TAM_PAGINA = 10;
    private boolean inicial = true;
    private View vistaInferior;
    private ListView listView;
    private AdminListAdapter adapter;
    private Handler manejador;
    private List<Administrador> admins;
    private int posicion = -1;
    private int idEliminar;

    public FragGestAdminLista() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_gest_admin_lista, container, false);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vistaInferior = li.inflate(R.layout.vista_inferior_cargando, null);
        manejador = new FragGestAdminLista.MyHandler();
        admins = new ArrayList<Administrador>();
        listView = (ListView) view.findViewById(R.id.gest_admin_listview);
        obtenerMasDatos();
        OnclickDelMaterialButton(view.findViewById(R.id.gest_admin_MaterialButtonFiltrar));
        OnclickDelMaterialButton(view.findViewById(R.id.gest_admin_MaterialButtonTodos));

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentFirstVisibleItem;
            private int totalItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && scrollState == SCROLL_STATE_IDLE) {
                    Thread thread = new FragGestAdminLista.ThreadMoreData();
                    thread.start();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                currentFirstVisibleItem = firstVisibleItem;
                currentVisibleItemCount = visibleItemCount;
                totalItem = totalItemCount;
            }
        });
        return view;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Se agrega la vista de cargar mientras se busca mas datos
                    listView.addFooterView(vistaInferior);
                    break;
                case 1:
                    //Se actualizan los datos del adaptador y de la interfaaz
                    adapter.actualizarDatos();
                    listView.removeFooterView(vistaInferior);
                    break;
                default:
                    break;

            }
        }
    }

    private class ThreadMoreData extends Thread {
        @Override
        public void run() {
            //Agrega la vista inferior
            manejador.sendEmptyMessage(0);
            //Se buscan mas datos
            obtenerMasDatos();
        }
    }

    private class AdminListAdapter extends ArrayAdapter<Administrador> {
        public AdminListAdapter() {
            super(getActivity(), R.layout.item_gest_admin, admins);
        }

        public void actualizarDatos() {
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_gest_admin, parent, false);
            }
            Administrador actual = admins.get(position);
            // Fill the view
            TextView userTV = (TextView) itemView.findViewById(R.id.item_gest_admin_user);
            userTV.setText(actual.getUsuario());
            TextView correoTV = (TextView) itemView.findViewById(R.id.item_gest_admin_correo);
            correoTV.setText(actual.getCorreo());
            TextView edadTV = (TextView) itemView.findViewById(R.id.item_gest_admin_tel);
            edadTV.setText(Long.toString(actual.getTelefono()));
            TextView estadoTV = (TextView) itemView.findViewById(R.id.item_gest_admin_estado);
            estadoTV.setText(actual.isEstado() ? "Activado" : "Desactivado");
            //MaterialCardView panel = (MaterialCardView) itemView.findViewById(R.id.item_gest_estandar_panel);
            MaterialButton estado = (MaterialButton) itemView.findViewById(R.id.item_gest_admin_MaterialButtonEstado);
            estado.setText(actual.isEstado() ? "Desactivar" : "Activar");
            MaterialButton eliminar = (MaterialButton) itemView.findViewById(R.id.item_gest_admin_MaterialButtonEliminar);
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
        miMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Administrador escogido = admins.get((int) v.getTag());
                GlobalAdmin.getInstance().setAdmin(escogido);
                Mensaje(escogido.getCorreo());
                //Reemplazo de fragment
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialCardView

    public void OnclickDelMaterialButton(View view) {
        MaterialButton miMaterialButton = (MaterialButton) view;
        miMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.item_gest_admin_MaterialButtonEstado:
                        posicion = (int) v.getTag();
                        Administrador usuarioModificar = admins.get(posicion);
                        String contenido = "Usuario: " + usuarioModificar.getUsuario() + "\nCorreo: " + usuarioModificar.getCorreo();
                        DialogSiNO(usuarioModificar.isEstado() ? "¿Desactivar administrador?" : "¿Activar administrador?",
                                contenido, usuarioModificar.isEstado() ? "DESACTIVAR" : "ACTIVAR");
                        break;
                    case R.id.item_gest_admin_MaterialButtonEliminar:
                        posicion = (int) v.getTag();
                        usuarioModificar = admins.get(posicion);
                        idEliminar = usuarioModificar.getId();
                        contenido = "Usuario: " + usuarioModificar.getUsuario() + "\nCorreo: " + usuarioModificar.getCorreo();
                        DialogSiNO("¿Eliminar administrador?", contenido, "ELIMINAR");
                        break;
                    case R.id.gest_admin_MaterialButtonFiltrar:
                        //DailogoFiltros();
                        break;
                    case R.id.gest_admin_MaterialButtonTodos:

                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialButton

    private void DialogSiNO(String titulo, String contenido, final String accion) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle(titulo);
        builder1.setMessage(contenido);
        builder1.setCancelable(true);
        builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        posicion = -1;
                    }
                });
        builder1.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (accion) {
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
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    ;

    /*
    private void DailogoFiltros(){
        Dialog dialog = new Dialog(getActivity());
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filtros_u_estandar);
        dialog.setTitle("Filtrar");
        dialog.show();
    };*/

    private void obtenerMasDatos() {
        //Consultar a la base
        int idMinimo;
        if (admins.size() == 0) {
            idMinimo = 0;
        } else {
            idMinimo = (admins.get(admins.size() - 1)).getId();
        }
        String query = "SELECT u.id, u.tipo, u.correo, u.usuario, u.estado, a.telefono FROM Usuarios u, Administradores a WHERE u.tipo <> 0 and u.id = a.idUsuario AND u.id > '" + idMinimo + "'";
        //Agregar fitros
        //Limite despues de los filtros
        query += " ORDER BY u.id LIMIT " + TAM_PAGINA;
        String url = Util.urlWebService + "/adminObtener.php?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if (mensajeError.equalsIgnoreCase("")) {
                        if (jsonOb.has("usuarios")) {
                            JSONArray users = jsonOb.getJSONArray("usuarios");
                            if (users.length() != 0) {
                                for (int i = 0; i < users.length(); i++) {
                                    JSONObject usuario = users.getJSONObject(i);
                                    admins.add(new Administrador(
                                            usuario.getInt("id"),
                                            usuario.getInt("tipo"),
                                            usuario.getLong("telefono"),
                                            usuario.getInt("estado") != 0,
                                            usuario.getString("correo"),
                                            usuario.getString("usuario")));
                                }
                            }
                        }
                        if (inicial) {
                            adapter = new FragGestAdminLista.AdminListAdapter();
                            listView.setAdapter(adapter);
                            inicial = false;
                        } else {
                            Message msg = manejador.obtainMessage(1);
                            manejador.sendMessage(msg);
                        }
                    } else {
                        Mensaje(mensajeError);
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
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void actualizarEstadoUsuario() {
        if (posicion != -1) {
            String url = Util.urlWebService + "/usuarioActualizarEstado.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("")) {
                        Mensaje("Exito: Se actualizo correctamente");
                        Administrador u = admins.get(posicion);
                        u.setEstado(!u.isEstado());
                        admins.set(posicion, u);
                        adapter.actualizarDatos();
                        posicion = -1;
                        //Enviar correo al usuario
                    } else {
                        Mensaje(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Mensaje("No se puede conectar " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("estado", "" + (admins.get(posicion).isEstado() == true ? 0 : 1));
                    parametros.put("id", Integer.toString(admins.get(posicion).getId()));
                    return parametros;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
        }
    }

    private void eliminarUsuario() {
        if (posicion != -1) {
            String url = Util.urlWebService + "/adminEliminar.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("")) {
                        Mensaje("Exito: Se elimino correctamente");
                        admins.remove(posicion);
                        adapter.actualizarDatos();
                    } else {
                        Mensaje(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Mensaje("No se puede conectar " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("id", Integer.toString(idEliminar));
                    return parametros;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
        }
    }

    public void Mensaje(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
