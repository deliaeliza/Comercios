package com.example.comercios.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.comercios.Filtros.FiltrosSeccion;
import com.example.comercios.Global.GlobalAdmin;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Global.GlobalSuperUsuario;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragAcercaDe extends Fragment {


    public FragAcercaDe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mensajeAB("Acerca de");
        GlobalUsuarios.getInstance().setVentanaActual(R.layout.frag_acerca_de);
        GlobalAdmin.getInstance().setVentanaActual(R.layout.frag_acerca_de);
        GlobalComercios.getInstance().setVentanaActual(R.layout.frag_acerca_de);
        GlobalSuperUsuario.getInstance().setVentanaActual(R.layout.frag_acerca_de);
        View v = inflater.inflate(R.layout.frag_acerca_de, container, false);
        OnclickDelMaterialButton(v.findViewById(R.id.frag_btnVideo));

        return v;
    }

    public void OnclickDelMaterialButton(View view) {
        MaterialButton miMaterialButton = (MaterialButton)  view;
        miMaterialButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.frag_btnVideo:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=5P9D0LWiGfo&feature=youtu.be")));
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialButton

    private void mensajeAB(String msg){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);
    };
}
