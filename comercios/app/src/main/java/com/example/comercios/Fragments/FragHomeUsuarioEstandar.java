package com.example.comercios.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.Util;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragHomeUsuarioEstandar extends Fragment {


    public FragHomeUsuarioEstandar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_home_usuario_estandar, container, false);
        mensajeAB(Util.nombreApp);

        GlobalUsuarios.getInstance().setVentanaActual(R.layout.frag_home_usuario_estandar);
        TextView Mi_textview = (TextView) view.findViewById(R.id.fHomeUsuario_User);
        Mi_textview.setText(GlobalUsuarios.getInstance().getUserE().getUsuario());


        MaterialButton btnMap = (MaterialButton) view.findViewById(R.id.fHomeUsuario_btnMapa);
        MaterialButton btnComercios = (MaterialButton) view.findViewById(R.id.fHomeUsuario_btnTodosComercios);
        OnclickDelMaterialButton(btnMap);
        OnclickDelMaterialButton(btnComercios);
        return view;
    }
    public void OnclickDelMaterialButton(View view) {
        MaterialButton miButton = (MaterialButton) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fHomeUsuario_btnMapa:
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        FragEmpresasMaps mifrag = new FragEmpresasMaps();
                        fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag, "vermapa_usuarioEstandar");
                        fragmentTransaction.commit();

                        break;
                    case R.id.fHomeUsuario_btnTodosComercios:
                        fm = getFragmentManager();
                        fragmentTransaction = fm.beginTransaction();
                        FragVerComerciosLista mifrag2 = new FragVerComerciosLista();
                        fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag2, "listacomercios_usuarioEstandar");
                        fragmentTransaction.commit();
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};

}
