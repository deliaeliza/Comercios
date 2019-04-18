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
import android.util.Base64;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Adapter.viewPagerAdapter;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragProductoResgistrar extends Fragment {

    private TextInputEditText nombre, descripcion, precio;
    StringRequest stringRequest;
    private Button btnElegirFoto, btnRemFoto;

    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    viewPagerAdapter vie;

    public FragProductoResgistrar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_producto_resgistrar, container, false);

        ViewPager viewpager = (ViewPager) view.findViewById(R.id.fRegProd_viewPager);
        vie = new viewPagerAdapter(getActivity(), GlobalComercios.getInstance().getImageViews());
        viewpager.setAdapter(vie);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setPageMargin(-200);
        btnElegirFoto = (Button) view.findViewById(R.id.fRegProd_btnAgrImg);
        //Permisos
        if (solicitaPermisosVersionesSuperiores()) {
            btnElegirFoto.setEnabled(true);
        } else {
            btnElegirFoto.setEnabled(false);
        }
        OnclickDelButton(btnElegirFoto);
        OnclickDelButton(view.findViewById(R.id.fRegProd_btnElimImg));
        OnclickDelButton(view.findViewById(R.id.fRegProd_btnRegProd));
        OnclickDelButton(view.findViewById(R.id.fRegProd_btnRemImg));

        return view;
    }

    public void OnclickDelButton(View view) {
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fRegProd_btnAgrImg:
                        mostrarDialogOpciones();
                        break;
                    case R.id.fRegProd_btnElimImg:
                        Mensaje("Implementar Button2");

                        break;
                    case R.id.fRegProd_btnRegProd:
                        nombre = (TextInputEditText) v.findViewById(R.id.fRegProd_edtNombre);
                        descripcion = (TextInputEditText) v.findViewById(R.id.fRegProd_edtDescripcion);
                        precio = (TextInputEditText) v.findViewById(R.id.fRegProd_edtPrecio);
                        if (validaDatos()) {

                        }

                        break;
                    case R.id.fRegProd_btnRemImg:
                        Mensaje("Implementar Button4");

                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton

    public void Mensaje(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, MIS_PERMISOS, array);
        byte[] imagenByte = array.toByteArray();
        return Base64.encodeToString(imagenByte, Base64.DEFAULT);
    }

    public void enviarDatosRegistrar() {
        String url = Util.urlWebService + "/productoRegistrar.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("registra")) {
                    nombre.setText("");
                    precio.setText("");
                    descripcion.setText("");
                    Mensaje(response);
                } else {
                    Mensaje(response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Mensaje("No se ha podido conectar");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //String imagen=convertirImgString(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("idComercio", Integer.toString(GlobalComercios.getInstance().getComercio().getId()));
                //faltan las secciones a la que pertenecera
                parametros.put("nombre", nombre.getText().toString());
                parametros.put("precio", precio.getText().toString());
                parametros.put("descripcion", descripcion.getText().toString());
                //imagenes que se subiran, pueden ser 5
                //parametros.put("imagen",imagen);

                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
    }

    public boolean validaDatos() {
        return false;
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
        GlobalComercios.getInstance().getImageViews().add(redimensionarImagen(imagen1, 600, 800));
        vie.notifyDataSetChanged();
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
                Mensaje("Permisos aceptados");
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
}
