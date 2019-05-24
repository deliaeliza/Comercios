package com.example.comercios.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Adapter.viewPagerAdapter;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.Comercio;
import com.example.comercios.Modelo.Producto;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
                precioTV.setVisibility(View.INVISIBLE);
            }
            MaterialCardView materialCardView = (MaterialCardView) itemView.findViewById(R.id.item_ver_prod_grid_MaterialCardView);
            Timer timer = actual.getTimer();
            Handler handler;
            Runnable update;
            final ViewPager viewPager = (ViewPager) itemView.findViewById(R.id.item_ver_prod_grid_viewPager);
            if(actual.getImagenes() != null ) {
                if(actual.getImagenes().size() > 0) {
                    final viewPagerAdapter viewPAdaptador = new viewPagerAdapter(itemView.getContext(), actual.getImagenes());
                    viewPAdaptador.setItem(itemView);
                    viewPAdaptador.setId(R.id.item_ver_prod_grid_MaterialCardView);
                    viewPager.setAdapter(viewPAdaptador);
                    if(actual.getImagenes().size() > 1){
                        //if(timer != null){
                        //    timer.cancel();
                        //    timer.purge();
                        //}
                        if(timer == null) {
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
                }
            }else {
                viewPager.setBackground(getResources().getDrawable(R.drawable.images));
                viewPager.setAdapter(null);
            }
            materialCardView.setTag(position);
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
                dialogoInformacion(productos.get(position));
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialCardView

    private void obtenerMasDatos() {
        vistaInferior.setVisibility(View.VISIBLE);
        String query = "SELECT p.id, p.estado, p.precio, p.nombre, p.descripcion FROM Productos p INNER JOIN SeccionesProductos sp ON p.id = sp.idProducto WHERE p.estado = '1' AND p.idComercio='"+ GlobalUsuarios.getInstance().getComercio().getId() +"' AND sp.idSeccion='"+GlobalUsuarios.getInstance().getIdSeccion()+"'";// AND p.id > '" + idMinimo + "'";
        query += " ORDER BY p.nombre";
        //query += " LIMIT " + TAM_PAGINA;
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
                                for(int i = 0; i < productos.size(); i ++){
                                    if(productos.get(i).getUrlsImagenes() != null && productos.get(i).getUrlsImagenes().length > 0)
                                        cargarWebServicesImagen(productos.get(i).getUrlsImagenes(), i, productos.get(i).getId());
                                }
                            }
                            if (productos.size() == 0) {
                                mensajeToast("No hay productos que mostrar");
                            }
                        } else {
                            mensajeToast(mensajeError);
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                    vistaInferior.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //cargando = false;
                if(getActivity() != null) {
                    mensajeToast("Error, inténtelo más tarde");
                    vistaInferior.setVisibility(View.GONE);
                }
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).getRequestQueue().cancelAll(getActivity());
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).getRequestQueue().cancelAll(getActivity().getApplicationContext());
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void cargarWebServicesImagen(String[] rutas, final int posicion, final int idProducto) {
        for(String ruta: rutas) {
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
                    if(getActivity() != null)
                        mensajeToast("Error al cargar la imagen");
                }
            });
            VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(imagR);
        }
    }
    private void dialogoInformacion(Producto producto) {
        if((producto.getImagenes() != null && producto.getImagenes().size() > 0) || producto.getDescripcion() != null){
            View view = getLayoutInflater().inflate(R.layout.dialogo_ver_producto, null);
            BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.CustomBottomSheetDialogTheme);
            //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setContentView(view);
            viewPagerAdapter adapterImagenes;
            ViewPager viewPager = (ViewPager) view.findViewById(R.id.dialogo_ver_producto_viewPager);
            TextView nombre = (TextView) view.findViewById(R.id.dialogo_ver_producto_nombre);
            TextView descripcion = (TextView) view.findViewById(R.id.dialogo_ver_producto_descripcion);
            TextView precio = (TextView) view.findViewById(R.id.dialogo_ver_producto_precio);

            nombre.setText(producto.getNombre());

            if(producto.getImagenes() != null && producto.getImagenes().size() > 0){
                adapterImagenes = new viewPagerAdapter(dialog.getContext(), producto.getImagenes());
                viewPager.setAdapter(adapterImagenes);
                viewPager.setClipToPadding(false);
                viewPager.setPadding(50, 0, 50, 0);
                viewPager.setPageMargin(20);
                //viewPager.setOffscreenPageLimit(3);

            } else {
                viewPager.setVisibility(View.GONE);
            }
            if(producto.getDescripcion() != null){
                descripcion.setText(producto.getDescripcion());
                descripcion.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK){
                            case MotionEvent.ACTION_UP:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                        return false;
                    }
                });
                descripcion.setMovementMethod(new ScrollingMovementMethod());
            } else {
                descripcion.setVisibility(View.GONE);
            }
            if(producto.getPrecio() != -1){
                precio.setText("₡ " + producto.getPrecio());
            } else {
                precio.setVisibility(View.GONE);
            }
            dialog.show();
        } else {
            mensajeToast("No hay información que mostrar");
        }
    }

    /*private Bitmap convertirStringToImg(String imgCodificada) {
        byte[] stringDecodificado = Base64.decode(imgCodificada, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(stringDecodificado, 0, stringDecodificado.length);
        return decodedByte;
    }*/

    public void mensajeToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    };
}
