package com.example.comercios.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Global.GlobalAdmin;
import com.example.comercios.Global.GlobalSuperUsuario;
import com.example.comercios.Modelo.Administrador;
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
public class FragActInfoAdmin extends Fragment {

    StringRequest stringRequest;
    TextInputEditText email, usuario, telefono, contrasena, confContra;
    TextInputLayout lyEmail, lyUsuario, lyTelefono, lyContrasena, lyConfContra;

    public FragActInfoAdmin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_act_info_admin, container, false);
        mensajeAB("Actualizar administracion");
        GlobalAdmin.getInstance().setVentanaActual(R.layout.frag_act_info_admin);
        OnclickDelButton(view.findViewById(R.id.fragActInfoAdmin_btnReg));
        email = (TextInputEditText) view.findViewById(R.id.fragActInfoAdmin_edtEmail);
        usuario = (TextInputEditText) view.findViewById(R.id.fragActInfoAdmin_edtUser);
        telefono = (TextInputEditText) view.findViewById(R.id.fragActInfoAdmin_edtTelefono);
        contrasena = (TextInputEditText) view.findViewById(R.id.fragActInfoAdmin_edtPass);
        confContra = (TextInputEditText) view.findViewById(R.id.fragActInfoAdmin_edtConfPass);
        lyEmail = (TextInputLayout) view.findViewById(R.id.fragActInfoAdmin_widEmail);
        lyUsuario = (TextInputLayout) view.findViewById(R.id.fragActInfoAdmin_widUser);
        lyTelefono = (TextInputLayout) view.findViewById(R.id.fragActInfoAdmin_widTelefono);
        lyConfContra = (TextInputLayout) view.findViewById(R.id.fragActInfoAdmin_widConfPass);
        lyContrasena = (TextInputLayout) view.findViewById(R.id.fragActInfoAdmin_widPass);

        email.setText(GlobalAdmin.getInstance().getAdmin().getCorreo());
        usuario.setText(GlobalAdmin.getInstance().getAdmin().getUsuario());
        telefono.setText(Long.toString(GlobalAdmin.getInstance().getAdmin().getTelefono()));
        contrasena.setText(GlobalAdmin.getInstance().getAdmin().getContrasena());
        confContra.setText(GlobalAdmin.getInstance().getAdmin().getContrasena());

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarEmail();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        usuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarUsuario();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        telefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarTelefono();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        contrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarContrasena();
                validarConfContra();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        confContra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarConfContra();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    public void OnclickDelButton(View view) {
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fragActInfoAdmin_btnReg:
                        if (validarDatos()) {
                            actualizarAdministrador();
                        } else {
                            MensajeToast("Datos invalidos");
                        }
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton

    private void actualizarAdministrador() {
        String url = Util.urlWebService + "/actualizarInfoAdmin.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("Se actualizo correctamente")) {
                    lyConfContra.setError(null);
                    lyContrasena.setError(null);
                    lyEmail.setError(null);
                    lyTelefono.setError(null);
                    lyUsuario.setError(null);
                    MensajeToast(response.trim());
                } else {
                    email.setText(GlobalAdmin.getInstance().getAdmin().getCorreo());
                    usuario.setText(GlobalAdmin.getInstance().getAdmin().getUsuario());
                    telefono.setText(Long.toString(GlobalAdmin.getInstance().getAdmin().getTelefono()));
                    contrasena.setText(GlobalAdmin.getInstance().getAdmin().getContrasena());
                    confContra.setText(GlobalAdmin.getInstance().getAdmin().getContrasena());
                    MensajeToast(response.trim());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                email.setText(GlobalAdmin.getInstance().getAdmin().getCorreo());
                usuario.setText(GlobalAdmin.getInstance().getAdmin().getUsuario());
                telefono.setText(Long.toString(GlobalAdmin.getInstance().getAdmin().getTelefono()));
                contrasena.setText(GlobalAdmin.getInstance().getAdmin().getContrasena());
                confContra.setText(GlobalAdmin.getInstance().getAdmin().getContrasena());
                MensajeToast("Intentelo mas tarde");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", Integer.toString(GlobalAdmin.getInstance().getAdmin().getId()));
                parametros.put("correo", email.getText().toString());
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
        boolean valE = validarEmail();
        boolean valT = validarTelefono();
        boolean valU = validarUsuario();
        boolean valC = validarContrasena();
        boolean valCC = validarConfContra();

        if (valE && valT && valU && valC && valCC) {
            return true;
        }
        return false;
    }

    public boolean validarEmail() {
        String emai = email.getText().toString();
        if (emai.length() > 0 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(emai).find()) {
            lyEmail.setError(null);
            return true;
        }
        lyEmail.setError("Correo invalido");
        return false;
    }

    public boolean validarTelefono() {
        String tel = telefono.getText().toString();
        if (tel.length() > 0) {
            lyTelefono.setError(null);
            return true;
        }
        lyTelefono.setError("Telefono invalido");
        return false;
    }

    public boolean validarUsuario() {
        String usuari = usuario.getText().toString();
        if (usuari.length() > 0 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(usuari).find()) {
            lyUsuario.setError(null);
            return true;
        }
        lyUsuario.setError("Usuario invalida");
        return false;
    }

    public boolean validarContrasena() {
        String contra = contrasena.getText().toString();
        if (contra.length() > 0 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(contra).find()) {
            lyContrasena.setError(null);
            return true;
        }
        lyContrasena.setError("Contraseña invalida");
        return false;
    }

    public boolean validarConfContra() {
        String confContr = confContra.getText().toString();
        String contra = contrasena.getText().toString();
        if (contra.equals(confContr)) {
            lyConfContra.setError(null);
            return true;
        }
        lyConfContra.setError("Las contraseñas no coinciden");
        return false;
    }

    public void MensajeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};

}
