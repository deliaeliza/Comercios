package com.example.comercios.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.comercios.Modelo.Util;
import com.example.comercios.R;
import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragHomeSuperUsuario extends Fragment {


    public FragHomeSuperUsuario() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mensajeAB(Util.nombreApp);
        View view = inflater.inflate(R.layout.frag_home_super_usuario, container, false);


        MaterialCardView cardViewMapa = (MaterialCardView)view.findViewById(R.id.fHomeAdmin_addAdm);
        cardViewMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                FragRegAdmin mifrag = new FragRegAdmin ();
                fragmentTransaction.replace(R.id.superUsuario_contenedor, mifrag, "agreagarAdminSU");
                fragmentTransaction.commit();
            }
        });
        MaterialCardView cardViewComercios = (MaterialCardView)view.findViewById(R.id.fHomeAdmin_GestCom);
        cardViewComercios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                FragGestAdminLista mifrag = new FragGestAdminLista();
                fragmentTransaction.replace(R.id.superUsuario_contenedor, mifrag, "gestAdmins");
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};
}
