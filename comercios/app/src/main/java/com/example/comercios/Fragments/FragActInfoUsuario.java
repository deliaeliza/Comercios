package com.example.comercios.Fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
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
import com.example.comercios.Modelo.UsuarioEstandar;
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
    UsuarioEstandar ue;

    EditText usuario, correo, password, confiPassword;
    String CUsuario, CContra,CCorreo;



    public FragActInfoUsuario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_act_info_usuario, container, false);
        correo = (EditText) view.findViewById(R.id.fActInfoUser_edtEmail);
        usuario = (EditText)view.findViewById(R.id.fActInfoUser_edtUser);
        password = (EditText) view.findViewById(R.id.fActInfoUser_edtPass);
        confiPassword = (EditText) view.findViewById(R.id.fActInfoUser_edtConfPass);
        cargarInfoUsuario();
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
                        if (!usuario.getText().toString().equalsIgnoreCase("") ||
                                !correo.getText().toString().equalsIgnoreCase("") ||
                                !password.getText().toString().equalsIgnoreCase("") ||
                                !confiPassword.getText().toString().equalsIgnoreCase("")) {
                            //si la deja vacia igualmente envio la anterior registrada, esto con todos los datos
                            //sino esta vacia compara con la confirmacion y actualizo
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

        /*String url = Util.urlWebService + "/actualizarInfoUsuario.php?usuario="+usuario.getText().toString()+
                "&correo="+correo.getText().toString()+"&contrasena="+password.getText().toString()+"&id="+"3";
*/
        String url = Util.urlWebService + "/actualizarInfoUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.trim().equalsIgnoreCase("correcto")) {
                    Mensaje("Actualización éxitosa");
                    dialogoRegresarMenuPrincial();

                } else {

                    Mensaje("Sucedio un error al intentar actualizar");

                }
            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                Mensaje("Intentelo mas tarde");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                //id hay que tomarlo del usuario logueado
                parametros.put("id","3");

                if(!usuario.getText().toString().equalsIgnoreCase("")){
                    parametros.put("usuario",usuario.getText().toString());
                }else{
                    parametros.put("usuario",CUsuario);
                }
                if(!correo.getText().toString().equalsIgnoreCase("")){
                    parametros.put("correo",correo.getText().toString());
                }else{
                    parametros.put("correo",CCorreo);
                }
                if(!password.getText().toString().equalsIgnoreCase("")){
                    parametros.put("contrasena",password.getText().toString());
                }else {
                    parametros.put("contrasena",CContra);
                }

                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);

    }

    public void Mensaje(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    };

    public void cargarInfoUsuario(){
        //id cambiarlos por el id del usuario logueado en la clase global
        //ue.getId();
        String url = Util.urlWebService + "/obtenerInfoEstandar.php?id="+3;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String r= String.valueOf(response.getJSONObject("estandar"));
                    if(r!="") {
                        JSONObject jsonEstandar = response.getJSONObject("estandar");
                        usuario.setText(jsonEstandar.getString("usuario"));
                        CUsuario=jsonEstandar.getString("usuario");
                        correo.setText(jsonEstandar.getString("correo"));
                        CCorreo= jsonEstandar.getString("correo");
                        CContra = jsonEstandar.getString("contrasena");
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
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void dialogoRegresarMenuPrincial() {
        final CharSequence[] opciones = {"si", "no"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones = new androidx.appcompat.app.AlertDialog.Builder(getActivity());//estamos en fragment
        alertOpciones.setTitle("¿Desea regresar al menu principal?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    /*FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    FragMenuPrincipalUser mifrag = new FragMenuPrincipalUser ();
                    fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag, "ID");
                    fragmentTransaction.commit();*/
                    dialogInterface.dismiss();

                } else {
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }
}
