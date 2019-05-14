package com.example.comercios.Fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragSeccionResgistrar extends Fragment {

    private TextInputEditText nombreSeccion;
    private TextInputLayout tilNombre;
    StringRequest stringRequest;

    public FragSeccionResgistrar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.frag_seccion_resgistrar, container, false);
        mensajeAB("Registrar Sección");
        //GlobalComercios.getInstance().setVentanaActual(R.layout.frag_seccion_resgistrar);
        OnclickDelButton(view.findViewById(R.id.fRegSec_btnReg));
        nombreSeccion = (TextInputEditText) view.findViewById(R.id.fRegSec_edtNombre);
        tilNombre = (TextInputLayout) view.findViewById(R.id.fRegSec_txtNombre);
        nombreSeccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarDatos();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        return view; // debe comentar el otro return
    }

    public void OnclickDelButton(final View view) {
        Button miButton = (Button)  view;
        miButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fRegSec_btnReg:
                        if(validarDatos()){
                            registrarSeccion();
                        }
                        break;
                    default:break; }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton

    private void registrarSeccion() {
        String url = Util.urlWebService + "/seccionesRegistrar.php?";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("Se registro correctamente")) {
                    nombreSeccion.setText("");
                    tilNombre.setError(null);
                    mensajeToast(response.trim());
                } else {
                    mensajeToast(response.trim());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Intentelo mas tarde" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idComercio", Integer.toString(GlobalComercios.getInstance().getComercio().getId()));
                parametros.put("nombreSeccion", nombreSeccion.getText().toString());
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
    }

    public void mensajeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};

    private boolean validarDatos(){
        String dato = nombreSeccion.getText().toString();
        if(dato.length() > 51)
            return false;
        if(dato.length() > 0 && dato.length() <= 50 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()){
            tilNombre.setError(null);
            return true;
        }
        tilNombre.setError("Nombre invalido");
        return false;
    }

}
