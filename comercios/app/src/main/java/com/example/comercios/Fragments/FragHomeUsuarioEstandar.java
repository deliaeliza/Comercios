package com.example.comercios.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.Util;
import com.example.comercios.R;
import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragHomeUsuarioEstandar extends Fragment {


    public FragHomeUsuarioEstandar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_home_usuario_estandar, container, false);
        mensajeAB(Util.nombreApp);

        GlobalUsuarios.getInstance().setVentanaActual(R.layout.frag_home_usuario_estandar);
        //TextView Mi_textview = (TextView) view.findViewById(R.id.fHomeUsuario_User);
        //Mi_textview.setText(GlobalUsuarios.getInstance().getUserE().getUsuario());

        MaterialCardView cardViewMapa = (MaterialCardView)view.findViewById(R.id.fHomeAdmin_GestCom);
        cardViewMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                FragEmpresasMaps mifrag = new FragEmpresasMaps();
                fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag, "vermapa_usuarioEstandar");
                fragmentTransaction.commit();
            }
        });
        MaterialCardView cardViewComercios = (MaterialCardView)view.findViewById(R.id.fHomeUsuario_cardComercios);
        cardViewComercios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                FragVerComerciosLista mifrag2 = new FragVerComerciosLista();
                fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag2, "listacomercios_usuarioEstandar");
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};

    public void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};

}
