package com.example.comercios.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comercios.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragHomeAdmin extends Fragment {


    public FragHomeAdmin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mensajeAB("Shop World");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_home_admin, container, false);
    }
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};
}
