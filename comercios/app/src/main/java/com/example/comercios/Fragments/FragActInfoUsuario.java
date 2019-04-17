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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Modelo.Util;
import com.example.comercios.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragActInfoUsuario extends Fragment {
    JsonObjectRequest jsonObjectRequest;
    private EditText usuario;
    private EditText correo;
    private EditText password;
    private EditText confiPassword;

    public FragActInfoUsuario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final View view =inflater.inflate(R.layout.frag_act_info_usuario, container, false);
        Button MiButton = (Button) view.findViewById(R.id.fActInfoUser_btnAct);
        MiButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                usuario = (EditText) view.findViewById(R.id.fActInfoUser_edtUser);
                correo = (EditText) view.findViewById(R.id.fActInfoUser_edtEmail);
                password = (EditText) view.findViewById(R.id.fActInfoUser_edtPass);
                String url = Util.urlWebService + "/actualizarInfoUsuario.php?usuario=" +usuario.getText().toString()+
                        "&correo="+correo.getText().toString() + "&contrasena=" + password.getText().toString()+
                        "&id="+"3";

                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonOb = response.getJSONObject("datos");
                            String mensajeError = jsonOb.getString("mensajeError");
                            Mensaje(jsonOb.toString());
                            if(mensajeError.equalsIgnoreCase("")){
                                String resp = jsonOb.getString("respuesta");
                                Mensaje(resp);

                            } else {
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

            }
        });
        return view;
    }

    public void Mensaje(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    };

}
