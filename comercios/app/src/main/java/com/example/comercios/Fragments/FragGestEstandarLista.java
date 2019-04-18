package com.example.comercios.Fragments;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.comercios.Modelo.UsuarioEstandar;
import com.example.comercios.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragGestEstandarLista extends Fragment {
    private final int TAM_PAGINA = 5;
    private View vistaInferior;
    private ListView listView;
    private EstandarListAdapter adapter;
    private Handler manejador;
    private boolean cargano = false;
    private List<UsuarioEstandar> usuarios;
    public FragGestEstandarLista() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.frag_gest_estandar_lista, container, false);
        listView = (ListView) view.findViewById(R.id.gest_estandar_listview);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vistaInferior = li.inflate(R.layout.vista_inferior_cargando, null);
        manejador = new MyHandler();
        usuarios = new ArrayList<UsuarioEstandar>();
        //Aqui agregamos cosas a la lista
        adapter = new EstandarListAdapter();
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Revisa si el scroll llego al ultimo item
                if(view.getLastVisiblePosition() == usuarios.size()-1 ){
                    cargano = true;
                    Thread thread = new ThreadMoreData();
                    thread.start();
                }
            }
        });
        return view;
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    //Se agrega la vista de cargar mientras se busca mas datos
                    listView.addFooterView(vistaInferior);
                    break;
                case 1:
                    //Se actualizan los datos del adaptador y de la interfaaz
                    adapter.agregarUsuarios((ArrayList<UsuarioEstandar>)msg.obj);
                    listView.removeFooterView(vistaInferior);
                    cargano = false;
                    break;
                default:
                    break;

            }
        }
    }

    private class ThreadMoreData extends Thread {
        @Override
        public  void run(){
            //Agrega la vista inferior
            manejador.sendEmptyMessage(0);
            //Se buscan mas datos
            ArrayList<UsuarioEstandar> nuevosDatos = obtenerMasDatos();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = manejador.obtainMessage(1, nuevosDatos);
            manejador.sendMessage(msg);

        }
    }

    private class EstandarListAdapter extends ArrayAdapter<UsuarioEstandar> {
        public EstandarListAdapter() {
            super(getActivity(), R.layout.item_gest_estandar, usuarios);
        }
        public void agregarUsuarios(List<UsuarioEstandar> usuariosNuevos){
            usuarios.addAll(usuariosNuevos);
            this.notifyDataSetChanged();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_gest_estandar, parent, false);
            }
            UsuarioEstandar actual = usuarios.get(position);
            // Fill the view
            TextView userTV = (TextView)itemView.findViewById(R.id.item_gest_estandar_user);
            userTV.setText(actual.getUsuario());
            TextView correoTV = (TextView) itemView.findViewById(R.id.item_gest_estandar_correo);
            correoTV.setText(actual.getCorreo());
            TextView edadTV = (TextView) itemView.findViewById(R.id.item_gest_estandar_edad);
            edadTV.setText(actual.getEdad() + " años");
            MaterialCardView panel = (MaterialCardView) itemView.findViewById(R.id.item_gest_estandar_panel);
            panel.setTag(position);
            OnclickDelMaterialCardView(panel);
            return itemView;
        }
    }


    public void OnclickDelMaterialCardView(View view) {
        MaterialCardView miMaterialCardView = (MaterialCardView) view;
        miMaterialCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                UsuarioEstandar escogido = usuarios.get((int)v.getTag());

            }// fin del onclick
        });
    }// fin de OnclickDelMaterialCardView

    public ArrayList<UsuarioEstandar> obtenerMasDatos() {
        ArrayList<UsuarioEstandar> datos = new ArrayList<UsuarioEstandar>();
        //Consultar a la base
        return datos;
    }
}
