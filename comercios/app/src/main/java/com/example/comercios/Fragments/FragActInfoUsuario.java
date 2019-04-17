package com.example.comercios.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragActInfoUsuario extends Fragment {
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;

    EditText usuario, correo, password, confiPassword;

    public FragActInfoUsuario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_act_info_usuario, container, false);
        OnclickDelButton(view.findViewById(R.id.fActInfoUser_btnAct));
        return view;

    }
    public void OnclickDelButton(View view) {
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fActInfoUser_btnAct:
                        correo = (EditText) getView().findViewById(R.id.fActInfoUser_edtEmail);
                        usuario = (EditText) getView().findViewById(R.id.fActInfoUser_edtUser);
                        password = (EditText) getView().findViewById(R.id.fActInfoUser_edtPass);
                        confiPassword = (EditText) getView().findViewById(R.id.fActInfoUser_edtConfPass);
                        if (!usuario.getText().toString().equalsIgnoreCase("") &&
                                !correo.getText().toString().equalsIgnoreCase("") &&
                                !password.getText().toString().equalsIgnoreCase("") &&
                                !confiPassword.getText().toString().equalsIgnoreCase("")) {

                            if (password.getText().toString().equals(confiPassword.getText().toString())) {
                                actualizarUsuario();
                            } else {
                                Mensaje("Las contraseñas no coinciden");
                            }
                        } else {
                            Mensaje("Complete todos los datos");
                        }
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }
    public void actualizarUsuario(){
        String url = Util.urlWebService + "/actualizarInfoUsuario.php?usuario="+usuario.getText().toString()+
                "&correo="+correo.getText().toString()+"&contrasena="+password.getText().toString()+"&id="+"3";

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("Se actualizo correctamente")) {
                    correo.setText("");
                    usuario.setText("");
                    password.setText("");
                    confiPassword.setText("");
                    Mensaje(response.trim());
                } else {
                    Mensaje(response.trim());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Mensaje("Intentelo mas tarde");
            }
        })/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", Integer.toString(3));
                parametros.put("correo", correo.getText().toString());
                parametros.put("usuario", usuario.getText().toString());
                parametros.put("contrasena", password.getText().toString());
                return parametros;
            }
        }*/;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);

    }

    public void Mensaje(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    };

}
