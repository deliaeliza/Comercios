package com.example.comercios.Fragments;


import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Producto;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FragGestProductosSeccion extends Fragment {

    //private boolean inicial = true;
    private View view;

    //private boolean cargando = false;
    //private boolean userScrolled = false;
    private View vistaInferior;
    private ListView listView;
    //private Handler manejador;

    private ProductosListAdapter adapter;
    private List<Producto> productosArray;
    //private int posicion = -1;
    private TabLayout tabLayout;
    ImageView imagen;

    public FragGestProductosSeccion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mensajeAB("Gestionar productos");
        view = inflater.inflate(R.layout.frag_gest_productos_seccion, container, false);
        GlobalComercios.getInstance().setVentanaActual(R.layout.frag_gest_productos_seccion);
        listView = (ListView) view.findViewById(R.id.listViewProductosSeccion);
        vistaInferior = view.findViewById(R.id.FGestProductoSec_cargando);
        //manejador = new MyHandler();
        productosArray = new ArrayList<>();
        tabLayout = (TabLayout) view.findViewById(R.id.FGestProductoSec_radioGroup);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    productosArray.clear();
                    listView.setAdapter(null);
                    cargarProductosSeccion();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        cargarProductosSeccion();

        return view;
    }

    public void cargarProductosSeccion() {
        vistaInferior.setVisibility(View.VISIBLE);
        String sql;
        TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        tab.select();
        if (tab.getText().toString().trim().equalsIgnoreCase("Comercio")) {
            sql = "Select p.id, p.nombre,p.descripcion, p.precio, p.estado " +
                    "from Productos p where p.idComercio=" + GlobalComercios.getInstance().getComercio().getId()+";";

        } else {
            sql = "SELECT p.id, p.nombre, p.descripcion, p.precio, p.estado " +
                    "FROM Productos p INNER JOIN SeccionesProductos sp ON p.id=sp.idProducto " +
                    "WHERE sp.idSeccion= '" + GlobalComercios.getInstance().getSeccion().getId() + "'";
        }

        String url = Util.urlWebService + "/obtenerProductosSeccion.php?query=" + sql + "&idSeccion=" + GlobalComercios.getInstance().getSeccion().getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if (mensajeError.equalsIgnoreCase("")) {
                        if (jsonOb.has("productos")) {
                            JSONArray productos = jsonOb.getJSONArray("productos");
                            if (productos.length() != 0) {
                                productosArray.clear();
                                for (int i = 0; i < productos.length(); i++) {
                                    JSONObject producto = productos.getJSONObject(i);

                                    productosArray.add(new Producto(
                                            producto.getInt("id"),
                                            producto.getInt("estado") != 0,
                                            producto.isNull("precio") ? -1 : producto.getInt("precio"),
                                            producto.getString("nombre"),
                                            producto.isNull("descripcion") ? null :producto.getString("descripcion"),
                                            producto.getInt("pertenece") == 1,
                                            producto.isNull("Imagen") ? null : Util.urlWebService + "/" +producto.getString("Imagen")
                                    ));
                                }

                            }

                        }
                        if(listView.getAdapter() == null){
                            adapter = new ProductosListAdapter();
                            listView.setAdapter(adapter);
                        } else {
                            adapter.actualizarDatos();
                            //Message msg = manejador.obtainMessage(1);
                            //manejador.sendMessage(msg);
                        }
                        for(int i = 0; i < productosArray.size(); i++){
                            Producto pr = productosArray.get(i);
                            if(pr.getUrlPrueba() != null && pr.getUrlPrueba() != ""){
                                cargarWebServicesImagen(pr.getUrlPrueba(), i);
                            }
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

    private class ProductosListAdapter extends ArrayAdapter<Producto> {

        public ProductosListAdapter() {
            super(getActivity(), R.layout.item_gest_productos, productosArray);
        }
        public void actualizarDatos() {
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_gest_productos, parent, false);
            }
            Producto actual = productosArray.get(position);
            // Fill the view

            TextView nombre = (TextView) itemView.findViewById(R.id.item_gest_producto_nombre);
            nombre.setText(actual.getNombre());


            imagen = (ImageView) itemView.findViewById(R.id.item_gest_producto_ImgVProducto);
            if (actual.getImagen() != null){
                imagen.setImageBitmap(actual.getImagen());
            }else {
                imagen.setImageResource(R.drawable.ic_menu_camera);
            }
            TextView precio = (TextView) itemView.findViewById(R.id.item_gest_producto_precio);
            if(actual.getPrecio() != -1){
                precio.setText("₡ " + actual.getPrecio());
            } else {
                precio.setVisibility(View.GONE);
            }
            TextView estado = (TextView) itemView.findViewById(R.id.item_gest_producto_estado);
            estado.setText(actual.isEstado() ? "Activo" : "Desactivo");
            MaterialButton buttonAction;
            if (actual.isPertenece()) {
                buttonAction = (MaterialButton) itemView.findViewById(R.id.item_gest_producto_MaterialButtonEliminar);
                buttonAction.setText("Quitar de sección");
                buttonAction.setIconResource(R.drawable.trash_alt);
                buttonAction.setBackgroundColor(getResources().getColor(R.color.error));
                buttonAction.setTag(position);

            } else {
                buttonAction = (MaterialButton) itemView.findViewById(R.id.item_gest_producto_MaterialButtonEliminar);
                buttonAction.setText("Agregar a sección");
                buttonAction.setIconResource(R.drawable.plus_square);
                buttonAction.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                buttonAction.setTag(position);
            }
            estado.setTag(position);
            buttonAction.setTag(position);
            OnclickDelMaterialButton(buttonAction);

            return itemView;
        }
    }

    public void OnclickDelMaterialButton(View view) {
        MaterialButton miMaterialButton = (MaterialButton) view;
        miMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_gest_producto_MaterialButtonEliminar:
                        int posicion = (int) v.getTag();
                        Producto p = productosArray.get(posicion);
                        actualizarProducto(p,posicion);
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialButton
    private void mensajeToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    /*private class MyHandler extends Handler {
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
                    cargando = false;
                    break;
                default:
                    break;
            }
        }
    }*/
    /*private class ThreadMoreData extends Thread {
        @Override
        public void run() {
            //Agrega la vista inferior
            manejador.sendEmptyMessage(0);
            //Se buscan mas datos
            cargarProductosSeccion();
        }
    }*/
    private void mensajeAB(String msg) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(msg);
    }
    private void cargarWebServicesImagen(String ruta_foto, final int posicionP) {
        ImageRequest imagR = new ImageRequest(ruta_foto, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if(posicionP < productosArray.size()) {
                    productosArray.get(posicionP).setImagen(response);
                    adapter.actualizarDatos();
                }
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Error al cargar la imagen");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(imagR);
    }

    public void actualizarProducto(final Producto p, final int posicionProducto) {
        //final ProgressDialog progreso = new ProgressDialog(getActivity());
        //progreso.setMessage("Actualizando...");
        //progreso.show();
        String url = Util.urlWebService + "/productoSeccionAdministrar.php?";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("correcto")) {
                    mensajeToast("Actualización éxitosa");
                    if(productosArray.get(posicionProducto).isPertenece()){
                        if(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString().trim().equalsIgnoreCase("Sección")){
                            productosArray.remove(posicionProducto);
                        } else {
                            productosArray.get(posicionProducto).setPertenece(false);
                        }
                        GlobalComercios.getInstance().getSeccion().setCantProductos(GlobalComercios.getInstance().getSeccion().getCantProductos()-1);
                    } else {
                        productosArray.get(posicionProducto).setPertenece(true);
                        GlobalComercios.getInstance().getSeccion().setCantProductos(GlobalComercios.getInstance().getSeccion().getCantProductos()+1);
                    }
                    adapter.actualizarDatos();
                } else {
                    mensajeToast("Error al actualizar");
                }
                //progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progreso.hide();
                mensajeToast("Error, inténtelo más tarde");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idSeccion", String.valueOf(GlobalComercios.getInstance().getSeccion().getId()));
                parametros.put("idProducto", String.valueOf(p.getId()));
                parametros.put("idComercio", String.valueOf(GlobalComercios.getInstance().getComercio().getId()));
                parametros.put("operacion", p.isPertenece() ? "2" : "1");
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
    }
}