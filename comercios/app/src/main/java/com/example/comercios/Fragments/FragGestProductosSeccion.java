package com.example.comercios.Fragments;

        import android.app.Fragment;
        import android.content.Context;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;

        import com.example.comercios.Modelo.Comercio;
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

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;


   public class FragGestProductosSeccion extends Fragment {

    private final int TAM_PAGINA = 10;
    private boolean inicial = true;
    private View view;

    private boolean cargando = false;
    private boolean userScrolled = false;
    private View vistaInferior;
    private ListView listView;
    private Handler manejador;
    private List<Comercio> comercios;

    private FragGestProductosSeccion.ProductosListAdapter adapter;
    private FragGestProductosSeccion.ProductosListAdapter adapter2;

    private List<Producto> productosArray;
    private List<Producto> productosDefArray;
    private int posicion = -1;
    private RadioGroup radioGroup;



    public FragGestProductosSeccion() {
        // Required empty public constructor
    }


       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
            mensajeAB("Gestionar productos");
            view =inflater.inflate(R.layout.frag_gest_productos_seccion, container, false);
            //LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = (ListView) view.findViewById(R.id.listViewProductosSeccion);
            manejador = new MyHandler();
            radioGroup = (RadioGroup) view.findViewById(R.id.FGestProductoSec_radioGroup);
            //adapter = new FragGestProductosSeccion.ProductosListAdapter();

            productosArray= new ArrayList<>();
            cargarProductosSeccion();

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                   RadioButton rb1 = (RadioButton) view.findViewById(R.id.FGestProductoSec_AddProd);
                   RadioButton rb2 = (RadioButton) view.findViewById(R.id.FGestProductoSec_DelProd);
                   if (rb1.isChecked()) {
                       //cargarProductosSeccion();
                       productosArray.clear();
                      // adapter.actualizarDatos();
                       cargarProductosSeccion();
                   }
                   if (rb2.isChecked()) {
                       //productos de la seccion que se pueden eliminar
                       productosArray.clear();
                       //adapter.actualizarDatos();
                       cargarProductosSeccion();
                       //group.clearCheck();
                   }
               }
           });


            return view;
        }
        public void cargarProductosSeccion(){

           String sql;
            //general
            if(radioGroup.getCheckedRadioButtonId()==R.id.FGestProductoSec_AddProd){
                sql="Select p.id, p.nombre,p.descripcion, p.precio, p.estado " +
                        "from Productos p where p.idComercio =" +GlobalComercios.getInstance().getComercio().getId(); ;
            }else{
                sql="SELECT p.id, p.nombre, p.descripcion, p.precio, p.estado " +
                        "FROM Productos p INNER JOIN SeccionesProductos sp ON p.id=sp.idProducto " +
                        "WHERE sp.idSeccion= '"+GlobalComercios.getInstance().getSeccion().getId() + "'";

            }
            String url = Util.urlWebService + "/obtenerProductosSeccion.php?query="+sql+"&idSeccion="+GlobalComercios.getInstance().getSeccion().getId();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONObject jsonOb = response.getJSONObject("datos");
                        String mensajeError = jsonOb.getString("mensajeError");
                        if(mensajeError.equalsIgnoreCase("")){
                            if(jsonOb.has("productos")) {
                                JSONArray productos = jsonOb.getJSONArray("productos");
                                if (productos.length() != 0) {
                                    for (int i = 0; i < productos.length(); i++) {
                                        JSONObject producto = productos.getJSONObject(i);

                                        productosArray.add(new Producto(
                                                producto.getInt("id"),
                                                producto.getInt("estado")!=0,
                                                producto.getInt("precio"),
                                                producto.getString("nombre"),
                                                producto.getString("descripcion"),
                                                producto.getInt("pertenece")==1));
                                    }
                                }

                            }
                            if(inicial){
                                listView.setAdapter(null);
                                adapter = new FragGestProductosSeccion.ProductosListAdapter();;
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
        private class ProductosListAdapter extends ArrayAdapter<Producto> {

            public ProductosListAdapter() {
                super(getActivity(), R.layout.item_gest_productos, productosArray);
            }
            public void actualizarDatos(){
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

                TextView nombre = (TextView)itemView.findViewById(R.id.item_gest_producto_nombre);
                nombre.setText(actual.getNombre());

                TextView precio = (TextView) itemView.findViewById(R.id.item_gest_producto_precio);
                precio.setText(String.valueOf(actual.getPrecio()));

                TextView estado = (TextView) itemView.findViewById(R.id.item_gest_producto_estado);
                estado.setText(actual.isEstado()? "Activo" : "Desactivo");

                MaterialButton buttonAction;
                if(actual.isPertenece()){
                    buttonAction = (MaterialButton) itemView.findViewById(R.id.item_gest_producto_MaterialButtonEliminar);

                }else{
                    buttonAction = (MaterialButton) itemView.findViewById(R.id.item_gest_producto_MaterialButtonEliminar);
                    buttonAction.setText("Agregar");
                    buttonAction.setIconResource(R.drawable.plus_square);
                    buttonAction.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                    buttonAction.setTag(position);
                }

                ImageView imagen = (ImageView) itemView.findViewById(R.id.item_gest_producto_ImgVProducto);


          /*  if(actual.getUrlImagen() != null && !actual.getUrlImagen().equals("")){
                cargarWebServicesImagen(imagen, actual.getUrlImagen(), actual.getCorreo());
            }*/


                //panel.setTag(position);
                estado.setTag(position);
                buttonAction.setTag(position);


                //OnclickDelMaterialCardView(panel);
            /*  OnclickDelMaterialButton(estado);
                OnclickDelMaterialButton(eliminar);
*/
                return itemView;
            }
        }
        public void Mensaje(String msg){ Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};

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
                cargarProductosSeccion();
            }
        }
       private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};


   }