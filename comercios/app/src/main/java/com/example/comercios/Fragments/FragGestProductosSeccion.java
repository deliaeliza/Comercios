package com.example.comercios.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.comercios.Modelo.Comercio;
import com.example.comercios.R;

import java.util.List;


public class FragGestProductosSeccion extends Fragment {

    private final int TAM_PAGINA = 10;
    private boolean inicial = true;
    private boolean cargando = false;
    private boolean userScrolled = false;
    private View vistaInferior;
    private ListView listView;
    private Handler manejador;
    private List<Comercio> comercios;
    private int posicion = -1;
    public FragGestProductosSeccion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.frag_gest_productos_seccion, container, false);
        return view;
    }

    public void mensajeToast(String msg){ Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
