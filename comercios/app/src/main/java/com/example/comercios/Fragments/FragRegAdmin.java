
package com.example.comercios.Fragments;

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
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;

import java.util.HashMap;
import java.util.Map;

public class FragRegAdmin extends Fragment {

    StringRequest stringRequest;
    EditText email, usuario, telefono, contrasena, confContra;

    public FragRegAdmin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_reg_admin, container, false);
        OnclickDelButton(view.findViewById(R.id.fRegAdmin_btnReg));
        return view;
    }

    public void OnclickDelButton(View view) {
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fRegAdmin_btnReg:
                        email = (EditText) getView().findViewById(R.id.fRegAdmin_edtEmail);
                        usuario = (EditText) getView().findViewById(R.id.fRegAdmin_edtUser);
                        telefono = (EditText) getView().findViewById(R.id.fRegAdmin_edtTelefono);
                        contrasena = (EditText) getView().findViewById(R.id.fRegAdmin_edtPass);
                        confContra = (EditText) getView().findViewById(R.id.fRegAdmin_edtConfPass);
                        if (!email.getText().toString().equalsIgnoreCase("") &&
                                !usuario.getText().toString().equalsIgnoreCase("") &&
                                !telefono.getText().toString().equalsIgnoreCase("") &&
                                !contrasena.getText().toString().equalsIgnoreCase("") &&
                                !confContra.getText().toString().equalsIgnoreCase("")) {
                            if (contrasena.getText().toString().equals(confContra.getText().toString())) {
                                registrarAdministrador();
                            } else {
                                MensajeToast("Las contraseñas no coinciden");
                            }
                        } else {
                            MensajeToast("Complete todos los datos");
                        }
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton

    private void registrarAdministrador() {
        String url = Util.urlWebService + "/adminRegistrar.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("Se registro correctamente")) {
                    email.setText("");
                    usuario.setText("");
                    telefono.setText("");
                    confContra.setText("");
                    contrasena.setText("");
                    MensajeToast(response.trim());
                } else {
                    MensajeToast(response.trim());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MensajeToast("Intentelo mas tarde");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("tipo", Integer.toString(Util.USUARIO_ADMINISTRADOR));
                parametros.put("email", email.getText().toString());
                parametros.put("usuario", usuario.getText().toString());
                parametros.put("telefono", telefono.getText().toString());
                parametros.put("contrasena", contrasena.getText().toString());
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
    }

    public void MensajeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
