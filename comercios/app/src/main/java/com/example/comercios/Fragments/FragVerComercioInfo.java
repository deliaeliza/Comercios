package com.example.comercios.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Filtros.FiltrosUsuarioEstandar;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.UsuarioEstandar;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import static android.Manifest.permission.CALL_PHONE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragVerComercioInfo extends Fragment {
    private final int MIS_PERMISOS = 100;
    private TextView cantVotos;
    private RatingBar ratingBar;
    Dialog dialog;
    public FragVerComercioInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.frag_ver_comercio_info, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.frag_ver_comercio_info_imageview);
        TextView nombre = (TextView) view.findViewById(R.id.frag_ver_comercio_info_nombre);
        TextView correo = (TextView) view.findViewById(R.id.frag_ver_comercio_info_correo);
        TextView telefono = (TextView) view.findViewById(R.id.frag_ver_comercio_info_telefono);
        TextView ubicacion = (TextView) view.findViewById(R.id.frag_ver_comercio_info_ubicacion);
        TextView descripcion = (TextView) view.findViewById(R.id.frag_ver_comercio_info_descrip);
        cantVotos = (TextView) view.findViewById(R.id.frag_ver_comercio_info_cantVotos);
        ratingBar = (RatingBar) view.findViewById(R.id.frag_ver_comercio_info_ratingBar);
        if(GlobalUsuarios.getInstance().getComercio().getImagen() != null)
            imageView.setImageBitmap(GlobalUsuarios.getInstance().getComercio().getImagen());
        nombre.setText(GlobalUsuarios.getInstance().getComercio().getUsuario());
        descripcion.setText(GlobalUsuarios.getInstance().getComercio().getDescripcion());
        ubicacion.setText(GlobalUsuarios.getInstance().getComercio().getUbicacion());
        correo.setText(GlobalUsuarios.getInstance().getComercio().getCorreo());
        telefono.setText(GlobalUsuarios.getInstance().getComercio().getTelefono() + "");
        cantVotos.setText(GlobalUsuarios.getInstance().getComercio().getCantCalificaciones() + " Votos");
        ratingBar.setRating(GlobalUsuarios.getInstance().getComercio().getCalificacion());
        DialogoCalificar();

        OnclickDelMaterialCardView(view.findViewById(R.id.frag_ver_comercio_info_calificar_MaterialCardView));
        OnclickDelMaterialCardView(view.findViewById(R.id.frag_ver_comercio_info_correo_MaterialCardView));
        OnclickDelMaterialCardView(view.findViewById(R.id.frag_ver_comercio_info_telefono_MaterialCardView));
        return view;
    }

    public void OnclickDelMaterialCardView(View view) {
        MaterialCardView miMaterialCardView = (MaterialCardView)  view;
        miMaterialCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.frag_ver_comercio_info_calificar_MaterialCardView:
                        dialog.show();
                        break;
                    case R.id.frag_ver_comercio_info_correo_MaterialCardView:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setData(Uri.parse("mailto:"));
                        String[] to = { GlobalUsuarios.getInstance().getComercio().getCorreo() };
                        i.putExtra(Intent.EXTRA_EMAIL, to);
                        i.setType("message/rfc822");
                        startActivity(Intent.createChooser(i, "Email"));
                        break;
                    case R.id.frag_ver_comercio_info_telefono_MaterialCardView:
                        MarcaryLlamar(GlobalUsuarios.getInstance().getComercio().getTelefono() + "");
                        break;
                    default:break; }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialCardView

    private void DialogoCalificar(){
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_calificar_comercio);
        MaterialCardView calificar = (MaterialCardView) dialog.findViewById(R.id.dialogo_calificar_comercio_MaterialCardViewCalificar);
        MaterialCardView cancelar = (MaterialCardView) dialog.findViewById(R.id.dialogo_calificar_comercio_MaterialCardViewCancelar);
        final RatingBar ratingBarAct = (RatingBar)dialog.findViewById(R.id.dialogo_calificar_comercio_ratingBar);
        final TextView error = (TextView) dialog.findViewById(R.id.dialogo_calificar_comercio_error);
        error.setVisibility(View.INVISIBLE);
        ratingBarAct.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(((int)rating) > 0){
                    error.setVisibility(View.INVISIBLE);
                }
            }
        });
        calificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarEstrellas((int)ratingBarAct.getRating(), error)){
                    calificar((int)ratingBarAct.getRating());
                    dialog.cancel();
                }

            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.INVISIBLE);
                dialog.cancel();

            }
        });
    };

    private boolean validarEstrellas(int estrellas, TextView error){
        if(estrellas > 0){
            error.setVisibility(View.INVISIBLE);
            return true;
        }
        error.setVisibility(View.VISIBLE);
        return false;
    }

    private void calificar(int calificacion){
        //Consultar a la base
        String parametros = "calificacion="+calificacion+"&idUsuario="+GlobalUsuarios.getInstance().getUserE().getId()+
                "&idComercio="+GlobalUsuarios.getInstance().getComercio().getId();
        String url = Util.urlWebService + "/calificarComercio.php?" + parametros;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if(mensajeError.equalsIgnoreCase("")){
                        if(jsonOb.has("totales")) {
                            JSONObject totales = jsonOb.getJSONObject("totales");
                            GlobalUsuarios.getInstance().getComercio().setCalificacion((float)totales.getDouble("promedio"));
                            GlobalUsuarios.getInstance().getComercio().setCantCalificaciones(totales.getInt("cantidad"));
                            cantVotos.setText(totales.getInt("cantidad") + " Votos");
                            ratingBar.setRating((float)totales.getDouble("promedio"));
                        }
                    } else {
                        mensajeToast(mensajeError);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Error, intentelo más tarde");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    private void MarcaryLlamar(String numero) {
        Intent i = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:" + numero));
        if (ActivityCompat.checkSelfPermission(getContext(), CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if(!solicitaPermisosVersionesSuperioresLlamar())
                return;
        }
        startActivity(i);
    };
    private void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};

    private void cargarDialogoRecomendacionLlamar() {
        androidx.appcompat.app.AlertDialog.Builder dialogo = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{CALL_PHONE}, MIS_PERMISOS);
            }
        });
        dialogo.show();
    }

    private boolean solicitaPermisosVersionesSuperioresLlamar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }
        //validamos si los permisos ya fueron aceptados
        if (getActivity().checkSelfPermission(CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(CALL_PHONE)) {
            cargarDialogoRecomendacionLlamar();
        } else {
            requestPermissions(new String[]{CALL_PHONE}, MIS_PERMISOS);
        }
        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MIS_PERMISOS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {

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

}
