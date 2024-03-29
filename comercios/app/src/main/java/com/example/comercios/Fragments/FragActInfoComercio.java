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
import android.graphics.drawable.BitmapDrawable;
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
import com.example.comercios.Modelo.Comercio;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragActInfoComercio extends Fragment {
    StringRequest stringRequest;
    StringRequest stringRequest2;
    ImageView fotoComercio;
    JsonObjectRequest jsonObjectRequest;
    MaterialSpinner spinner;
    ArrayList<Categorias> categorias;
    ArrayList<String> categoriasArray;
    Bitmap bitmap;
    MaterialButton btnFoto, btnEliminarFoto;
    Comercio comercioActual;

    private String path;//almacena la ruta de la imagen
    File fileImagen; //donde se almacenara la imagen

    ///objetos de la interfaz
    int categoriaSeleccionada;
    TextInputEditText descripcion, telefono, correo, password, confiPassword, ubicacion, usuario;
    TextInputLayout LayoutDescripcion, LayoutTelefono, LayoutCorreo, LayoutUsuario, LayoutPsw, LayoutConfPsw;
    Double latitud, longitud;


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

        categorias = new ArrayList<>();
        categoriasArray = new ArrayList<>();
        comercioActual = GlobalComercios.getInstance().getComercio();

        descripcion = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtDescripcion);
        LayoutDescripcion = (TextInputLayout) view.findViewById(R.id.fActInfoComercio_widDescripcion);

        usuario = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtUsuario);
        LayoutUsuario = (TextInputLayout) view.findViewById(R.id.fActInfoComercio_widUsuario);

        telefono = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtTelefono);
        LayoutTelefono = (TextInputLayout) view.findViewById(R.id.fActInfoComercio_widTelefono);

        correo = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtCorreo);
        LayoutCorreo = (TextInputLayout) view.findViewById(R.id.fActInfoComercio_widCorreo);

        password = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtPass);
        LayoutPsw = (TextInputLayout) view.findViewById(R.id.fActInfoComercio_widPass);

        confiPassword = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtConfiPass);
        LayoutConfPsw = (TextInputLayout) view.findViewById(R.id.fActInfoComercio_widConfiPass);

        ubicacion = (TextInputEditText) view.findViewById(R.id.fActInfoComercio_edtUbicacion);
        spinner = (MaterialSpinner) view.findViewById(R.id.fActInfoComercio_SPcategorias);

        usuario.setText(GlobalComercios.getInstance().getComercio().getUsuario());
        descripcion.setText(GlobalComercios.getInstance().getComercio().getDescripcion());
        correo.setText(GlobalComercios.getInstance().getComercio().getCorreo());
        ubicacion.setText(GlobalComercios.getInstance().getComercio().getUbicacion());
        telefono.setText(Long.toString(GlobalComercios.getInstance().getComercio().getTelefono()));
        latitud = GlobalComercios.getInstance().getComercio().getLatitud();
        longitud = GlobalComercios.getInstance().getComercio().getLongitud();

        OnTextChangedDelTextInputEditText(descripcion);
        OnTextChangedDelTextInputEditText(usuario);
        OnTextChangedDelTextInputEditText(telefono);
        OnTextChangedDelTextInputEditText(correo);
        OnTextChangedDelTextInputEditText(password);
        OnTextChangedDelTextInputEditText(confiPassword);

        fotoComercio = view.findViewById(R.id.fActInfoComercio_imagen);
        //Permisos para camara
        btnFoto = (MaterialButton) view.findViewById(R.id.fActInfoComercio_cambiarFoto);
        btnEliminarFoto = (MaterialButton) view.findViewById(R.id.fActInfoComercio_eliminarFoto);

        if (solicitaPermisosVersionesSuperiores()) {
            btnFoto.setEnabled(true);
        } else {
            btnFoto.setEnabled(false);
        }

        cargarCategorias(view);

        if (GlobalComercios.getInstance().getComercio().getUrlImagen() != null ||
                !GlobalComercios.getInstance().getComercio().getUrlImagen().equalsIgnoreCase("")) {
            if (GlobalComercios.getInstance().getComercio().getImagen() == null) {
                cargarWebServicesImagen(GlobalComercios.getInstance().getComercio().getUrlImagen());
            } else {
                bitmap = GlobalComercios.getInstance().getComercio().getImagen();
                fotoComercio.setImageBitmap(bitmap);
            }
        }

        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoNotificarUbicacionComercio();
            }
        });

        OnclickDelButton(view.findViewById(R.id.fActInfoComercio_cambiarFoto));
        OnclickDelButton(view.findViewById(R.id.fActInfoComercio_btnAct));
        OnclickDelButton(view.findViewById(R.id.fActInfoComercio_eliminarFoto));

        return view;

    }

    private void cargarWebServicesImagen(String ruta_foto) {
        ImageRequest imagR = new ImageRequest(ruta_foto, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                fotoComercio.setImageBitmap(response);
                bitmap = response;
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(imagR);
    }

    public void OnclickDelButton(final View view) {

        MaterialButton miButton = (MaterialButton) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fActInfoComercio_btnAct:
                        if (validarCorreo() && validarDescripcion() && validarTelefono() &&
                                validarUsuario() && validarContrasena() && validarConfContrasena()) {
                            actulizarInformacion();
                        }
                        break;
                    case R.id.fActInfoComercio_cambiarFoto:
                        mostrarDialogOpciones();
                        break;
                    case R.id.fActInfoComercio_eliminarFoto:
                        bitmap = null;
                        fotoComercio.setImageResource(R.drawable.ic_menu_camera);
                        break;
                    default:
                        break;
                }// fin de casos
            }
        });
    }

    private void actulizarInformacion() {
        final ProgressDialog progreso = new ProgressDialog(getActivity());
        progreso.setMessage("Actualizando...");
        progreso.show();

        String url = Util.urlWebService + "/actualizarInfoComercio.php?";
        stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();
                if (response.trim().equalsIgnoreCase("correcto")) {
                    mensajeToast("Actualización éxitosa");
                    GlobalComercios.getInstance().getComercio().setDescripcion(descripcion.getText().toString());
                    GlobalComercios.getInstance().getComercio().setUsuario(usuario.getText().toString());
                    GlobalComercios.getInstance().getComercio().setIdCategoria(categoriaSeleccionada);
                    GlobalComercios.getInstance().getComercio().setTelefono(Long.parseLong(telefono.getText().toString()));
                    GlobalComercios.getInstance().getComercio().setCorreo(correo.getText().toString());
                    GlobalComercios.getInstance().getComercio().setContrasena(password.getText().toString());
                    GlobalComercios.getInstance().getComercio().setUbicacion(ubicacion.getText().toString());
                    GlobalComercios.getInstance().getComercio().setLatitud(latitud);
                    GlobalComercios.getInstance().getComercio().setLongitud(longitud);
                    GlobalComercios.getInstance().getComercio().setImagen(bitmap);
                } else {
                    mensajeToast("Error al actualizar");
                    GlobalComercios.getInstance().setComercio(comercioActual);
                    descripcion.setText(comercioActual.getDescripcion());
                    usuario.setText(comercioActual.getUsuario());
                    for (int i = 0; i < categoriasArray.size(); i++) {
                        if (categoriasArray.get(i).equalsIgnoreCase(comercioActual.getCategoria())) {
                            spinner.setSelectedIndex(i);
                            break;
                        }
                    }
                    telefono.setText(Long.toString(comercioActual.getTelefono()));
                    correo.setText(comercioActual.getCorreo());
                    latitud = comercioActual.getLatitud();
                    longitud = comercioActual.getLongitud();
                    bitmap = comercioActual.getImagen();
                    fotoComercio.setImageBitmap(bitmap);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                mensajeToast("Error, Inténtelo más tarde");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", String.valueOf(GlobalComercios.getInstance().getComercio().getId()));
                parametros.put("descripcion", descripcion.getText().toString());
                parametros.put("usuario", usuario.getText().toString());
                parametros.put("categoria", String.valueOf(categoriaSeleccionada));
                parametros.put("telefono", telefono.getText().toString());
                parametros.put("correo", correo.getText().toString());
                parametros.put("contrasena", password.getText().toString());
                parametros.put("ubicacion", ubicacion.getText().toString());
                parametros.put("latitud", String.valueOf(latitud));
                parametros.put("longitud", String.valueOf(longitud));
                final String imagenConveritda = convertirImgString(bitmap);
                parametros.put("imagen", imagenConveritda);
                return parametros;
            }
        };
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest2);

    }

    private void mensajeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void cargarCategorias(View view) {
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

                    for (int i = 0; i < categoriasArray.size(); i++) {
                        if (categoriasArray.get(i).equalsIgnoreCase(GlobalComercios.getInstance().getComercio().getCategoria())) {
                            spinner.setSelectedIndex(i);
                            break;
                        }
                    }

                    categoriaSeleccionada = GlobalComercios.getInstance().getComercio().getIdCategoria();
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
                mensajeToast("Error, Inténtelo más tarde");
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
                    abrirCamara();
                } else {
                    if (opciones[i].equals("Elegir de Galeria")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        //cod 10 para galeria
                        startActivityForResult(intent.createChooser(intent, "Seleccione"), Util.COD_SELECCIONA);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    public void DialogoNotificarUbicacionComercio() {
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder1.setTitle("¿Esta dentro de su negocio?");
        builder1.setMessage("Para poder obtener correctamente la ubicacin de su negocio, debe de estar dentro de su negocio" +
                ", la ubicación es obligatoria para el registro");
        builder1.setCancelable(true);
        builder1.setNegativeButton("Intentar más tarde",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder1.setPositiveButton("Obtener ubicación",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        locationStart();
                    }
                });
        androidx.appcompat.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(), Util.DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();
        if (isCreada == false) {
            isCreada = miFile.mkdirs();
        }
        if (isCreada == true) {
            Long consecutivo = System.currentTimeMillis() / 1000;
            String nombre = consecutivo.toString() + ".jpg";
            path = Environment.getExternalStorageDirectory() + File.separator + Util.DIRECTORIO_IMAGEN
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
            startActivityForResult(takePictureIntent, Util.COD_FOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case Util.COD_SELECCIONA:
                    Uri miPath = data.getData();
                    fotoComercio.setImageURI(miPath);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), miPath);
                        fotoComercio.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case Util.COD_FOTO:

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
            bitmap = redimensionarImagen(bitmap, Util.IMAGEN_ANCHO, Util.IMAGEN_ALTO);
        }

    }

    //permisos/////////////////////////////////////////////////////////////////////////
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
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, Util.MIS_PERMISOS);
        }
        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Util.MIS_PERMISOS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {//el dos representa los 2 permisos
                mensajeToast("Permisos aceptados");
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
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, Util.MIS_PERMISOS);
            }
        });
        dialogo.show();
    }

    ///////////////////////////////////////////////////////////////////////////////////
    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, Util.MIS_PERMISOS, array);
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
        LayoutDescripcion.setError("Descripción invalida");
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
        LayoutTelefono.setError("Teléfono invalido");
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

    ;

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
        FragActInfoComercio mainActivity;

        public FragActInfoComercio getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(FragActInfoComercio mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            latitud = loc.getLatitude();
            longitud = loc.getLongitude();

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
    ////////////////////////////////////////LOCALIZACION/////////////////////////////////////////////////////////
}

