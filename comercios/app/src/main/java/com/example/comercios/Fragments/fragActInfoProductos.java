package com.example.comercios.Fragments;


import android.app.AlertDialog;
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
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
    private String path;//almacena la ruta de la imagen
    File fileImagen;
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
        ViewPager viewpager = (ViewPager) view.findViewById(R.id.act_prod_viewPager);
        viewPager = new viewPagerAdapter(getActivity(), GlobalComercios.getInstance().getImageViews());
        viewpager.setAdapter(viewPager);
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
        //recuperarSeccionesComercio(GlobalComercios.getInstance().getComercio().getId());
        recuperarSeccionesComercio(4);
        //Permisos
        btnAgregar.setEnabled(solicitaPermisosVersionesSuperiores() == true);

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
        categoria.setOnFocusChangeListener(new View.OnFocusChangeListener(){
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
        return view;
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
                        if(CANTIMG_MAX == GlobalComercios.getInstance().getImageViews().size()){
                            btnAgregar.setEnabled(false);
                            mensajeToast("Ha llegado al máximo de imagenes");
                        }
                        break;
                    case R.id.act_prod_img_eliminar:
                        if (GlobalComercios.getInstance().getImageViews().size() > 0) {
                            GlobalComercios.getInstance().getImageViews().remove(GlobalComercios.getInstance().getImgActual());
                            viewPager.notifyDataSetChanged();
                        } else {
                            mensajeToast("No hay imagenes que borrar");
                        }
                        break;
                    case R.id.act_prod_modificar:
                        if (validarDatos()) {
                            //enviarDatosRegistrar();
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
        return validarNombre() && validarPrecio();
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
        if (dato.length() > 0 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()) {
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
    //fotos
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
        imagen1 = redimensionarImagen(imagen1, 600, 800);
        if (reemImg) {
            GlobalComercios.getInstance().getImageViews().remove(GlobalComercios.getInstance().getImgActual());
            GlobalComercios.getInstance().getImageViews().add(GlobalComercios.getInstance().getImgActual(), imagen1);
        } else {
            GlobalComercios.getInstance().agregarImagenes(imagen1);
        }
        viewPager.notifyDataSetChanged();
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


    //**********************************************************************************************************
    //**********************************************************************************************************


    //**********************************************************************************************************
    //*******************************************Conexion web service*******************************************
    public void recuperarSeccionesComercio(int idComercio) {
        String consulta = "SELECT id, nombre FROM Secciones WHERE nombre <> 'DEFAULT' AND idComercio='" + idComercio + "'";

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
                            //recuperarSeccionesProducto(GlobalComercios.getInstance().getProducto().getId());
                            recuperarSeccionesProducto(2);
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
                                    if(secciones.get(j).getId() == id) {
                                        secEscogidas[j] = true;
                                        idSec.add(id);
                                        if(!escogidas.equals("")){
                                            escogidas += "-";
                                        }
                                        escogidas += secciones.get(j).getNombre();
                                        break;
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




    //*****************************************Fin Conexion web service*****************************************
    //**********************************************************************************************************

    //Mensajes
    public void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};
    public void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};

    //Permisps
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
}
