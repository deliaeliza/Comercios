package com.example.comercios.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragActInfoProductos extends Fragment {
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
    private ViewPager viewpager;
    private com.example.comercios.Adapter.viewPagerAdapter viewPagerAdapter;
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    private boolean reemImg = false;
    public fragActInfoProductos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_act_info_productos, container, false);
        mensajeAB("Modificar Producto");
        GlobalComercios.getInstance().setVentanaActual(R.layout.frag_act_info_productos);
        GlobalComercios.getInstance().getImageViews().clear();
        secciones = new ArrayList<>();
        idSec = new ArrayList<>();
        viewpager = (ViewPager) view.findViewById(R.id.act_prod_viewPager);
        viewPagerAdapter = new viewPagerAdapter(getActivity(), GlobalComercios.getInstance().getImageViews());
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setPageMargin(70);

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
        //btnModificar = (MaterialButton) view.findViewById(R.id.act_prod_modificar);
        btnEliminar.setVisibility(View.GONE);
        btnCambiar.setVisibility(View.GONE);
        //recuperarSeccionesComercio(GlobalComercios.getInstance().getComercio().getId());
        recuperarSeccionesComercio(4);
        //nombre.setText(GlobalComercios.getInstance().getProducto().getNombre());
        nombre.setText("Celular");
        //precio.setText(GlobalComercios.getInstance().getProducto().getPrecio());
        precio.setText(200 +"");
        //desc.setText(GlobalComercios.getInstance().getProducto().getDescripcion());
        desc.setText("4 RAM, 128GB");
        //Permisos
        btnAgregar.setEnabled(solicitaPermisosVersionesSuperiores() == true);
        recuperarImagenes();
        OnclickDelMaterialButton(btnAgregar);
        OnclickDelMaterialButton(btnCambiar);
        OnclickDelMaterialButton(btnEliminar);
        OnclickDelMaterialButton(view.findViewById(R.id.act_prod_modificar));
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarNombre();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        precio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPrecio.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    private void OnFocusDelTextInputEditText(View v){
        TextInputEditText txt = (TextInputEditText)v;
        txt.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    final boolean[] aux = new boolean[secEscogidas.length];
                    for(int i = 0; i < secEscogidas.length; i++){
                        aux[i] = secEscogidas[i];
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Categorias del producto");
                    builder.setMultiChoiceItems(nombreSec, aux, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            aux[which] = isChecked;
                        }
                    });
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            idSec.clear();
                            String escogidas = "";
                            for(int i = 0; i < secEscogidas.length; i++){
                                secEscogidas[i] = aux[i];
                            }
                            for (int i = 0; i < secEscogidas.length; i++) {
                                if (secEscogidas[i]) {
                                    idSec.add(secciones.get(i).getId());
                                    if(!escogidas.equals("")){
                                        escogidas += "-";
                                    }
                                    escogidas += secciones.get(i).getNombre();
                                }
                            }
                            categoria.setText(escogidas);
                        }
                    });
                    builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    nombre.requestFocus();
                }
            }
        });
    }
    //**********************************************************************************************************
    //*************************************************Eventos ON***********************************************
    public void OnclickDelMaterialButton(View view) {
        MaterialButton miButton = (MaterialButton) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.act_prod_img_agregar:
                        if (CANTIMG_MAX > GlobalComercios.getInstance().getImageViews().size()) {
                            reemImg = false;
                            dialogoAgregarImagen();
                        }
                        break;
                    case R.id.act_prod_img_eliminar:
                        if (GlobalComercios.getInstance().getImageViews().size() > 0) {
                            GlobalComercios.getInstance().getImageViews().remove(GlobalComercios.getInstance().getImgActual());
                            if(GlobalComercios.getInstance().getImageViews().size() == 0){
                                btnEliminar.setVisibility(View.GONE);
                                btnCambiar.setVisibility(View.GONE);
                                viewpager.setBackgroundResource(R.drawable.ic_menu_camera);
                            }
                            viewPagerAdapter.notifyDataSetChanged();

                        } else {
                            mensajeToast("No hay imagenes que borrar");
                        }

                        break;
                    case R.id.act_prod_modificar:
                        if (validarDatos()) {
                            enviarDatosModificar();
                        }
                        break;
                    case R.id.act_prod_img_cambiar:
                        if (GlobalComercios.getInstance().getImageViews().size() > 0) {
                            reemImg = true;
                            dialogoAgregarImagen();
                        } else {
                            mensajeToast("No hay imagenes que cambiar");
                        }
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton
    //***********************************************Fin Eventos ON*********************************************
    //**********************************************************************************************************

    //**********************************************************************************************************
    //*******************************************Validaciones interfaz******************************************
    private boolean validarDatos(){
        boolean nom = validarNombre();
        boolean pre = validarPrecio();
        return nom && pre;
    }
    private boolean validarNombre(){
        String dato = nombre.getText().toString();
        if (dato.length() > 0 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()) {
            tilNombre.setError(null);
            return true;
        }
        tilNombre.setError("Nombre invalido");
        return false;
    }
    private boolean validarPrecio(){
        String dato = precio.getText().toString();
        if (dato.length() > 0) {
            try {
                int i = Integer.parseInt(precio.getText().toString().trim());
                tilPrecio.setError(null);
                return true;
            } catch (NumberFormatException ex){
                tilPrecio.setError("Precio invalido");
                return false;
            }
        }
        tilPrecio.setError(null);//El precio es opcional
        return true;
    }
    //*****************************************Fin Validaciones interfaz****************************************
    //**********************************************************************************************************

    //**********************************************************************************************************
    //***************************************************Fotos**************************************************
    private void dialogoAgregarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    abrirCamara();
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
    private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();
        if (isCreada == false) {
            isCreada = miFile.mkdirs();
        }
        if (isCreada == true) {
            Long consecutivo = System.currentTimeMillis() / 1000;
            String nombre = consecutivo.toString() + ".jpg";
            path = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN
                    + File.separator + nombre;//indicamos la ruta de almacenamiento
            fileImagen = new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = getActivity().getPackageName() + ".provider";
                Uri imageUri = FileProvider.getUriForFile(getActivity(), authorities, fileImagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            startActivityForResult(intent, COD_FOTO);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imagen1 = null;
        switch (requestCode) {
            case COD_SELECCIONA:
                Uri miPath = data.getData();
                try {
                    imagen1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), miPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(getActivity(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path", "" + path);
                            }
                        });
                imagen1 = BitmapFactory.decodeFile(path);
                break;
        }
        imagen1 = redimensionarImagen(imagen1, Util.IMAGEN_ANCHO, Util.IMAGEN_ALTO);
        if (reemImg) {
            GlobalComercios.getInstance().getImageViews().remove(GlobalComercios.getInstance().getImgActual());
            GlobalComercios.getInstance().getImageViews().add(GlobalComercios.getInstance().getImgActual(), imagen1);
        } else {
            GlobalComercios.getInstance().agregarImagenes(imagen1);
        }
        viewPagerAdapter.notifyDataSetChanged();
        viewpager.setCurrentItem(GlobalComercios.getInstance().getImgActual());
        if(CANTIMG_MAX == GlobalComercios.getInstance().getImageViews().size()){
            btnAgregar.setEnabled(false);
            mensajeToast("Ha llegado al máximo de imagenes");
        } else if(GlobalComercios.getInstance().getImageViews().size() == 1){
            btnEliminar.setVisibility(View.VISIBLE);
            btnCambiar.setVisibility(View.VISIBLE);
            viewpager.setBackgroundResource(R.color.design_default_color_surface);
        }
    }
    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();
        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;
            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);
            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);
        } else {
            return bitmap;
        }
    }
    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, MIS_PERMISOS, array);
        byte[] imagenByte = array.toByteArray();
        return Base64.encodeToString(imagenByte, Base64.DEFAULT);
    }
    //*************************************************Fin Fotos************************************************
    //**********************************************************************************************************


    //**********************************************************************************************************
    //*******************************************Conexion web service*******************************************
    public void recuperarSeccionesComercio(int idComercio) {
        String consulta = "SELECT id, nombre FROM Secciones WHERE nombre <> 'DEFAULT' AND idComercio='" + idComercio + "'";

        String url = Util.urlWebService + "/seccionesObtenerPertenece.php?query=" + consulta + "&idProducto='"+ GlobalComercios.getInstance().getProducto() +"'";

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
                            secEscogidas = new boolean[seccion.length()];
                            nombreSec = new CharSequence[seccion.length()];
                            for (int i = 0; i < seccion.length(); i++) {
                                JSONObject sec = seccion.getJSONObject(i);
                                secciones.add(new Seccion(sec.getInt("id"), sec.getString("nombre")));
                                nombreSec[i] = sec.getString("nombre");
                                if(sec.getInt("pertenece") == 1){
                                    secEscogidas[i] = true;
                                    if(!escogidas.equals("")){
                                        escogidas += "-";
                                    }
                                    escogidas += sec.getString("nombre");
                                }
                            }
                            categoria.setText(escogidas);
                            OnFocusDelTextInputEditText(categoria);
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
                mensajeToast("Error, Inténtelo más tarde");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    public void enviarDatosModificar() {
        String url = Util.urlWebService + "/productoModificar.php?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("")) {
                    //GlobalComercios.getInstance().getProducto().setNombre(nombre.getText().toString());
                    //GlobalComercios.getInstance().getProducto().setPrecio(Integer.parseInt(precio.getText().toString().trim()));
                    //GlobalComercios.getInstance().getProducto().setDescripcion(desc.getText().toString());
                    //GlobalComercios.getInstance().getProducto().setEstado(true);
                    mensajeToast("Actualización éxitosa");
                } else {
                    mensajeToast(response);
                    //nombre.setText(GlobalComercios.getInstance().getProducto().getNombre());
                    //precio.setText(GlobalComercios.getInstance().getProducto().getPrecio().trim());
                    //desc.setText(GlobalComercios.getInstance().getProducto().getDescripcion());
                    //estado.set
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("No se ha podido conectar" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                //parametros.put("idComercio", Integer.toString(GlobalComercios.getInstance().getComercio().getId()));
                parametros.put("idComercio", 4 + "");
                //parametros.put("idProducto", Integer.toString(GlobalComercios.getInstance().getProducto().getId()));
                parametros.put("idProducto", 2 + "");
                String update = "UPDATE Productos SET nombre='"+nombre.getText().toString().trim() + "'";
                if(precio.getText().toString().equals("")){
                    update += ", precio = null";
                } else {
                    update += ", precio = '" + precio.getText().toString().trim() + "'";
                }
                if(desc.getText().toString().equals("")){
                    update += ", descripcion = null";
                } else {
                    update += ", descripcion = '" + desc.getText().toString().trim() + "'";
                }
                //if(estado == true){
                update += ", estado = '1'";
                //} else {
                //update += ", estado = '0'";
                //}
                //update += " WHERE id='" + GlobalComercios.getInstance().getProducto().getId() + "'";
                update += " WHERE id='" + 2 + "'";
                parametros.put("update", update);
                int cantImg = GlobalComercios.getInstance().getImageViews().size();
                parametros.put("cantImg", Integer.toString(cantImg));
                parametros.put("cantSec", Integer.toString(idSec.size()));
                int idImgen = 1;
                for (Bitmap img : GlobalComercios.getInstance().getImageViews()) {
                    parametros.put("img" + idImgen, convertirImgString(img));
                    idImgen = idImgen + 1;
                }
                int idSe = 1;
                for (Integer secid : idSec) {
                    parametros.put("sec" + idSe, Integer.toString(secid));
                    idSe = idSe + 1;
                }
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
    }
    public void recuperarImagenes(){
        //String url = Util.urlWebService + "/usuariosEstandarObtener.php?id='" + GlobalComercios.getInstance().getProducto().getId() + "'";
        final String request = Util.urlWebService + "/productoRecuperarUrls.php?id='" + 9 + "'";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String error = response.getString("mensajeError");
                    if(error.equals("")){
                        if(response.has("urls")){
                            JSONArray urls = response.getJSONArray("urls");
                            for(int i = 0; i < urls.length(); i++){
                                String url = urls.getString(i);
                            }
                        }
                    } else {
                        mensajeToast(error);
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

    private void cargarWebServicesImagen(String urlImagen) {
        ImageRequest imagR = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                GlobalComercios.getInstance().agregarImagenes(response);
                viewPagerAdapter.notifyDataSetChanged();
                viewpager.setCurrentItem(GlobalComercios.getInstance().getImgActual());
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mensajeToast("No se encontro la imagen del comercio con correo: " + correo );
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(imagR);
    }

    //*****************************************Fin Conexion web service*****************************************
    //**********************************************************************************************************

    //**********************************************************************************************************
    //*************************************************Mensajes*************************************************
    public void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};
    public void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};
    //***********************************************Fin Mensajes***********************************************
    //**********************************************************************************************************

    //**********************************************************************************************************
    //***********************************************Permisos app***********************************************
    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }
        //validamos si los permisos ya fueron aceptados
        if ((getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && getActivity().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) || (shouldShowRequestPermissionRationale(CAMERA)))) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }
        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }
    private void cargarDialogoRecomendacion() {
        androidx.appcompat.app.AlertDialog.Builder dialogo = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialogo.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MIS_PERMISOS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {//el dos representa los 2 permisos
                mensajeToast("Permisos aceptados");
                btnAgregar.setEnabled(true);
            }
        } else {
            solicitarPermisosManual();
        }
    }
    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"Si", "No"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones = new androidx.appcompat.app.AlertDialog.Builder(getActivity());//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }
    //*********************************************Fin Permisos app*********************************************
    //**********************************************************************************************************

}
