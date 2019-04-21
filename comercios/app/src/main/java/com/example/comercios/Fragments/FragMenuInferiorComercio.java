package com.example.comercios.Fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;

public class FragMenuInferiorComercio extends Fragment {


    public FragMenuInferiorComercio() {
        // Required empty public constructor
    }
    BottomNavigationView menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.frag_menu_inferior_comercio, container, false);
        menu = (BottomNavigationView) view.findViewById(R.id.comercio_menu_inferior);

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return definirVista(item);
            }
        });
        return view;
    }

    private boolean definirVista(MenuItem item){
        int opcionActual = GlobalComercios.getInstance().getOpcActual();
        switch (opcionActual){
            case R.string.catalogo_lbl:
                if(item.getItemId() == R.id.menuIferiorComercio_agregar){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    FragSeccionResgistrar  mifrag = new FragSeccionResgistrar ();
                    fragmentTransaction.replace(R.id.menuInferiorComercios_contenido, mifrag, "IdMenuIferior");
                    fragmentTransaction.commit();
                    return true;
                } else if (item.getItemId() == R.id.menuIferiorComercio_modificar) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    FragSeccionListarComercio  mifrag = new FragSeccionListarComercio ();
                    fragmentTransaction.replace(R.id.menuInferiorComercios_contenido, mifrag, "IdMenuInferior");
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            case R.string.productos_lbl:
                if(item.getItemId() == R.id.menuIferiorComercio_agregar){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    FragProductoResgistrar  mifrag = new FragProductoResgistrar ();
                    fragmentTransaction.replace(R.id.menuInferiorComercios_contenido, mifrag, "IdMenuIferior");
                    fragmentTransaction.commit();
                    return true;
                } else if (item.getItemId() == R.id.menuIferiorComercio_modificar) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    //FragProductoListarComercio  mifrag = new FragProductoListarComercio ();
                    fragActInfoProductos mifrag = new fragActInfoProductos();
                    fragmentTransaction.replace(R.id.menuInferiorComercios_contenido, mifrag, "IdMenuInferior");
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}
