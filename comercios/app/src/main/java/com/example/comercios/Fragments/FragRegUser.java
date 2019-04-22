package com.example.comercios.Fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Login;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragRegUser extends Fragment {
    private TextInputEditText correo;
    private TextInputLayout tilCorreo;
    private TextInputEditText usuario;
    private TextInputLayout tilUsuario;
    private TextInputEditText pwd;
    private TextInputLayout tilPwd;
    private TextInputEditText confPwd;
    private TextInputLayout tilConfPwd;
    private TextInputEditText fecha;
    private  TextInputLayout tilFecha;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    public FragRegUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_reg_user, container, false);
        correo = (TextInputEditText) view.findViewById(R.id.fRegUser_edtEmail);
        tilCorreo = (TextInputLayout) view.findViewById(R.id.fRegUser_widEmail);
        usuario = (TextInputEditText) view.findViewById(R.id.fRegUser_edtUser);
        tilUsuario = (TextInputLayout) view.findViewById(R.id.fRegUser_widUser);
        pwd = (TextInputEditText) view.findViewById(R.id.fRegUser_edtPass);
        tilPwd = (TextInputLayout) view.findViewById(R.id.fRegUser_widPass);
        confPwd = (TextInputEditText) view.findViewById(R.id.fRegUser_edtConfPass);
        tilConfPwd = (TextInputLayout) view.findViewById(R.id.fRegUser_widConfPass);
        fecha = (TextInputEditText) view.findViewById(R.id.fRegUser_edtNac);
        tilFecha = (TextInputLayout) view.findViewById(R.id.fRegUser_widNac);
        fecha.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        calendar = Calendar.getInstance();
                        int dia = calendar.get(Calendar.DAY_OF_MONTH);
                        int mes = calendar.get(Calendar.MONTH);
                        int anio = calendar.get(Calendar.YEAR);
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                fecha.setText(dayOfMonth + "/" + (month+1) + "/" + year);


                            }
                        }, dia, mes, anio);
                        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                        datePickerDialog.show();
                        tilFecha.clearFocus();
                        fecha.clearFocus();
                        pwd.requestFocus();
                    }
                }
        });
        OnTextChangedDelTextInputEditText(correo);
        OnTextChangedDelTextInputEditText(usuario);
        OnTextChangedDelTextInputEditText(pwd);
        OnTextChangedDelTextInputEditText(confPwd);
        OnTextChangedDelTextInputEditText(fecha);
        OnclickDelMaterialButtom(view.findViewById(R.id.fRegUser_btnReg));
        return view;
    }

    private void OnclickDelMaterialButtom(View view) {
        MaterialButton miMaterialButtom = (MaterialButton) view;
        miMaterialButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.fRegUser_btnReg && validarDatos()){
                    registrarUsuario();
                }
            }// fin del onclick
        });
    }
    private void OnTextChangedDelTextInputEditText(final TextInputEditText textInputEditText){
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int id = textInputEditText.getId();
                switch (id){
                    case R.id.fRegUser_edtEmail:
                        tilCorreo.setError(null);
                        break;
                    case R.id.fRegUser_edtUser:
                        validarUsuario();
                        break;
                    case R.id.fRegUser_edtPass:
                        validarContrasena();
                        validarConfContrasena();
                        break;
                    case R.id.fRegUser_edtConfPass:
                        validarConfContrasena();
                        break;
                    case R.id.fRegUser_edtNac:
                        validarFecha();
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private boolean validarDatos() { //Variable para que se llamen todos lo metodos aunque todos sean false
        boolean email = validarCorreo() && validarUsuario() && validarContrasena() && validarConfContrasena();
        boolean user = validarUsuario();
        boolean fec = validarFecha();
        boolean pass = validarContrasena();
        boolean confPass = validarConfContrasena();
        return email && user && fec && pass && confPass;
    }
    private boolean validarCorreo(){
        String dato = correo.getText().toString();
        if (dato.length() > 46)
            return false;
        if (dato.length() > 0 && dato.length() <= 45 && Patterns.EMAIL_ADDRESS.matcher(dato).find()) {
            tilCorreo.setError(null);
            return true;
        }
        tilCorreo.setError("Email invalido");
        return false;
    }
    private boolean validarUsuario(){
        String dato = usuario.getText().toString();
        if (dato.length() > 46)
            return false;
        if (dato.length() > 0 && dato.length() <= 45 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()) {
            tilUsuario.setError(null);
            return true;
        }
        tilUsuario.setError("Usuario invalido");
        return false;
    }
    private boolean validarContrasena(){
        String dato = pwd.getText().toString();
        if (dato.length() > 46)
            return false;
        if (dato.length() > 0 && dato.length() <= 45 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()) {
            tilPwd.setError(null);
            return true;
        }
        tilPwd.setError("Contraseña invalida");
        return false;
    }
    private boolean validarConfContrasena(){
        String dato1 = pwd.getText().toString();
        String dato2 = confPwd.getText().toString();
        if (dato1.equals(dato2)) {
            tilConfPwd.setError(null);
            return true;
        }
        tilConfPwd.setError("Las contraseñas no coinciden");
        return false;
    }
    private boolean validarFecha(){
        String dato = fecha.getText().toString();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        try {
            formatoFecha.parse(dato);
            tilFecha.setError(null);
            return true;
        } catch (ParseException ex){
            tilFecha.setError("Fecha invalida");
        }
        return false;
    }

    private void registrarUsuario(){
        String url = Util.urlWebService + "/usuarioEstandarRegistrar.php?";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("")) {
                    mensajeToast("Se registro correctamente");
                    Intent intento = new Intent(getActivity().getApplicationContext(), Login.class);
                    startActivity(intento);
                } else {
                    mensajeToast(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Intentelo mas tarde");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("tipo", Integer.toString(Util.USUARIO_ESTANDAR));
                parametros.put("correo", correo.getText().toString());
                parametros.put("usuario", usuario.getText().toString());
                parametros.put("fechaNac", fecha.getText().toString());
                parametros.put("contrasena", pwd.getText().toString());
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
    }


    private void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};
}
