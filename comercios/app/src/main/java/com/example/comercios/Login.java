package com.example.comercios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    JsonObjectRequest jsonObjectRequest;
    private TextInputEditText correo;
    private TextInputEditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mensajeAB("Ingresar");
        correo = (TextInputEditText) findViewById(R.id.Login_edtEmail);
        password = (TextInputEditText) findViewById(R.id.Login_edtPass);
        OnclickDelMaterialButton(R.id.Login_btnIgresar);
        OnclickDelTextView(R.id.Login_txtRegistrar);
        OnclickDelTextView(R.id.Login_txtOlvido);
    }

    public void enviarDatosLogin() {
        String url = Util.urlWebService + "/login.php?correo=" +
                correo.getText().toString() + "&contrasena=" + password.getText().toString();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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
                                        user.optLong("telComercio"),
                                        user.optInt("verificado") !=0,
                                        estado != 0,
                                        user.optString("correo"),
                                        user.getString("usuario"),
                                        user.optString("descripcion"),
                                        user.optString("ubicacion"),
                                        user.optString("nombreCat"),
                                        user.optString("urlImagen"),
                                        Double.parseDouble(user.optString("longitud")),
                                        Double.parseDouble(user.optString("latitud")),
                                        user.optInt("idCategoria"),
                                        user.optString("contrasena")
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
                        }
                    } else {
                        mensajeToast(mensajeError);
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
                mensajeToast("Inténtelo mas tarde");
            }
        });
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void OnclickDelMaterialButton(int ref) {
        View view = findViewById(ref);
        MaterialButton miButton = (MaterialButton) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatosLogin();
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialButton

    public void OnclickDelTextView(int ref) {
        View view = findViewById(ref);
        TextView miTextView = (TextView) view;
        miTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Login_txtRegistrar:
                        Intent intento = new Intent(getApplicationContext(), Registrar.class);
                        startActivity(intento);
                        break;
                    case R.id.Login_txtOlvido:
                        mensajeToast("Implementar activity de olvido");
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelTextView




    public void mensajeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public void mensajeAB(String msg) {
        getSupportActionBar().setTitle(msg);
    }
}
