package com.example.comercios.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Categorias;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragRegEmpresa extends Fragment {

    StringRequest stringRequest;
    ImageView fotoComercio;
    JsonObjectRequest jsonObjectRequest;
    MaterialSpinner spinner;
    ArrayList<Categorias> categorias;
    ArrayList<String> categoriasArray;
    Bitmap bitmap;
    Button btnFoto;
    Button btnUbicacion;
    String latitud;
    String longitud;

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
    TextInputEditText descripcion, telefono, correo, password, confiPassword, ubicacion, usuario;
    TextInputLayout LayoutDescripcion, LayoutTelefono, LayoutCorreo, LayoutUsuario, LayoutPsw, LayoutConfPsw;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    public FragRegEmpresa() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mensajeAB("Registrarme");
        View view = inflater.inflate(R.layout.frag_reg_empresa, container, false);
        GlobalComercios.getInstance().setVentanaActual(R.layout.frag_reg_empresa);

        categorias = new ArrayList<>();
        categoriasArray = new ArrayList<>();

        descripcion = (TextInputEditText) view.findViewById(R.id.fragRegComercio_edtDescripcion);
        LayoutDescripcion = (TextInputLayout) view.findViewById(R.id.fragRegComercio_widDescripcion);
        usuario = (TextInputEditText) view.findViewById(R.id.fragRegComercio_edtUsuario);
        LayoutUsuario = (TextInputLayout) view.findViewById(R.id.fragRegComercio_widUsuario);
        telefono = (TextInputEditText) view.findViewById(R.id.fragRegComercio_edtTelefono);
        LayoutTelefono = (TextInputLayout) view.findViewById(R.id.fragRegComercio_widTelefono);
        correo = (TextInputEditText) view.findViewById(R.id.fragRegComercio_edtCorreo);
        LayoutCorreo = (TextInputLayout) view.findViewById(R.id.fragRegComercio_widCorreo);
        password = (TextInputEditText) view.findViewById(R.id.fragRegComercio_edtPass);
        LayoutPsw = (TextInputLayout) view.findViewById(R.id.fragRegComercio_widPass);
        confiPassword = (TextInputEditText) view.findViewById(R.id.fragRegComercio_edtConfiPass);
        LayoutConfPsw = (TextInputLayout) view.findViewById(R.id.fragRegComercio_widConfiPass);
        ubicacion = (TextInputEditText) view.findViewById(R.id.fragRegComercio_edtUbicacion);
        spinner = (MaterialSpinner) view.findViewById(R.id.fragRegComercio_SPcategorias);

        OnTextChangedDelTextInputEditText(descripcion);
        OnTextChangedDelTextInputEditText(usuario);
        OnTextChangedDelTextInputEditText(telefono);
        OnTextChangedDelTextInputEditText(correo);
        OnTextChangedDelTextInputEditText(password);
        OnTextChangedDelTextInputEditText(confiPassword);

        fotoComercio = view.findViewById(R.id.fragRegComercio_imagen);
        //Permisos para camara
        btnFoto = view.findViewById(R.id.fragRegComercio_elegirFoto);
        if (solicitaPermisosVersionesSuperiores()) {
            btnFoto.setEnabled(true);
        } else {
            btnFoto.setEnabled(false);
        }
        btnUbicacion = view.findViewById(R.id.fragRegComercio_btnUbicacion);
        if (solicitaPermisosVersionesSuperioresGPS()) {
            btnUbicacion.setEnabled(true);
        } else {
            btnUbicacion.setEnabled(false);
        }
        cargarCategorias(view);
        OnclickDelButton(btnUbicacion);
        OnclickDelButton(view.findViewById(R.id.fragRegComercio_elegirFoto));
        OnclickDelButton(view.findViewById(R.id.fragRegComercio_btnAct));

        return view;
    }

    public void OnclickDelButton(final View view) {
        MaterialButton miButton = (MaterialButton) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fragRegComercio_btnAct:
                        if(validarCorreo() && validarDescripcion() && validarTelefono() &&
                                validarUsuario() && validarContrasena() && validarConfContrasena()){
                            registrarComercio();
                        }
                        break;
                    case R.id.fragRegComercio_btnUbicacion:
                        locationStart();
                        break;

                    case R.id.fragRegComercio_elegirFoto:
                        mostrarDialogOpciones();
                        break;
                    default:
                        break;
                }// fin de casos
            }
        });
    }

    private void registrarComercio() {
        final ProgressDialog progreso = new ProgressDialog(getActivity());
        progreso.setMessage("Esperando respuesta...");
        progreso.show();

        final String imagenConveritda = convertirImgString(bitmap);
        String url = Util.urlWebService + "/comercioRegistrar.php?";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();
                if (response.trim().equalsIgnoreCase("registra")) {
                    Mensaje("Se registro correctamente");
                } else {
                    Mensaje("No se registro correctamente");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Mensaje("Intentelo mas tarde");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("descripcion", descripcion.getText().toString());
                parametros.put("usuario", usuario.getText().toString());
                parametros.put("categoria", String.valueOf(categoriaSeleccionada));
                parametros.put("telefono", telefono.getText().toString());
                parametros.put("correo", correo.getText().toString());
                parametros.put("contrasena", password.getText().toString());
                parametros.put("ubicacion", ubicacion.getText().toString());
                parametros.put("imagen", imagenConveritda);
                parametros.put("tipo", Integer.toString(Util.USUARIO_COMERCIO));
                parametros.put("latitud", latitud);
                parametros.put("longitud", longitud);
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
    }

    public void Mensaje(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void cargarCategorias(View view) {
        String url = Util.urlWebService + "/categoriasObtener.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonCategorias = response.getJSONArray("categoria");
                    JSONObject obj;
                    for (int i = 0; i < jsonCategorias.length(); i++) {
                        obj = jsonCategorias.getJSONObject(i);
                        categoriasArray.add(obj.getString("nombre"));
                        categorias.add(new Categorias(obj.getInt("id"), obj.getString("nombre")));
                    }
                    spinner.setItems(categoriasArray);
                    spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                            categoriaSeleccionada = ObtenerIdCategoria(item);
                        }
                    });

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
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        //cod 10 para galeria
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

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = getActivity().getPackageName() + ".provider";
                Uri imageUri = FileProvider.getUriForFile(getActivity(), authorities, fileImagen);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            startActivityForResult(takePictureIntent, COD_FOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COD_SELECCIONA:
                Uri miPath = data.getData();
                fotoComercio.setImageURI(miPath);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), miPath);
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
                                Log.i("Path", "" + path);
                            }
                        });

                bitmap = BitmapFactory.decodeFile(path);
                fotoComercio.setImageBitmap(bitmap);

                break;
        }
        bitmap = redimensionarImagen(bitmap, 600, 800);

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

    private boolean solicitaPermisosVersionesSuperioresGPS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }
        //validamos si los permisos ya fueron aceptados
        if ((getActivity().checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && getActivity().checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if ((shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) || (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)))) {
            cargarDialogoRecomendacionGPS();
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MIS_PERMISOS);
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

        /*if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }*/
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

    private void cargarDialogoRecomendacionGPS() {
        androidx.appcompat.app.AlertDialog.Builder dialogo = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MIS_PERMISOS);
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

    private int ObtenerIdCategoria(String cat) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getNombre().equalsIgnoreCase(cat)) {
                return categorias.get(i).getId();
            }
        }
        return -1;
    }

    private void OnTextChangedDelTextInputEditText(final TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int id = textInputEditText.getId();
                switch (id) {
                    case R.id.fragRegComercio_edtCorreo:
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
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean validarDescripcion() {
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

    private boolean validarTelefono() {
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

    private boolean validarCorreo() {
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

    private boolean validarUsuario() {
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

    private boolean validarContrasena() {
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

    private boolean validarConfContrasena() {
        String dato1 = password.getText().toString();
        String dato2 = confiPassword.getText().toString();
        if (dato1.equals(dato2)) {
            LayoutConfPsw.setError(null);
            return true;
        }
        LayoutConfPsw.setError("Las contraseñas no coinciden");
        return false;
    }

    private void mensajeAB(String msg) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(msg);
    }


    /*---------------------------------------------UBICACION--------------------------------------------------------------*/

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

        //cordenadas = "Localizacion agregada";
        ubicacion.setText("");
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                   ubicacion.setText(DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Localizacion implements LocationListener {
        FragRegEmpresa mainActivity;

        public FragRegEmpresa getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(FragRegEmpresa mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            latitud = Double.toString(loc.getLatitude());
            longitud = Double.toString(loc.getLongitude());

            //String Text = loc.getLatitude() + "()" + loc.getLongitude();
            //cordenadas = Text;
            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //cordenadas = "GPS Desactivado";
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //cordenadas = "GPS Activado";
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}
