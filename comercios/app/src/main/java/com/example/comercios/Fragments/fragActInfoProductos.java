package com.example.comercios.Fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Adapter.viewPagerAdapter;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Seccion;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragActInfoProductos extends Fragment {
    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private static final int CANTIMG_MAX = 5;

    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios

    private ArrayList<Seccion> secciones;
    private ArrayList<Integer> idSec;
    private CharSequence[] nombreSec;
    private boolean[] secEscogidas;

    private TextInputLayout tilCategoria, tilNombre, tilPrecio, tilDesc;
    private TextInputEditText categoria, nombre, precio, desc;
    private MaterialButton btnEliminar, btnCambiar, btnAgregar, btnModificar;
    private viewPagerAdapter viewPager;
    private boolean reemImg = false;
    public FragActInfoProductos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_act_info_productos, container, false);
        secciones = new ArrayList<>();
        idSec = new ArrayList<>();
        //recuperarSeccionesComercio(GlobalComercios.getInstance().getComercio().getId());
        recuperarSeccionesComercio(4);



        tilCategoria = (TextInputLayout) view.findViewById(R.id.act_prod_tilCategorias);
        tilNombre = (TextInputLayout) view.findViewById(R.id.act_prod_tilNombre);
        tilPrecio = (TextInputLayout) view.findViewById(R.id.act_prod_tilPrecio);
        tilDesc = (TextInputLayout) view.findViewById(R.id.act_prod_tilDescripcion);
        categoria = (TextInputEditText) view.findViewById(R.id.act_prod_categorias);
        nombre = (TextInputEditText) view.findViewById(R.id.act_prod_nombre);
        precio = (TextInputEditText) view.findViewById(R.id.act_prod_precio);
        desc = (TextInputEditText) view.findViewById(R.id.act_prod_descripcion);
        btnEliminar = (MaterialButton) view.findViewById(R.id.act_prod_img_eliminar);
        btnCambiar = (MaterialButton) view.findViewById(R.id.act_prod_img_cambiar);
        btnAgregar = (MaterialButton) view.findViewById(R.id.act_prod_img_agregar);
        btnModificar = (MaterialButton) view.findViewById(R.id.act_prod_modificar);








        categoria.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                    builder.setTitle("Categorias del producto");
                    builder.setMultiChoiceItems(nombreSec, secEscogidas, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            secEscogidas[which] = isChecked;
                        }
                    });
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            idSec.clear();
                            for (int i = 0; i < secEscogidas.length; i++) {
                                if (secEscogidas[i]) {
                                    idSec.add(secciones.get(i).getId());
                                    categoria.setText(i>0? "-"+secciones.get(i).getNombre() :secciones.get(i).getNombre());
                                }
                            }
                        }
                    });
                    builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    });
                    androidx.appcompat.app.AlertDialog dialog = builder.create();
                    dialog.show();
                    tilCategoria.clearFocus();
                    categoria.clearFocus();
                    nombre.requestFocus();
                }
            }
        });
        return view;
    }

    //**********************************************************************************************************
    //*************************************************Eventos ON***********************************************
    //**********************************************************************************************************
    /*public void OnclickDelMaterialButton(View view) {
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fRegProd_btnAgrImg:
                        if (CANTIMG_MAX > GlobalComercios.getInstance().getImageViews().size()) {
                            reemImg = false;
                            dialogoAgregarImagen();
                        } else {
                            btnElegirFoto.setEnabled(false);
                            Mensaje("Ha sobrepasado la cantidad maxima de imagenes");
                        }
                        break;
                    case R.id.fRegProd_btnElimImg:
                        if (GlobalComercios.getInstance().getImageViews().size() > 0) {
                            GlobalComercios.getInstance().getImageViews().remove(GlobalComercios.getInstance().getImgActual());
                            vie.notifyDataSetChanged();
                        } else {
                            Mensaje("No hay imagenes que borrar");
                        }
                        break;
                    case R.id.fRegProd_btnRegProd:
                        if (validaDatos()) {
                            enviarDatosRegistrar();
                        }
                        break;
                    case R.id.fRegProd_btnRemImg:
                        if (GlobalComercios.getInstance().getImageViews().size() > 0) {
                            reemImg = true;
                            dialogoAgregarImagen();
                        } else {
                            Mensaje("Debe elegir al menos una imagen");
                        }
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton*/
    // *********************************************************************************************************
    //***********************************************Fin Eventos ON*********************************************
    //**********************************************************************************************************


    //**********************************************************************************************************
    //*****************************************Fin Validaciones interfaz****************************************
    //**********************************************************************************************************

    //**********************************************************************************************************
    //*******************************************Validaciones interfaz******************************************
    //**********************************************************************************************************


    //**********************************************************************************************************
    //*****************************************Fin Validaciones interfaz****************************************
    //**********************************************************************************************************

    //**********************************************************************************************************
    //**********************************************************************************************************

    //Abre una ventana de dialogo (Tomar foto, elegir de la galeria y cancelar)
    private void dialogoAgregarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    //abriCamara();
                }else{
                    if (opciones[i].equals("Elegir de Galeria")){
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        //cod 10 para galeria
                        startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    //**********************************************************************************************************
    //**********************************************************************************************************


    //**********************************************************************************************************
    //*******************************************Conexion web service*******************************************
    //**********************************************************************************************************
    public void recuperarSeccionesComercio(int idComercio) {
        String consulta = "select id, nombre from Secciones where idComercio=" + idComercio;

        String url = Util.urlWebService + "/seccionesObtener.php?query=" + consulta;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if (mensajeError.equalsIgnoreCase("")) {
                        JSONArray seccion = jsonOb.getJSONArray("secciones");
                        if (seccion.length() != 0) {
                            for (int i = 0; i < seccion.length(); i++) {
                                JSONObject sec = seccion.getJSONObject(i);
                                secciones.add(new Seccion(sec.getInt("id"), sec.getString("nombre")));
                            }
                            nombreSec = new CharSequence[secciones.size()];
                            for (int i = 0; i < secciones.size(); i++) {
                                nombreSec[i] = secciones.get(i).getNombre();
                            }
                            secEscogidas = new boolean[secciones.size()];
                            recuperarSeccionesProducto(GlobalComercios.getInstance().getProducto().getId());
                        }
                    } else {
                        mensajeToast("No hay secciones");
                    }
                } catch (JSONException e) {
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
    public void recuperarSeccionesProducto(int idProducto) {
        String query = "SELECT s.id, s.nombre FROM SeccionesProductos sp INNER JOIN Secciones s ON sp.idSeccion = s.id WHERE sp.idProducto='" + idProducto + "'";
        String url = Util.urlWebService + "/seccionesObtener.php?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if (mensajeError.equalsIgnoreCase("")) {
                        JSONArray seccion = jsonOb.getJSONArray("secciones");
                        if (seccion.length() != 0) {
                            String escogidas = "";
                            for (int i = 0; i < seccion.length(); i++) {
                                JSONObject sec = seccion.getJSONObject(i);
                                int id = sec.getInt("id");
                                for(int j = 0; j < secciones.size(); j++){
                                    if(secciones.get(j).getId() == id)
                                        secEscogidas[j] = true;
                                    if(escogidas.equals("")){
                                        escogidas += secciones.get(j).getNombre();
                                    } else {
                                        escogidas += "-" + secciones.get(j).getNombre();
                                    }
                                }
                            }
                            categoria.setText(escogidas);
                        }
                    }
                } catch (JSONException e) {
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

    //**********************************************************************************************************
    //*****************************************Fin Conexion web service*****************************************
    //**********************************************************************************************************

    //Mensajes
    public void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};
    public void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};
}
