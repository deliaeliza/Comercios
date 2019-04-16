package com.example.comercios.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comercios.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragSeccionListarComercio extends Fragment {


    public FragSeccionListarComercio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_seccion_listar_comercio, container, false);
    }

}
