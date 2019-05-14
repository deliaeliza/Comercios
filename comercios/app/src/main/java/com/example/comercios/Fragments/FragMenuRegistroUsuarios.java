package com.example.comercios.Fragments;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.comercios.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragMenuRegistroUsuarios extends Fragment {


    public FragMenuRegistroUsuarios() {
        // Required empty public constructor
    }
    BottomNavigationView menu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_menu_registro_usuarios, container, false);
        menu = (BottomNavigationView) view.findViewById(R.id.menu_nav_registrar);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return definirVista(item);
            }
        });

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragRegUser  mifrag = new FragRegUser ();
        fragmentTransaction.replace(R.id.menuSupRegUsCom_contenido, mifrag, "menuUsuarioAgregar");
        fragmentTransaction.commit();
        return view;
    }

    private boolean definirVista(MenuItem item){
        if(item.getItemId() == R.id.menuSupRegUsCom_usuario){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragRegUser  mifrag = new FragRegUser ();
            fragmentTransaction.replace(R.id.menuSupRegUsCom_contenido, mifrag, "menuUsuarioAgregar");
            fragmentTransaction.commit();
            return true;
        }else if (item.getItemId() == R.id.menuSupRegUsCom_comercio){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragRegEmpresa  mifrag = new FragRegEmpresa ();
            fragmentTransaction.replace(R.id.menuSupRegUsCom_contenido, mifrag, "menuComercioAgregar");
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

}
