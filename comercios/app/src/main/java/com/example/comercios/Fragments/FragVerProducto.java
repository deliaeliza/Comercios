package com.example.comercios.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comercios.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragVerProducto extends Fragment {


    public FragVerProducto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.frag_ver_producto, container, false);

        return view;
    }

}
