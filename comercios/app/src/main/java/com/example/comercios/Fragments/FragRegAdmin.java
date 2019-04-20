
package com.example.comercios.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class FragRegAdmin extends Fragment {

    StringRequest stringRequest;
    TextInputEditText email, usuario, telefono, contrasena, confContra;
    TextInputLayout lyEmail, lyUsuario, lyTelefono, lyContrasena, lyConfContra;

    public FragRegAdmin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_reg_admin, container, false);
        OnclickDelButton(view.findViewById(R.id.fRegAdmin_btnReg));
        email = (TextInputEditText) getView().findViewById(R.id.fRegAdmin_edtEmail);
        usuario = (TextInputEditText) getView().findViewById(R.id.fRegAdmin_edtUser);
        telefono = (TextInputEditText) getView().findViewById(R.id.fRegAdmin_edtTelefono);
        contrasena = (TextInputEditText) getView().findViewById(R.id.fRegAdmin_edtPass);
        confContra = (TextInputEditText) getView().findViewById(R.id.fRegAdmin_edtConfPass);
        lyEmail = (TextInputLayout) getView().findViewById(R.id.fRegAdmin_widEmail);
        lyUsuario = (TextInputLayout) getView().findViewById(R.id.fRegAdmin_widUser);
        lyTelefono = (TextInputLayout) getView().findViewById(R.id.fRegAdmin_widTelefono);
        lyConfContra = (TextInputLayout) getView().findViewById(R.id.fRegAdmin_widConfPass);
        lyContrasena = (TextInputLayout) getView().findViewById(R.id.fRegAdmin_widPass);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarEmail();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        usuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarUsuario();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        telefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarTelefono();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        contrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarContrasena();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        confContra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarConfContra();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        return view;
    }

    public void OnclickDelButton(View view) {
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fRegAdmin_btnReg:
                        if (validarDatos()) {
                                registrarAdministrador();
                        }else{
                            MensajeToast("Datos invalidos");
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

    public boolean validarDatos() {
        String emai = email.getText().toString();
        String tel = telefono.getText().toString();
        String usuari = usuario.getText().toString();
        String contra = contrasena.getText().toString();
        String confCon = confContra.getText().toString();

        if (email.length() > 45 && tel.length() > 13 && usuari.length() > 45 && contra.length() > 45 && confCon.length() > 45)
            return false;
        if (validarEmail() && validarTelefono() && validarUsuario() && validarContrasena() && validarConfContra()) {
            return true;
        }
        return false;
    }

    public boolean validarEmail() {
        String emai = email.getText().toString();
        if (emai.length() > 0 && emai.length() <= 45 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(emai).find()) {
            lyEmail.setError(null);
            return true;
        } else {
            lyEmail.setError("Email invalido");
        }
        return false;
    }

    public boolean validarTelefono() {
        String tel = telefono.getText().toString();
        if (tel.length() <= 13) {
            lyTelefono.setError(null);
            return true;
        } else {
            lyTelefono.setError("Telefono invalido");
        }
        return false;
    }

    public boolean validarUsuario() {
        String usuari = usuario.getText().toString();
        if (usuari.length() > 0 && usuari.length() <= 45 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(usuari).find()) {
            lyUsuario.setError(null);
            return true;
        } else {
            lyUsuario.setError("Usuario invalida");
        }
        return false;
    }

    public boolean validarContrasena() {
        String contra = contrasena.getText().toString();
        if (contra.length() > 0 && contra.length() <= 45 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(contra).find()) {
            lyContrasena.setError(null);
            return true;
        } else {
            lyContrasena.setError("Descripcion invalida");
        }
        return false;
    }

    public boolean validarConfContra() {
        String confContr = confContra.getText().toString();
        String contra = contrasena.getText().toString();
        if (contra.equals(confContr)) {
            lyConfContra.setError(null);
            return true;
        } else {
            lyConfContra.setError("Las contraseñas no coinciden");
        }
        return false;
    }



    public void MensajeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
