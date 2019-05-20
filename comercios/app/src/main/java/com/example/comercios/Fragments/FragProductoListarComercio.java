package com.example.comercios.Fragments;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Adapter.viewPagerAdapter;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Producto;
import com.example.comercios.Modelo.Seccion;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragProductoListarComercio extends Fragment {


    private GridView gridView;
    private ProgressBar progressBar;
    private ProductoGridAdapter adapter;
    private TabLayout tabLayout;
    private List<Producto> productos;
    private List<Seccion> secciones;
    private int idSeccionActual;

    public FragProductoListarComercio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_producto_listar_comercio, container, false);
        mensajeAB(nombreDefaultPorCategoria(GlobalComercios.getInstance().getComercio().getCategoria()));
        //GlobalUsuarios.getInstance().setVentanaActual(R.layout.frag_producto_listar_comercio);
        progressBar = (ProgressBar) view.findViewById(R.id.fragProdListCom_cargando);
        productos = new ArrayList<>();
        secciones = new ArrayList<>();
        idSeccionActual = -1;
        tabLayout = (TabLayout) view.findViewById(R.id.fragProdListCom_tab);
        gridView = (GridView) view.findViewById(R.id.fragProdListCom_grid);
        cargarSecciones();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productos.clear();
                gridView.setAdapter(null);
                idSeccionActual = (int) tab.getTag();
                obtenerMasDatos();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private class ProductoGridAdapter extends ArrayAdapter<Producto> {
        public ProductoGridAdapter() {
            super(getActivity(), R.layout.item_ver_productos_grid_comercio, productos);
        }

        public void actualizarDatos() {
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)

            View itemView = convertView;

            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_ver_productos_grid_comercio, parent, false);
            }
            final Producto actual = productos.get(position);
            // Fill the view

            TextView productoTV = (TextView) itemView.findViewById(R.id.item_ver_prod_grid_comercio_txtProducto);
            productoTV.setText(actual.getNombre());
            TextView precioTV = (TextView) itemView.findViewById(R.id.item_ver_prod_grid_comercio_txtPrecio);
            if (actual.getPrecio() != -1) {
                precioTV.setText("₡ " + actual.getPrecio());
            } else {
                precioTV.setVisibility(View.GONE);
            }

            TextView estado = (TextView) itemView.findViewById(R.id.item_ver_prod_grid_comercio_txtInfoEstado);
            estado.setText(actual.isEstado() ? "Activo" : "Desactivo");
            MaterialButton btnEliminar = (MaterialButton) itemView.findViewById(R.id.item_ver_prod_grid_comercio_btnEliminar);
            MaterialButton btnCambiarEstado = (MaterialButton) itemView.findViewById(R.id.item_ver_prod_grid_comercio_btnActEstado);
            btnEliminar.setTag(position);
            btnCambiarEstado.setTag(position);
            btnCambiarEstado.setText(actual.isEstado() ? "Desactivar" : "Activar");
            MaterialCardView materialCardView = (MaterialCardView) itemView.findViewById(R.id.item_ver_prod_grid_comercio_MaterialCardView);
            Timer timer = actual.getTimer();
            Handler handler;
            Runnable update;
            final ViewPager viewPager = (ViewPager) itemView.findViewById(R.id.item_ver_prod_grid_comercio_viewPager);
            if (actual.getImagenes() != null) {
                if (actual.getImagenes().size() > 0) {

                    final viewPagerAdapter viewPAdaptador = new viewPagerAdapter(itemView.getContext(), actual.getImagenes());
                    viewPAdaptador.setItem(itemView);
                    viewPager.setAdapter(viewPAdaptador);
                    if (actual.getImagenes().size() > 1) {
                        if (timer != null) {
                            timer.cancel();
                            timer.purge();
                        }
                        final int paginas = actual.getImagenes().size();
                        handler = new Handler();
                        update = new Runnable() {
                            int pagActual = 0;

                            public void run() {
                                if (pagActual == paginas) {
                                    pagActual = 0;
                                }
                                viewPager.setCurrentItem(pagActual++, false);
                            }
                        };
                        timer = new Timer(); //This will create a new Thread
                        actual.setHandler(handler);
                        actual.setUpdate(update);

                        timer.schedule(new TimerTask() { //task to be scheduled
                            @Override
                            public void run() {
                                actual.getHandler().post(actual.getUpdate());
                            }
                        }, 500, 3000);
                        actual.setTimer(timer);
                    }
                }
            } else {
                viewPager.setBackground(getResources().getDrawable(R.drawable.images));
                viewPager.setAdapter(null);
            }
            materialCardView.setTag(position);
            OnclickDelMaterialButton(btnEliminar);
            OnclickDelMaterialButton(btnCambiarEstado);
            OnclickDelMaterialCardView(materialCardView);
            return itemView;
        }
    }

    public void OnclickDelMaterialCardView(MaterialCardView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Producto escogido = productos.get(position);
                GlobalComercios.getInstance().setProducto(escogido);
                GlobalComercios.getInstance().setPosActProd(position);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                FragMenuInferiorComercio actualFrag = (FragMenuInferiorComercio) fm.findFragmentByTag("comercios_productos");
                FragActInfoProductos mifrag = new FragActInfoProductos();
                fragmentTransaction.hide(actualFrag);
                fragmentTransaction.add(R.id.comercio_contenedor, mifrag, "comercios_actualizar_producto");
                fragmentTransaction.commit();
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialCardView

    public void OnclickDelMaterialButton(View view) {
        MaterialButton miMaterialButton = (MaterialButton) view;
        miMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto escogido = productos.get((int) v.getTag());
                switch (v.getId()) {
                    case R.id.item_ver_prod_grid_comercio_btnActEstado:
                        DialogSiNO(escogido.isEstado() ? "¿Desactivar producto?" : "¿Activar comercio?",
                                "Nombre: " + escogido.getNombre(), escogido.isEstado() ? "DESACTIVAR" : "ACTIVAR", (int) v.getTag());
                        break;
                    case R.id.item_ver_prod_grid_comercio_btnEliminar:
                        DialogSiNO("¿Eliminar producto?", "Nombre: " + escogido.getNombre(), "ELIMINAR", (int) v.getTag());
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialButton

    private void DialogSiNO(String titulo, String contenido, final String accion, final int posicionProd) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle(titulo);
        builder1.setMessage(contenido);
        builder1.setCancelable(true);
        builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder1.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (accion) {
                            case "ACTIVAR": //"ACTIVAR" || "DESACTIVAR"
                            case "DESACTIVAR":
                                actualizarEstadoProducto(posicionProd);
                                break;
                            case "ELIMINAR":
                                eliminarProducto(posicionProd);
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

    private void eliminarProducto(final int posicionProd) {
        String url = Util.urlWebService + "/productoEliminar.php?";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("")) {
                    mensajeToast("Se elimino correctamente");
                    productos.remove(posicionProd);
                    adapter.actualizarDatos();
                } else {
                    mensajeToast(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Error, inténtelo más tarde");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idComercio", Integer.toString(GlobalComercios.getInstance().getComercio().getId()));
                parametros.put("id", Integer.toString(productos.get(posicionProd).getId()));
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
    }

    private void actualizarEstadoProducto(final int posicionProd) {
        String url = Util.urlWebService + "/productoActualizarEstado.php?";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("")) {
                    mensajeToast("Actualización éxitosa");
                    Producto u = productos.get(posicionProd);
                    u.setEstado(!u.isEstado());
                    productos.set(posicionProd, u);
                    adapter.actualizarDatos();
                } else {
                    mensajeToast(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Error, inténtelo más tarde");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("estado", "" + (productos.get(posicionProd).isEstado() ? 0 : 1));
                parametros.put("id", Integer.toString(productos.get(posicionProd).getId()));
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);

    }

    private void obtenerMasDatos() {
        //Consultar a la base
        String query = "SELECT p.id, p.estado, p.precio, p.nombre, p.descripcion FROM Productos p INNER JOIN SeccionesProductos sp ON p.id = sp.idProducto WHERE p.idComercio='" + GlobalComercios.getInstance().getComercio().getId() + "'";
        if (idSeccionActual != -1) {
            query = query + " AND sp.idSeccion='" + idSeccionActual + "'";
        }
        query += " ORDER BY p.nombre";
        String url = Util.urlWebService + "/obtenerProductos.php?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (getActivity() != null) {
                    try {
                        JSONObject jsonOb = response.getJSONObject("datos");
                        String mensajeError = jsonOb.getString("mensajeError");
                        if (mensajeError.equalsIgnoreCase("")) {
                            if (jsonOb.has("productos")) {
                                JSONArray productosJson = jsonOb.getJSONArray("productos");
                                for (int i = 0; i < productosJson.length(); i++) {
                                    JSONObject producto = productosJson.getJSONObject(i);
                                    String[] urlsImagenes = null;
                                    if (producto.has("urls")) {
                                        JSONArray urls = producto.getJSONArray("urls");
                                        urlsImagenes = new String[urls.length()];
                                        for (int j = 0; j < urls.length(); j++) {
                                            urlsImagenes[j] = (Util.urlWebService + "/" + urls.getString(j));
                                        }
                                    }
                                    productos.add(new Producto(
                                            producto.getInt("id"),
                                            producto.getInt("estado") == 1,
                                            producto.isNull("precio") ? -1 : producto.getInt("precio"),
                                            producto.getString("nombre"),
                                            producto.isNull("descripcion") ? null : producto.getString("descripcion"),
                                            urlsImagenes
                                    ));
                                }
                                if (getActivity() != null && gridView.getAdapter() == null) {
                                    adapter = new ProductoGridAdapter();
                                    gridView.setAdapter(adapter);
                                } else {
                                    adapter.actualizarDatos();
                                }
                                for (int i = 0; i < productos.size(); i++) {
                                    if (productos.get(i).getUrlsImagenes() != null && productos.get(i).getUrlsImagenes().length > 0)
                                        cargarWebServicesImagen(productos.get(i).getUrlsImagenes(), i, productos.get(i).getId());
                                }
                            }
                            if (productos.size() == 0) {
                                mensajeToast("No hay productos que mostrar");
                            }
                        } else {
                            mensajeToast(mensajeError);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getActivity() != null) {
                    mensajeToast("Error, inténtelo más tarde");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).getRequestQueue().cancelAll(getActivity());
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).getRequestQueue().cancelAll(getActivity().getApplicationContext());
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void cargarWebServicesImagen(String[] rutas, final int posicion, final int idProducto) {
        for (String ruta : rutas) {
            ImageRequest imagR = new ImageRequest(ruta, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    if (getActivity() != null && posicion < productos.size() && productos.get(posicion).getId() == idProducto) {
                        productos.get(posicion).agregarImagen(response);
                        adapter.actualizarDatos();
                    }
                }
            }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (getActivity() != null)
                        mensajeToast("Error al cargar la imagen");
                }
            });
            VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(imagR);
        }
    }

    public void mensajeToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void cargarSecciones() {
        progressBar.setVisibility(View.VISIBLE);
        String query = "SELECT s.*, COUNT(sp.idSeccion) cantidad FROM Secciones s INNER JOIN SeccionesProductos sp ON s.id = sp.idSeccion WHERE s.idComercio='" + GlobalComercios.getInstance().getComercio().getId() + "' GROUP BY s.id;";
        String url = Util.urlWebService + "/seccionesObtener.php?query=" + query;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Seccion porDefecto = null;
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if (mensajeError.equalsIgnoreCase("")) {
                        if (jsonOb.has("secciones")) {
                            JSONArray users = jsonOb.getJSONArray("secciones");
                            for (int i = 0; i < users.length(); i++) {
                                JSONObject usuario = users.getJSONObject(i);
                                if (!usuario.getString("nombre").equalsIgnoreCase("DEFAULT")) {
                                    secciones.add(new Seccion(usuario.getInt("id"), usuario.getString("nombre")));
                                } else {
                                    porDefecto = new Seccion(usuario.getInt("id"), usuario.getString("nombre"));
                                }
                            }
                            if (porDefecto != null)
                                secciones.add(porDefecto);
                        }
                    } else {
                        mensajeToast(mensajeError);
                    }
                    cargarTabLayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Error, inténtelo más tarde");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void cargarTabLayout() {
        if (secciones != null) {
            TabLayout.Tab inicio = tabLayout.newTab();
            inicio.setText("Todos");
            inicio.setTag(-1);
            tabLayout.addTab(inicio);
            if (secciones.size() > 0) {
                for (Seccion s : secciones) {
                    TabLayout.Tab t = tabLayout.newTab();
                    if (s.getNombre().equalsIgnoreCase("DEFAULT")) {
                        if (secciones.size() > 1) {
                            t.setText("Más");
                        } else {
                            t.setText(nombreDefaultPorCategoria(GlobalComercios.getInstance().getComercio().getCategoria()));
                        }
                    } else {
                        t.setText(s.getNombre());
                    }
                    t.setTag(s.getId());
                    tabLayout.addTab(t);
                }
            }
        }
        //obtenerMasDatos();
    }

    private String nombreDefaultPorCategoria(String categoria) {
        switch (categoria) {
            case "Restaurante":
                return "Menú";
            case "Bar":
                return "Bebidas";
            case "Libreria":
                return "Libros";
            default:
                return "Productos";
        }
    }

    private void mensajeAB(String msg) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(msg);
    }

    ;

}
