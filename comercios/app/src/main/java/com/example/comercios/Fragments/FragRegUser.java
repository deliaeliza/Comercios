package com.example.comercios.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.comercios.Modelo.Util;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
        OnTextChangedDelTextInputEditText(correo);
        OnTextChangedDelTextInputEditText(usuario);
        OnTextChangedDelTextInputEditText(pwd);
        OnTextChangedDelTextInputEditText(confPwd);
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
                        validarCorreo();
                        break;
                    case R.id.fRegUser_edtUser:
                        validarUsuario();
                        break;
                    case R.id.fRegUser_edtPass:
                        validarContrasena();
                        break;
                    case R.id.fRegUser_edtConfPass:
                        validarConfContrasena();
                        break;
                    default:
                        break;
                }
                validarDatos();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private boolean validarDatos() {
        return validarCorreo() && validarUsuario() && validarContrasena() && validarConfContrasena();
    }
    private boolean validarCorreo(){
        String dato = correo.getText().toString();
        if (dato.length() > 46)
            return false;
        if (dato.length() > 0 && dato.length() <= 45 && Patterns.EMAIL_ADDRESS.matcher(dato).find()) {
            tilCorreo.setError(null);
            return true;
        }
        tilCorreo.setError("Usuario invalido");
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

    private void registrarUsuario(){

    }


    private void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};
}
