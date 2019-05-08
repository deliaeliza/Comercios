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
public class Frag_ver_comercios_lista extends Fragment {


    public Frag_ver_comercios_lista() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_ver_comercios_lista, container, false);
    }

}
