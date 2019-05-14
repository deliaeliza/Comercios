package com.example.comercios.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Adapter.viewPagerAdapter;
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

    private final int TAM_PAGINA = 4;

    private boolean inicial = true;
    private boolean cargando = false;
    private boolean userScrolled = false;
    private View vistaInferior;
    private GridView gridView;
    private ProductoGridAdapter adapter;
    //private Handler manejador;
    private List<Producto> productos;
    private int minPosicion = -1;

    public FragVerProductosGrid() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_ver_productos_grid, container, false);
        //LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //vistaInferior = li.inflate(R.layout.vista_inferior_cargando, null);
        vistaInferior = view.findViewById(R.id.frag_ver_productos_grid_cargando);
        //manejador = new MyHandler();
        productos = new ArrayList<Producto>();
        gridView = (GridView) view.findViewById(R.id.gridViewVerProductos);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                /*if (mLastFirstVisibleItem < first || ( visibles <= total && visibles <= TAM_PAGINA) || (view.getLastVisiblePosition() == productos.size()-1 && view.getLastVisiblePosition() >= TAM_PAGINA)) {
                    if(view.getLastVisiblePosition() + 1 == productos.size()){
                        if (userScrolled && view.getLastVisiblePosition() == productos.size() - 1 && cargando == false) {
                            cargando = true;
                            //Thread thread = new ThreadMoreData();
                            //thread.start();
                            obtenerMasDatos();
                        }
                    }
                }*/
                //mLastFirstVisibleItem = first;
                if (/*mLastFirstVisibleItem < first || */( visibles <= total && visibles <= TAM_PAGINA)) {
                    if(view.getLastVisiblePosition() + 1 == productos.size()){
                        if (userScrolled && view.getLastVisiblePosition() == productos.size() - 1 && cargando == false) {
                            cargando = true;
                            //Thread thread = new ThreadMoreData();
                            //thread.start();
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
                        cargando = true;
                        //Thread thread = new ThreadMoreData();
                        //thread.start();
                        obtenerMasDatos();
                    }
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });
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
            Producto actual = productos.get(position);
            // Fill the view

            TextView productoTV = (TextView) itemView.findViewById(R.id.item_ver_prod_grid_txtProducto);
            productoTV.setText(actual.getNombre());
            TextView precioTV = (TextView) itemView.findViewById(R.id.item_ver_prod_grid_txtPrecio);
            precioTV.setText("₡ " + actual.getPrecio());
            final ViewPager viewPager = (ViewPager) itemView.findViewById(R.id.item_ver_prod_grid_viewPager);
            final int paginas = actual.getImagenes().size();
            final viewPagerAdapter viewPAdaptador = new viewPagerAdapter(itemView.getContext(), actual.getImagenes());
            viewPAdaptador.setItem(itemView);
            viewPager.setAdapter(viewPAdaptador);
            itemView.setTag(position);
            if(paginas > 1 && position > minPosicion) {
                Timer timer;
                final Handler handler = new Handler();
                final Runnable update = new Runnable() {
                    int pagActual = 0;
                    public void run() {
                        if (pagActual == paginas) {
                            pagActual = 0;
                        }
                        viewPager.setCurrentItem(pagActual++, false);
                    }
                };
                timer = new Timer(); //This will create a new Thread
                timer.schedule(new TimerTask() { //task to be scheduled
                    @Override
                    public void run() {
                        handler.post(update);
                    }
                }, 500, 3000);

            }
            if(position == productos.size() -1){
                minPosicion = position;
            }
            OnclickDelMaterialCardView((MaterialCardView)itemView);
            //OnclickDelViewPager(viewPager, (MaterialCardView)itemView);
            return itemView;
        }
    }
    /*public void OnclickDelViewPager(ViewPager view, final MaterialCardView card) {
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                card.performClick();
            }// fin del onclick
        });
    }// fin de OnclickDelViewPager*/
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


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Se agrega la vista de cargar mientras se busca mas datos
                    //gridView.addFooterView(vistaInferior);
                    vistaInferior.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    //Se actualizan los datos del adaptador y de la interfaaz
                    adapter.actualizarDatos();
                    //gridView.removeFooterView(vistaInferior);
                    vistaInferior.setVisibility(View.GONE);
                    cargando = false;
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
            //manejador.sendEmptyMessage(0);
            //Se buscan mas datos
            obtenerMasDatos();


        }
    }


    private void obtenerMasDatos() {
        vistaInferior.setVisibility(View.VISIBLE);
        //Consultar a la base
        int idMinimo = (productos.size() == 0 ? 0 : (productos.get(productos.size() - 1)).getId());
        String query = "SELECT * FROM Productos WHERE estado = '1' and idComercio='5' and id > '" + idMinimo + "'";
        //Filtros orden por nombre o precio
        query += " ORDER BY id";
        //Fin filtros
        //Limite despues de los filtros
        query += " LIMIT " + TAM_PAGINA;
        String url = Util.urlWebService + "/obtenerProductos.php?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if (mensajeError.equalsIgnoreCase("")) {
                        if (jsonOb.has("productos")) {
                            //minPosicion = productos.size() - 1;
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
                                        producto.getInt("precio"),
                                        producto.getString("nombre"),
                                        producto.getString("descripcion"),
                                        imagenes
                                ));
                            }
                            if (inicial) {
                                adapter = new ProductoGridAdapter();
                                gridView.setAdapter(adapter);
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
                                cargando = false;
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

    private Bitmap convertirStringToImg(String imgCodificada) {
        byte[] stringDecodificado = Base64.decode(imgCodificada, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(stringDecodificado, 0, stringDecodificado.length);
        return decodedByte;
    }

    public void mensajeToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    };
    private void mensajeAB(String msg) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(msg);
    };
}
