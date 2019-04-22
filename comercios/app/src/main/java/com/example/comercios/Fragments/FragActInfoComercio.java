package com.example.comercios.Fragments;



import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Categorias;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragActInfoComercio extends Fragment  {
    StringRequest stringRequest;
    StringRequest stringRequest2;
    ImageView fotoComercio;
    JsonObjectRequest jsonObjectRequest;
    MaterialSpinner spinner;
    ArrayList<Categorias> categorias;
    ArrayList<String> categoriasArray;
    Bitmap bitmap;
    Button btnFoto;

    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen; //donde se almacenara la imagen

    ///objetos de la interfaz
    int categoriaSeleccionada;
    TextInputEditText descripcion,telefono,correo,password,confiPassword,ubicacion,usuario;
    TextInputLayout LayoutDescripcion,LayoutTelefono, LayoutCorreo,LayoutUsuario,LayoutPsw,LayoutConfPsw;

    //para guardar la info actual del usuario
    String CDescripcion,CUsuario,CTelefono,CCorreo,CContra,CUbicacion,CUrlImagen;

    public FragActInfoComercio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mensajeAB("Cuenta");
        GlobalComercios.getInstance().setVentanaActual(R.layout.frag_act_info_comercio);
        View view = inflater.inflate(R.layout.frag_act_info_comercio, container, false);

        descripcion = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtDescripcion);
        LayoutDescripcion = (TextInputLayout)view.findViewById(R.id.fActInfoComercio_widDescripcion);

        usuario= (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtUsuario);
        LayoutUsuario = (TextInputLayout)view.findViewById(R.id.fActInfoComercio_widUsuario);

        telefono = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtTelefono);
        LayoutTelefono = (TextInputLayout)view.findViewById(R.id.fActInfoComercio_widTelefono);

        correo = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtCorreo);
        LayoutCorreo = (TextInputLayout)view.findViewById(R.id.fActInfoComercio_widCorreo);

        password = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtPass);
        LayoutPsw = (TextInputLayout)view.findViewById(R.id.fActInfoComercio_widPass);

        confiPassword = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtConfiPass);
        LayoutConfPsw = (TextInputLayout)view.findViewById(R.id.fActInfoComercio_widConfiPass);

        ubicacion = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtUbicacion);

        OnTextChangedDelTextInputEditText(descripcion);
        OnTextChangedDelTextInputEditText(usuario);
        OnTextChangedDelTextInputEditText(telefono);
        OnTextChangedDelTextInputEditText(correo);
        OnTextChangedDelTextInputEditText(password);
        OnTextChangedDelTextInputEditText(confiPassword);

        cargarCategorias(view);
        cargarDatosAnteriores(view);
        fotoComercio = view.findViewById(R.id.fActInfoComercio_imagen);
        //Permisos para camara
        btnFoto = view.findViewById(R.id.fActInfoComercio_cambiarFoto);
        if(solicitaPermisosVersionesSuperiores()){
            btnFoto.setEnabled(true);
        }else{
            btnFoto.setEnabled(false);
        }
        OnclickDelButton(view.findViewById(R.id.fActInfoComercio_btnUbicacion));
        OnclickDelButton(view.findViewById(R.id.fActInfoComercio_cambiarFoto));
        OnclickDelButton(view.findViewById(R.id.fActInfoComercio_btnAct));

        return view;

    }

    private void cargarDatosAnteriores(View view) {

        //GlobalComercios.getInstance().getComercio().getId();
        String url = Util.urlWebService + "/obtenerInfoComercio.php?id="+ GlobalComercios.getInstance().getComercio().getId();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String r= String.valueOf(response.getJSONObject("comercio"));
                    if(r!="") {
                        JSONObject jsonComercio = response.getJSONObject("comercio");
                        usuario.setText(jsonComercio.getString("usuario"));
                        CUsuario = jsonComercio.getString("usuario");
                        descripcion.setText(jsonComercio.getString("descripcion"));
                        CDescripcion = jsonComercio.getString("descripcion");
                        correo.setText(jsonComercio.getString("correo"));
                        CCorreo=jsonComercio.getString("correo");
                        ubicacion.setText(jsonComercio.getString("ubicacion"));
                        CUbicacion=jsonComercio.getString("ubicacion");
                        telefono.setText(jsonComercio.getString("telefono"));
                        CTelefono = jsonComercio.getString("telefono");

                        categoriaSeleccionada = Integer.parseInt(jsonComercio.getString("idCategoria"));
                        for(int i=0;i<categorias.size();i++){
                            if(categorias.get(i).getId() ==Integer.parseInt(jsonComercio.getString("idCategoria"))){
                                spinner.setHint(categorias.get(i).getNombre());
                            }
                        }
                        CContra = jsonComercio.getString("contrasena");
                        CUrlImagen =jsonComercio.getString("urlImagen");
                        String ruta_foto= Util.urlWebService +"/"+CUrlImagen;
                        cargarWebServicesImagen(ruta_foto);
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
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);

    }

    private void cargarWebServicesImagen(String ruta_foto) {
        ImageRequest imagR = new ImageRequest(ruta_foto, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                fotoComercio.setImageBitmap(response);
                bitmap=response;
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Mensaje("error al cargar la imagen");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(imagR);
    }

    public void OnclickDelButton(final View view) {

        Button miButton = (Button)  view;
        miButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.fActInfoComercio_btnAct:
                        if(!descripcion.getText().toString().equalsIgnoreCase("")||
                                !usuario.getText().toString().equalsIgnoreCase("")||
                                !telefono.getText().toString().equalsIgnoreCase("")||
                                !correo.getText().toString().equalsIgnoreCase("")||
                                !password.getText().toString().equalsIgnoreCase("")||
                                !ubicacion.getText().toString().equalsIgnoreCase("")||
                                categoriaSeleccionada != -1 || bitmap!=null
                                ){

                            if(password.getText().toString().equals(confiPassword.getText().toString())){
                                actulizarInformacion();
                            }
                            else {
                                Mensaje("Las contrase;as no coinciden");
                            }

                        }else{
                            Mensaje("Error,Complete los datos que desea modificar");
                        }
                        break;

                    case R.id.fActInfoComercio_btnUbicacion:

                        break;

                    case R.id.fActInfoComercio_cambiarFoto:
                        mostrarDialogOpciones();
                        break;
                    default:break; }// fin de casos
            }
        });
    }
    private void actulizarInformacion() {

       final ProgressDialog progreso = new ProgressDialog(getActivity());
       progreso.setMessage("Esperando respuesta...");
       progreso.show();

       final String imagenConveritda = convertirImgString(bitmap);
        String url = Util.urlWebService + "/actualizarInfoComercio.php?";

      /*descripcion="+descripcion.getText().toString()
                +"&categoria="+categoriaSeleccionada+"&telefono="+telefono.getText().toString()+
                "&ubicacion="+ubicacion.getText().toString()+"&imagen="+imagenConveritda+"&id="+"38";
*/
        stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();
                if (response.trim().equalsIgnoreCase("correcto")) {
                    Mensaje("Actualización éxitosa");
                } else {

                    Mensaje("Sucedio un error al intentar actualizar");

                }
            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Mensaje("Intentelo mas tarde");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                //id hay que tomarlo del usuario logueado
                //GlobalComercios.getInstance().getComercio().getId();
                parametros.put("id",String.valueOf(GlobalComercios.getInstance().getComercio().getId()));

                if(!descripcion.getText().toString().equalsIgnoreCase("")){
                    parametros.put("descripcion",descripcion.getText().toString());
                }else{
                    parametros.put("descripcion",CDescripcion);
                }

                if(!usuario.getText().toString().equalsIgnoreCase("")){
                    parametros.put("usuario",usuario.getText().toString());
                }else{
                    parametros.put("usuario",CUsuario);
                }

                parametros.put("categoria",String.valueOf(categoriaSeleccionada));

                if(!telefono.getText().toString().equalsIgnoreCase("")){
                    parametros.put("telefono",telefono.getText().toString());
                }else{
                    parametros.put("telefono",CTelefono);
                }

                if(!correo.getText().toString().equalsIgnoreCase("")){
                    parametros.put("correo",correo.getText().toString());
                }else{
                    parametros.put("correo",CCorreo);
                }

                if(!password.getText().toString().equalsIgnoreCase("")){
                    parametros.put("contrasena",password.getText().toString());
                }else {
                    parametros.put("contrasena",CContra);
                }

                if(!ubicacion.getText().toString().equalsIgnoreCase("")){
                    parametros.put("ubicacion",ubicacion.getText().toString());
                }else {
                    parametros.put("ubicacion",CUbicacion);
                }

                parametros.put("imagen",imagenConveritda);

                return parametros;
            }
        };
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest2);

    }
    public void Mensaje(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    public void cargarCategorias(View view){
        categorias = new ArrayList<>();
        categoriasArray= new ArrayList<>();
        String url = Util.urlWebService + "/categoriasObtener.php";

       spinner = (MaterialSpinner) view.findViewById(R.id.fActInfoComercio_SPcategorias);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonCategorias= response.getJSONArray("categoria");
                    JSONObject obj;
                    for(int i= 0;i<jsonCategorias.length();i++) {
                        obj = jsonCategorias.getJSONObject(i);
                        categoriasArray.add(obj.getString("nombre"));
                        categorias.add(new Categorias(obj.getInt("id"),obj.getString("nombre")));
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
        spinner.setHint("Seleccione una categoria");
        spinner.setItems(categoriasArray);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                categoriaSeleccionada=ObtenerIdCategoria(item);

            }
        });

        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
    private void mostrarDialogOpciones() {

        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    abriCamara();
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
    private void abriCamara() {
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

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = getActivity().getPackageName() + ".provider";
                Uri imageUri = FileProvider.getUriForFile(getActivity(), authorities, fileImagen);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            startActivityForResult(takePictureIntent,COD_FOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case COD_SELECCIONA:
                Uri miPath=data.getData();
                fotoComercio.setImageURI(miPath);
                try {
                    bitmap=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),miPath);
                    fotoComercio.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(getActivity(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });

                bitmap= BitmapFactory.decodeFile(path);
                fotoComercio.setImageBitmap(bitmap);

                break;
        }
        bitmap=redimensionarImagen(bitmap,600,800);

    }
    //permisos
    /////////////////////////////////////////////////////////////////////////
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MIS_PERMISOS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {//el dos representa los 2 permisos
                Mensaje("Permisos aceptados");
                btnFoto.setEnabled(true);
            }
        } else {
            solicitarPermisosManual();
        }
    }
    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones = new androidx.appcompat.app.AlertDialog.Builder(getActivity());//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
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
    private void cargarDialogoRecomendacion() {
        androidx.appcompat.app.AlertDialog.Builder dialogo = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
            }
        });
        dialogo.show();
    }
    ///////////////////////////////////////////////////////////////////////////////////
    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, MIS_PERMISOS, array);
        byte[] imagenByte = array.toByteArray();
        return Base64.encodeToString(imagenByte, Base64.DEFAULT);
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
    private int ObtenerIdCategoria(String cat){
        for(int i =0;i<categorias.size();i++){
            if(categorias.get(i).getNombre().equalsIgnoreCase(cat)){
                return categorias.get(i).getId();
            }
        }
        return -1;
    }

    private void OnTextChangedDelTextInputEditText(final TextInputEditText textInputEditText){
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int id = textInputEditText.getId();
                switch (id){
                    case R.id.fActInfoComercio_edtCorreo:
                        validarCorreo();
                        break;
                    case R.id.fActInfoComercio_edtUsuario:
                        validarUsuario();
                        break;
                    case R.id.fActInfoComercio_edtPass:
                        validarContrasena();
                        validarConfContrasena();
                        break;
                    case R.id.fActInfoComercio_edtConfiPass:
                        validarConfContrasena();
                        break;
                    case R.id.fActInfoComercio_edtDescripcion:
                        validarDescripcion();
                        break;
                    case R.id.fActInfoComercio_edtTelefono:
                        validarTelefono();
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private  boolean validarDescripcion(){
        String dato = descripcion.getText().toString();
        if (dato.length() > 500)
            return false;
        if (dato.length() > 0 && dato.length() <= 500 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()) {
            LayoutDescripcion.setError(null);
            return true;
        }
        LayoutDescripcion.setError("Descripción invalido");
        return false;
    }
    private boolean validarTelefono(){
        String dato = telefono.getText().toString();
        if (dato.length() > 20)
            return false;
        if (dato.length() > 0 && dato.length() <= 20) {
            LayoutTelefono.setError(null);
            return true;
        }
        LayoutTelefono.setError("Descripción invalido");
        return false;
    }
    private boolean validarCorreo(){
        String dato = correo.getText().toString();
        if (dato.length() > 46)
            return false;
        if (dato.length() > 0 && dato.length() <= 45 && Patterns.EMAIL_ADDRESS.matcher(dato).find()) {
            LayoutCorreo.setError(null);
            return true;
        }
        LayoutCorreo.setError("Email invalido");
        return false;
    }
    private boolean validarUsuario(){
        String dato = usuario.getText().toString();
        if (dato.length() > 46)
            return false;
        if (dato.length() > 0 && dato.length() <= 45 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()) {
            LayoutUsuario.setError(null);
            return true;
        }
        LayoutUsuario.setError("Usuario invalido");
        return false;
    }
    private boolean validarContrasena(){
        String dato = password.getText().toString();
        if (dato.length() > 46)
            return false;
        if (dato.length() <= 45 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()) {
            LayoutPsw.setError(null);
            return true;
        }
        LayoutPsw.setError("Contraseña invalida");
        return false;
    }
    private boolean validarConfContrasena(){
        String dato1 = password.getText().toString();
        String dato2 = confiPassword.getText().toString();
        if (dato1.equals(dato2)) {
            LayoutConfPsw.setError(null);
            return true;
        }
        LayoutConfPsw.setError("Las contraseñas no coinciden");
        return false;
    }
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};


}
