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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragSeccionResgistrar extends Fragment {

    EditText nombreSeccion;

    public FragSeccionResgistrar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.frag_seccion_resgistrar, container, false);
        OnclickDelButton(view.findViewById(R.id.fRegSec_btnReg));
        return view; // debe comentar el otro return
    }

    public void OnclickDelButton(final View view) {
        Button miButton = (Button)  view;
        miButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fRegSec_btnReg:
                        nombreSeccion = (EditText) view.findViewById(R.id.fRegSec_edtNombre);
                        if(!nombreSeccion.getText().toString().equalsIgnoreCase("")){
                            //registrarSeccion();
                        }else{
                            MensajeToast("Complete los datos");
                        }
                        break;
                    default:break; }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton
/*
    private void registrarSeccion() {
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
*/
    public void MensajeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
