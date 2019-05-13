package com.example.comercios.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.Util;
import com.example.comercios.R;

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
        mensajeAB(Util.nombreApp);
        GlobalUsuarios.getInstance().setVentanaActual(R.layout.frag_home_usuario_estandar);
        return inflater.inflate(R.layout.frag_home_usuario_estandar, container, false);
    }
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};

}
