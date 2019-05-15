package com.example.comercios.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragVerComercioInfo extends Fragment {

    private TextView cantVotos;
    private RatingBar ratingBar;
    private MaterialButton calificar;
    private MaterialButton correoEnv;
    private MaterialButton telefonoLlamar;
    public FragVerComercioInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.frag_ver_comercio_info, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.frag_ver_comercio_info_imageview);
        TextView nombre = (TextView) view.findViewById(R.id.frag_ver_comercio_info_nombre);
        TextView correo = (TextView) view.findViewById(R.id.frag_ver_comercio_info_correo);
        TextView telefono = (TextView) view.findViewById(R.id.frag_ver_comercio_info_telefono);
        TextView ubicacion = (TextView) view.findViewById(R.id.frag_ver_comercio_info_ubicacion);
        TextView descripcion = (TextView) view.findViewById(R.id.frag_ver_comercio_info_descrip);
        cantVotos = (TextView) view.findViewById(R.id.frag_ver_comercio_info_cantVotos);
        ratingBar = (RatingBar) view.findViewById(R.id.frag_ver_comercio_info_ratingBar);
        calificar = (MaterialButton) view.findViewById(R.id.frag_ver_comercio_info_calificar_btn);
        correoEnv = (MaterialButton) view.findViewById(R.id.frag_ver_comercio_info_correo_btn);
        telefonoLlamar = (MaterialButton) view.findViewById(R.id.frag_ver_comercio_info_telefono_btn);

        imageView.setImageBitmap(GlobalUsuarios.getInstance().getComercio().getImagen());
        nombre.setText(GlobalUsuarios.getInstance().getComercio().getUsuario());
        descripcion.setText(GlobalUsuarios.getInstance().getComercio().getDescripcion());
        ubicacion.setText(GlobalUsuarios.getInstance().getComercio().getUbicacion());
        correo.setText(GlobalUsuarios.getInstance().getComercio().getCorreo());
        telefono.setText(GlobalUsuarios.getInstance().getComercio().getTelefono() + "");
        cantVotos.setText(GlobalUsuarios.getInstance().getComercio().getCantCalificaciones());
        ratingBar.setRating(GlobalUsuarios.getInstance().getComercio().getCalificacion());

        return view;
    }

}
