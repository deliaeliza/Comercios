package com.example.comercioscostarica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercioscostarica.Modelo.Util;
import com.example.comercioscostarica.Modelo.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    JsonObjectRequest jsonObjectRequest;
    private EditText correo;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mensajeAB("Ingresar");
        correo = (EditText) findViewById(R.id.Login_edtEmail);
        password = (EditText) findViewById(R.id.Login_edtPass);
        OnclickDelButton(R.id.Login_btnIgresar);
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
                    JSONArray json = response.optJSONArray("datos");
                    String mensajeError = json.getJSONObject(0).getString("mensajeError");
                    if(mensajeError.equalsIgnoreCase("")){
                        JSONObject user = json.getJSONObject(1);
                        int estado = user.optInt("estado");
                        if (estado == 1) {
                            int tipo = user.optInt("tipo");
                            if (tipo == Util.USUARIO_ADMINISTRADOR) {
                                Intent intento = new Intent(getApplicationContext(), NavAdmin.class);
                                startActivity(intento);
                            } else if (tipo == Util.USUARIO_COMERCIO) {
                                //Intent intento = new Intent(getApplicationContext(), SegundaActivity.class);
                                //startActivity(intento);
                            } else if (tipo == Util.USUARIO_ESTANDAR) {
                                //Intent intento = new Intent(getApplicationContext(), SegundaActivity.class);
                                //startActivity(intento);
                            } else if (tipo == Util.USUARIO_SUPER) {
                                //Intent intento = new Intent(getApplicationContext(), SegundaActivity.class);
                                //startActivity(intento);
                            }
                        } else if (estado == 0) {
                            mensajeToast("Su cuenta se encuentra desactivada");
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
                mensajeToast("No se puede conectar " + error.toString());
            }
        });
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void OnclickDelButton(int ref) {
        View view = findViewById(ref);
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Login_btnIgresar:
                        enviarDatosLogin();
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton

    public void OnclickDelTextView(int ref) {
        View view = findViewById(ref);
        TextView miTextView = (TextView) view;
        miTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Login_txtRegistrar:
                        Intent intento = new Intent(getApplicationContext(), Registro.class);
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
