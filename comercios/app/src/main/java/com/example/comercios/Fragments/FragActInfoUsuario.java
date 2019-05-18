package com.example.comercios.Fragments;



import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.UsuarioEstandar;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

    TextInputEditText usuario, correo, password, confiPassword;
    String CUsuario, CContra,CCorreo;
    TextInputLayout LayoutCorreo,LayoutUsuario,LayoutPsw,LayoutConfPsw;



    public FragActInfoUsuario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mensajeAB("Cuenta");
        GlobalUsuarios.getInstance().setVentanaActual(R.layout.frag_act_info_usuario);
        View view = inflater.inflate(R.layout.frag_act_info_usuario, container, false);
        correo = (TextInputEditText) view.findViewById(R.id.fActInfoUser_edtEmail);
        LayoutCorreo = (TextInputLayout) view.findViewById(R.id.fActInfoUser_widEmail);
        usuario = (TextInputEditText)view.findViewById(R.id.fActInfoUser_edtUser);
        LayoutUsuario = (TextInputLayout) view.findViewById(R.id.fActInfoUser_widUser);
        password = (TextInputEditText) view.findViewById(R.id.fActInfoUser_edtPass);
        LayoutPsw = (TextInputLayout) view.findViewById(R.id.fActInfoUser_widPass);
        confiPassword = (TextInputEditText) view.findViewById(R.id.fActInfoUser_edtConfPass);
        LayoutConfPsw = (TextInputLayout) view.findViewById(R.id.fActInfoUser_widConfPass);
        cargarInfoUsuario();
        OnclickDelButton(view.findViewById(R.id.fActInfoUser_btnAct));
        OnTextChangedDelTextInputEditText(correo);
        OnTextChangedDelTextInputEditText(usuario);
        OnTextChangedDelTextInputEditText(password);
        OnTextChangedDelTextInputEditText(confiPassword);

        return view;

    }
    public void OnclickDelButton(View view) {
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fActInfoUser_btnAct:
                        if(valdiarDatos()){
                            actualizarUsuario();
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
                    //dialogoRegresarMenuPrincial();

                } else {

                    Mensaje("Sucedio un error al intentar actualizar");

                }
            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                Mensaje("Inténtelo más tarde");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                //GlobalUsuarios.getInstance().getUserE().getId();
                //id hay que tomarlo del usuario logueado
                parametros.put("id",String.valueOf(GlobalUsuarios.getInstance().getUserE().getId()));

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
        //GlobalUsuarios.getInstance().getUserE().getId();
        String url = Util.urlWebService + "/obtenerInfoEstandar.php?id="+GlobalUsuarios.getInstance().getUserE().getId();
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

    /*private void dialogoRegresarMenuPrincial() {
        final CharSequence[] opciones = {"si", "no"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones = new androidx.appcompat.app.AlertDialog.Builder(getActivity());//estamos en fragment
        alertOpciones.setTitle("¿Desea regresar al menu principal?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    //FragmentManager fm = getFragmentManager();
                    //FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    //FragMenuPrincipalUser mifrag = new FragMenuPrincipalUser ();
                    //fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag, "ID");
                    //fragmentTransaction.commit();
                    //dialogInterface.dismiss();

                } else {
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }*/


    private void OnTextChangedDelTextInputEditText(final TextInputEditText textInputEditText){
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int id = textInputEditText.getId();
                switch (id){
                    case R.id.fActInfoUser_edtEmail:
                        validarCorreo();
                        break;
                    case R.id.fActInfoUser_edtUser:
                        validarUsuario();
                        break;
                    case R.id.fActInfoUser_edtPass:
                        LayoutPsw.setError(null);
                        validarConfContrasena();
                        break;
                    case R.id.fActInfoUser_edtConfPass:
                        validarConfContrasena();
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private boolean valdiarDatos(){
        boolean c = validarCorreo();
        boolean u = validarUsuario();
        boolean con = validarContrasena();
        boolean conf = validarConfContrasena();
        return c && u && con && conf;
    }
    private boolean validarCorreo(){
        String dato = correo.getText().toString();
        if (dato.length() > 0 && Patterns.EMAIL_ADDRESS.matcher(dato).find()) {
            LayoutCorreo.setError(null);
            return true;
        }
        LayoutCorreo.setError("Email invalido");
        return false;
    }
    private boolean validarUsuario(){
        String dato = usuario.getText().toString();
        if (dato.length() > 0 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()) {
            LayoutUsuario.setError(null);
            return true;
        }
        LayoutUsuario.setError("Usuario invalido");
        return false;
    }
    private boolean validarContrasena(){
        String dato = password.getText().toString();
        if ((dato.length() > 0 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()) || dato.length() == 0) {
            LayoutPsw.setError(null);
            return true;
        }
        LayoutPsw.setError("Contraseña invalida");
        return false;
    }
    private boolean validarConfContrasena(){
        String dato1 = password.getText().toString();
        String dato2 = confiPassword.getText().toString();
        if (dato1.equals(dato2)) {
            LayoutConfPsw.setError(null);
            return true;
        }
        LayoutConfPsw.setError("Las contraseñas no coinciden");
        return false;
    }
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};

}
