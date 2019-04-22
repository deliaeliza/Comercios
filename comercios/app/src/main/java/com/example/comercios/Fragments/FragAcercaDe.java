package com.example.comercios.Fragments;


import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.R;


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

        return inflater.inflate(R.layout.frag_acerca_de, container, false);
    }
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};
}
