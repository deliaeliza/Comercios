package com.example.comercios;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Global.GlobalAdmin;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Global.GlobalSuperUsuario;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.Administrador;
import com.example.comercios.Modelo.Comercio;
import com.example.comercios.Modelo.UsuarioEstandar;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.Navigations.NavAdmin;
import com.example.comercios.Navigations.NavComercios;
import com.example.comercios.Navigations.NavSuperUsuario;
import com.example.comercios.Navigations.NavUsuarios;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PreLogin extends AppCompatActivity {

    JsonObjectRequest jsonObjectRequest;
    String correoGuardado;
    String passwordGuardado;
    Integer tipoGuardado;
    //ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mensajeAB("Comercios CR");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prelogin);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("usuarioSesion", MODE_PRIVATE);
        if(prefs!=null){
            correoGuardado = prefs.getString("correo", "");//" " valor default
            passwordGuardado = prefs.getString("password", ""); //" "valor default.
            tipoGuardado = prefs.getInt("tipo", 0);
            enviarDatosLogin();
        }else{
            Intent intento = new Intent(getApplicationContext(), Login.class);
            startActivity(intento);
        }

    }
    public void enviarDatosLogin() {
/*
        final ProgressDialog progreso = new ProgressDialog(getApplicationContext());
        progreso.setMessage("Iniciando...");
        progreso.show();*//*
        progress = ProgressDialog.show(this, "Iniciando..",
                "Esto puede tomar unos segundos.", true);*/

        String url = Util.urlWebService + "/login.php?correo=" +
                correoGuardado + "&contrasena=" + passwordGuardado;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progreso.hide();
                //progress.dismiss();
                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if(mensajeError.equalsIgnoreCase("")){
                        JSONObject user = jsonOb.getJSONObject("usuario");
                        int estado = user.optInt("estado");
                        if (estado == 1) {
                            int tipo = user.optInt("tipo");
                            if (tipo == Util.USUARIO_ADMINISTRADOR) {
                                GlobalAdmin.getInstance().setAdmin(new Administrador(
                                        user.optInt("id"),
                                        tipo,
                                        user.optLong("telAdmin"),
                                        estado != 0,
                                        user.optString("correo"),
                                        user.optString("usuario"),
                                        user.optString("contrasena")
                                ));
                                Intent intento = new Intent(getApplicationContext(), NavAdmin.class);
                                startActivity(intento);
                            } else if (tipo == Util.USUARIO_COMERCIO) {
                                GlobalComercios.getInstance().setComercio(new Comercio(
                                        user.optInt("id"),
                                        tipo,
                                        user.isNull("telComercio")? -1 :user.optLong("telComercio"),
                                        user.optInt("verificado") !=0,
                                        estado != 0,
                                        user.optString("correo"),
                                        user.getString("usuario"),
                                        user.optString("descripcion"),
                                        user.optString("ubicacion"),
                                        user.optString("nombreCat"),
                                        user.isNull("urlImagen") ? null : Util.urlWebService + "/" + user.getString("urlImagen"),
                                        Double.parseDouble(user.optString("longitud")),
                                        Double.parseDouble(user.optString("latitud")),
                                        user.optInt("idCategoria"),
                                        user.optString("contrasena"),
                                        (float) user.getDouble("calificacion"),
                                        user.getInt("cantidad")
                                ));

                                Intent intento = new Intent(getApplicationContext(), NavComercios.class);
                                startActivity(intento);
                            } else if (tipo == Util.USUARIO_ESTANDAR) {
                                SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                                GlobalUsuarios.getInstance().setUserE(new UsuarioEstandar(
                                        user.optInt("id"),
                                        tipo,
                                        estado != 0,
                                        null,
                                        user.optString("correo"),
                                        user.optString("usuario"),
                                        formatoFecha.parse(user.getString("fechaNac"))
                                ));

                                Intent intento = new Intent(getApplicationContext(), NavUsuarios.class);
                                startActivity(intento);
                            } else if (tipo == Util.USUARIO_SUPER) {
                                GlobalSuperUsuario.getInstance().setAdmin(new Administrador(
                                        user.optInt("id"),
                                        tipo,
                                        user.optLong("telefono"),
                                        estado != 0,
                                        user.optString("correo"),
                                        user.optString("usuario")
                                ));
                                Intent intento = new Intent(getApplicationContext(), NavSuperUsuario.class);
                                startActivity(intento);
                            }
                        } else if (estado == 0) {
                            mensajeToast("Su cuenta se encuentra desactivada");
                            Intent intento = new Intent(getApplicationContext(), Login.class);
                            startActivity(intento);
                        }
                    } else {
                        mensajeToast(mensajeError);
                        Intent intento = new Intent(getApplicationContext(), Login.class);
                        startActivity(intento);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progress.dismiss();
                mensajeToast("Inténtelo más tarde");
            }
        });
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void mensajeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public void mensajeAB(String msg) {
        Spannable text = new SpannableString(msg);
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
    }
}
