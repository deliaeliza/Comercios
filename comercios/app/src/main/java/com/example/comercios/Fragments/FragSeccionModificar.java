package com.example.comercios.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class FragSeccionModificar extends Fragment {

    private TextInputEditText nombre;
    private TextInputLayout tilNombre;
    public FragSeccionModificar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.frag_seccion_modificar, container, false);
        nombre = (TextInputEditText) view.findViewById(R.id.sec_modificar_TextInputEditText);
        tilNombre = (TextInputLayout) view.findViewById(R.id.sec_modificar_TextInputLayout);
        OnclickDelMaterialButtom(view.findViewById(R.id.sec_modificar_MaterialButtonAct));
        OnclickDelMaterialButtom(view.findViewById(R.id.sec_modificar_MaterialButtonEliminar));
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarDatos();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        return view;
    }

    public void OnclickDelMaterialButtom(View view) {

        MaterialButton miMaterialButtom = (MaterialButton)  view;
        miMaterialButtom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.sec_modificar_MaterialButtonAct:
                        if(validarDatos()){
                            enviarDatos();
                        }
                        break;

                    case R.id.sec_modificar_MaterialButtonEliminar:


                        break;
                    default:break; }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialButtom
    private boolean validarDatos(){
        String dato = nombre.getText().toString();
        if(dato.length() > 51)
            return false;

        if(dato.length() > 0 && dato.length() <= 50 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()){
            tilNombre.setError(null);
            return true;
        }
        tilNombre.setError("Nombre invalido");
        return false;
    }
    private void enviarDatos() {
        //String url = Util.urlWebService + "/seccionActualizar.php?id=" +
          //      GlobalComercios.getInstance().getIdSecModificar() + "&nombre=" + nombre.getText().toString();
        String url = Util.urlWebService + "/seccionActualizar.php?id=" +
               4  + "&nombre=" + nombre.getText().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonOb = response.getJSONObject("respuesta");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if(mensajeError.equalsIgnoreCase("")){
                        Mensaje("Exito: Nombre actualizado");
                    } else {
                        //JSONObject user = jsonOb.getJSONObject("usuario");
                        Mensaje(mensajeError);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Mensaje("No se puede conectar " + error.toString());
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }




    private void Mensaje(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};

}
