package com.example.comercios.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Adapter.viewPagerAdapter;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.Producto;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragVerProductosGrid extends Fragment {

    /*private final int TAM_PAGINA = 4;
    private boolean cargando = false;
    private boolean userScrolled = false;*/
    private boolean vaciar = false;
    private View vistaInferior;
    private GridView gridView;
    private ProductoGridAdapter adapter;
    private List<Producto> productos;

    public FragVerProductosGrid() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_ver_productos_grid, container, false);
        GlobalUsuarios.getInstance().setVentanaActual(R.layout.frag_ver_productos_grid);
        vistaInferior = view.findViewById(R.id.frag_ver_productos_grid_cargando);
        productos = new ArrayList<Producto>();
        gridView = (GridView) view.findViewById(R.id.gridViewVerProductos);
        /*gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

           @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //int first = view.getFirstVisiblePosition();
                int visibles = view.getChildCount();
                int total = view.getCount();
                if (scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                } else {
                    userScrolled = false;
                }
                if (mLastFirstVisibleItem < first || ( visibles <= total && visibles <= TAM_PAGINA) || (view.getLastVisiblePosition() == productos.size()-1 && view.getLastVisiblePosition() >= TAM_PAGINA)) {
                    if(view.getLastVisiblePosition() + 1 == productos.size()){
                        if (userScrolled && view.getLastVisiblePosition() == productos.size() - 1 && cargando == false) {
                            cargando = true;
                            obtenerMasDatos();
                        }
                    }
                }
                //mLastFirstVisibleItem = first;
                if (( visibles <= total && visibles <= TAM_PAGINA)) {
                    if(view.getLastVisiblePosition() + 1 == productos.size()){
                        if (userScrolled && view.getLastVisiblePosition() == productos.size() - 1 && cargando == false) {
                            //vaciar = false;
                            cargando = true;
                            obtenerMasDatos();
                        }
                    }
                }
                //mLastFirstVisibleItem = first;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Revisa si el scroll llego al ultimo item
                if (mLastFirstVisibleItem < firstVisibleItem || visibleItemCount <= TAM_PAGINA) {
                    if (userScrolled && view.getLastVisiblePosition() == productos.size() - 1 && cargando == false) {
                        //vaciar = false;
                        cargando = true;
                        obtenerMasDatos();
                    }
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });*/
        obtenerMasDatos();

        return view;
    }

    private class ProductoGridAdapter extends ArrayAdapter<Producto> {
        public ProductoGridAdapter() {
            super(getActivity(), R.layout.item_ver_productos_grid, productos);
        }

        public void actualizarDatos() {
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)

            View itemView = convertView;

            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_ver_productos_grid, parent, false);
            }
            final Producto actual = productos.get(position);
            // Fill the view

            TextView productoTV = (TextView) itemView.findViewById(R.id.item_ver_prod_grid_txtProducto);
            productoTV.setText(actual.getNombre());
            TextView precioTV = (TextView) itemView.findViewById(R.id.item_ver_prod_grid_txtPrecio);
            if(actual.getPrecio() != -1){
                precioTV.setText("₡ " + actual.getPrecio());
            } else {
                precioTV.setVisibility(View.GONE);
            }

            final ViewPager viewPager = (ViewPager) itemView.findViewById(R.id.item_ver_prod_grid_viewPager);
            final viewPagerAdapter viewPAdaptador = new viewPagerAdapter(itemView.getContext(), actual.getImagenes());
            MaterialCardView materialCardView = (MaterialCardView) itemView.findViewById(R.id.item_ver_prod_grid_MaterialCardView);
            viewPAdaptador.setItem(itemView);
            viewPager.setAdapter(viewPAdaptador);
            materialCardView.setTag(position);

            Timer timer = actual.getTimer();
            Handler handler = actual.getHandler();
            Runnable update = actual.getUpdate();
            if(actual.getImagenes() != null && actual.getImagenes().size() > 1){
                if(timer != null){
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
            //actual.setTimer(viewPager);
            OnclickDelMaterialCardView(materialCardView);
            return itemView;
        }
    }

    public void OnclickDelMaterialCardView(MaterialCardView view) {
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                Producto escogido = productos.get(position);
                String message = "Elegiste item No.  " + (position)
                        + ", con nombre  " + escogido.getNombre();
                mensajeToast(message);
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialCardView

    private void obtenerMasDatos() {
        vistaInferior.setVisibility(View.VISIBLE);
        //Consultar a la base
        //int idMinimo = (productos.size() == 0 ? 0 : (productos.get(productos.size() - 1)).getId());
        String query = "SELECT p.id, p.estado, p.precio, p.nombre, p.descripcion FROM Productos p INNER JOIN SeccionesProductos sp ON p.id = sp.idProducto WHERE p.estado = '1' AND p.idComercio='"+ GlobalUsuarios.getInstance().getComercio().getId() +"' AND sp.idSeccion='"+GlobalUsuarios.getInstance().getIdSeccion()+"'";// AND p.id > '" + idMinimo + "'";
        query += " ORDER BY p.nombre";
        //query += " LIMIT " + TAM_PAGINA;
        String url = Util.urlWebService + "/obtenerProductos.php?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(vaciar)
                        productos.clear();
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if (mensajeError.equalsIgnoreCase("")) {
                        if (jsonOb.has("productos")) {
                            JSONArray productosJson = jsonOb.getJSONArray("productos");
                            for (int i = 0; i < productosJson.length(); i++) {
                                JSONObject producto = productosJson.getJSONObject(i);
                                ArrayList<Bitmap> imagenes = new ArrayList();
                                if (producto.has("imagenes")) {
                                    JSONArray imagenesSTR = producto.getJSONArray("imagenes");
                                    for (int j = 0; j < imagenesSTR.length(); j++) {
                                        imagenes.add(convertirStringToImg(imagenesSTR.getString(j)));
                                    }
                                }
                                productos.add(new Producto(
                                        producto.getInt("id"),
                                        producto.getInt("estado") == 1,
                                        producto.isNull("precio") ? -1 : producto.getInt("precio"),
                                        producto.getString("nombre"),
                                        producto.isNull("descripcion") ? null : producto.getString("descripcion"),
                                        imagenes
                                ));
                            }
                            if(gridView.getAdapter() == null){
                                adapter = new ProductoGridAdapter();
                                gridView.setAdapter(adapter);
                            } else {
                                adapter.actualizarDatos();
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
                //cargando = false;
                vistaInferior.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //cargando = false;
                mensajeToast("Error, inténtelo más tarde");
                vistaInferior.setVisibility(View.GONE);
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void cargarWebServicesImagen(String[] rutas, final int posicion, final int idProducto) {
        for(String ruta: rutas) {
            ImageRequest imagR = new ImageRequest(ruta, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    if (posicion < productos.size()) {
                        productos.get(posicion).agregarImagen(response);
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
    }

    private Bitmap convertirStringToImg(String imgCodificada) {
        byte[] stringDecodificado = Base64.decode(imgCodificada, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(stringDecodificado, 0, stringDecodificado.length);
        return decodedByte;
    }

    public void mensajeToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    };
    public void actualizaDatos(){
        productos.clear();
        adapter.actualizarDatos();
        gridView.setAdapter(null);
        vaciar = true;
        obtenerMasDatos();
    }
}
