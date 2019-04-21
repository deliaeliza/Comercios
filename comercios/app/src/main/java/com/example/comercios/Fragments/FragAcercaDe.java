package com.example.comercios.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;

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
        return inflater.inflate(R.layout.frag_acerca_de, container, false);
    }

}
