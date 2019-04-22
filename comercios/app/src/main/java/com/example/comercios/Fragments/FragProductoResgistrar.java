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
import android.app.Fragment;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragProductoResgistrar extends Fragment {

    private TextInputEditText categoria, nombre, descripcion, precio;
    private TextInputLayout lyNombre, lyDescr, lyPre, lyCategoria;
    StringRequest stringRequest;
    private Button btnElegirFoto, btnRemFoto, btnElim;

    private ArrayList<Seccion> secciones;
    private ArrayList<Integer> idSec;
    private CharSequence[] nombreSec;
    private boolean[] secEscogidas;

    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private static final int CANTIMG_MAX = 5;

    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    viewPagerAdapter vie;
    ViewPager viewpager;
    private boolean reemImg = false;

    public FragProductoResgistrar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mensajeAB("Registrar producto");
        View view = inflater.inflate(R.layout.frag_producto_resgistrar, container, false);
        secciones = new ArrayList<>();
        idSec = new ArrayList<>();
        recuperarCategoriasComercio(GlobalComercios.getInstance().getComercio().getId());
        viewpager = (ViewPager) view.findViewById(R.id.fRegProd_viewPager);
        vie = new viewPagerAdapter(getActivity(), GlobalComercios.getInstance().getImageViews());
        viewpager.setAdapter(vie);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setPageMargin(70);
        btnElegirFoto = (MaterialButton) view.findViewById(R.id.fRegProd_btnAgrImg);
        btnElim = (MaterialButton) view.findViewById(R.id.fRegProd_btnElimImg);
        btnRemFoto = (MaterialButton) view.findViewById(R.id.fRegProd_btnRemImg);

        nombre = (TextInputEditText) view.findViewById(R.id.fRegProd_edtNombre);
        descripcion = (TextInputEditText) view.findViewById(R.id.fRegProd_edtDescripcion);
        precio = (TextInputEditText) view.findViewById(R.id.fRegProd_edtPrecio);
        categoria = (TextInputEditText) view.findViewById(R.id.fRegProd_edtTEsgCat);
        lyCategoria = (TextInputLayout) view.findViewById(R.id.fRegProd_tilEsgCat);
        lyNombre = (TextInputLayout) view.findViewById(R.id.fRegProd_txtNombre);
        lyPre = (TextInputLayout) view.findViewById(R.id.fRegProd_txtPrecio);
        lyDescr = (TextInputLayout) view.findViewById(R.id.fRegProd_txtDescripcion);

        btnElim.setVisibility(View.GONE);
        btnRemFoto.setVisibility(View.GONE);

        //Permisos
        btnElegirFoto.setEnabled(solicitaPermisosVersionesSuperiores() == true);

        OnclickDelButton(btnElegirFoto);
        OnclickDelButton(btnRemFoto);
        OnclickDelButton(view.findViewById(R.id.fRegProd_btnRegProd));
        OnclickDelButton(btnElim);

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
                lyPre.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view; // debe comentar el otro return
    }

    public void OnclickDelTextInputEditText(View v){
        TextInputEditText txt = (TextInputEditText)v;
        txt.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    final boolean[] aux = new boolean[secEscogidas.length];
                    for(int i = 0; i < secEscogidas.length; i++){
                        aux[i] = secEscogidas[i];
                    }
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                    nombre.requestFocus();
                }
            }
        });
    }
    public void OnclickDelButton(View view) {
        MaterialButton miButton = (MaterialButton) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fRegProd_btnAgrImg:
                        if (CANTIMG_MAX > GlobalComercios.getInstance().getImageViews().size()) {
                            reemImg = false;
                            mostrarDialogOpciones();
                        }
                        break;
                    case R.id.fRegProd_btnElimImg:
                        if (GlobalComercios.getInstance().getImageViews().size() > 0) {
                            GlobalComercios.getInstance().getImageViews().remove(GlobalComercios.getInstance().getImgActual());
                            if(GlobalComercios.getInstance().getImageViews().size() == 0){
                                btnElim.setVisibility(View.GONE);
                                btnRemFoto.setVisibility(View.GONE);
                                viewpager.setBackgroundResource(R.drawable.ic_menu_camera);
                            }
                            vie.notifyDataSetChanged();

                        } else {
                            mensaje("No hay imagenes que borrar");
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
                            mostrarDialogOpciones();
                        } else {
                            mensaje("Debe elegir al menos una imagen");
                        }
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton

    public void recuperarCategoriasComercio(int idComercio) {
        String consulta = "select id, nombre from Secciones where nombre <> 'DEFAULT' and idComercio=" + idComercio;

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
                                OnclickDelTextInputEditText(categoria);
                            }
                    } else {
                        mensaje("No hay productos");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensaje("No se puede conectar " + error.toString());
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void enviarDatosRegistrar() {
        String url = Util.urlWebService + "/productoRegistrar.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("Se registro correctamente")) {
                    nombre.setText("");
                    precio.setText("");
                    descripcion.setText("");
                    mensaje(response);
                } else {
                    mensaje(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensaje("No se ha podido conectar" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idComercio", Integer.toString(GlobalComercios.getInstance().getComercio().getId()));
                parametros.put("nombre", nombre.getText().toString());
                parametros.put("precio", precio.getText().toString());
                parametros.put("descripcion", descripcion.getText().toString());
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

    public boolean validaDatos() {
        boolean a = validarNombre();
        boolean b = validarPrecio();
        return a && b;
    }

    public boolean validarNombre() {
        String nomb = nombre.getText().toString();
        if (nomb.length() > 0 && nomb.length() <= 45 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(nomb).find()) {
            lyNombre.setError(null);
            return true;
        }
        lyNombre.setError("Nombre invalido");
        return false;
    }

    private boolean validarPrecio(){
        String dato = precio.getText().toString();
        if (dato.length() > 0) {
            try {
                int i = Integer.parseInt(precio.getText().toString().trim());
                lyPre.setError(null);
                return true;
            } catch (NumberFormatException ex){
                lyPre.setError("Precio invalido");
                return false;
            }
        }
        lyPre.setError(null);//El precio es opcional
        return true;
    }

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

    private void mostrarDialogOpciones() {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    abriCamara();
                } else {
                    if (opciones[i].equals("Elegir de Galeria")) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONA);
                    } else {
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
        vie.notifyDataSetChanged();
        viewpager.setCurrentItem(GlobalComercios.getInstance().getImgActual());
        if(CANTIMG_MAX == GlobalComercios.getInstance().getImageViews().size()){
            btnElegirFoto.setEnabled(false);
            mensaje("Ha llegado al máximo de imagenes");
        } else if(GlobalComercios.getInstance().getImageViews().size() == 1){
            btnElim.setVisibility(View.VISIBLE);
            btnRemFoto.setVisibility(View.VISIBLE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MIS_PERMISOS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {//el dos representa los 2 permisos
                mensaje("Permisos aceptados");
                btnElegirFoto.setEnabled(true);
            }
        } else {
            solicitarPermisosManual();
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getActivity());//estamos en fragment
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
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
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

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, MIS_PERMISOS, array);
        byte[] imagenByte = array.toByteArray();
        return Base64.encodeToString(imagenByte, Base64.DEFAULT);
    }

    public void mensaje(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};


}
