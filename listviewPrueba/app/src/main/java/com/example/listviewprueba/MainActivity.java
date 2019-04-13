package com.example.listviewprueba;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //panel_iconos
        //Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME_SOLID);
        //FontManager.markAsIconContainer(findViewById(R.id.panel_iconos), iconFont);
        LlenarListaObjetos();
        LlenarListView();
        RegistrarClicks();
    }

    private List<ObjetosxDesplegar> misObjetos = new ArrayList<ObjetosxDesplegar>();
    private void LlenarListaObjetos() {
        misObjetos.add(new ObjetosxDesplegar("INTELEC", R.drawable.intelec, 1));
        misObjetos.add(new ObjetosxDesplegar("EXTREMETECH", R.drawable.extreme, 2));
        misObjetos.add(new ObjetosxDesplegar("BARULU", R.drawable.barulu, 3));
        misObjetos.add(new ObjetosxDesplegar("GOLLO", R.drawable.gollo, 4));
        misObjetos.add(new ObjetosxDesplegar("ARTELEC", R.drawable.artelec, 5));
        misObjetos.add(new ObjetosxDesplegar("CASA BLANCA", R.drawable.casablanca, 6));
        misObjetos.add(new ObjetosxDesplegar("CURACAO", R.drawable.curacao, 7));
        misObjetos.add(new ObjetosxDesplegar("IMPORTADORA MONGE", R.drawable.monge, 8));
        misObjetos.add(new ObjetosxDesplegar("TOYS", R.drawable.toys, 9));
        misObjetos.add(new ObjetosxDesplegar("LA PARRILLITA DE PEPE PEPE PEPE PEPE", R.drawable.pepe, 10));
    }
    private void LlenarListView() {
        ArrayAdapter<ObjetosxDesplegar> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }
    private void RegistrarClicks() {
        ListView list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {
                ObjetosxDesplegar ObjEscogido = misObjetos.get(position);
                String message = "Elegiste item No.  " + (1+position)
                        + " que es un objeto cuyo nombre  " + ObjEscogido.getNombre();
                Mensaje(message);
            }
        });
    }
    private class MyListAdapter extends ArrayAdapter<ObjetosxDesplegar> {
        public MyListAdapter() {
            super(MainActivity.this, R.layout.itemlist, misObjetos);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.itemlist, parent, false);
            }
            ObjetosxDesplegar ObjetoActual = misObjetos.get(position);
            // Fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.logo);
            imageView.setImageResource(ObjetoActual.getNumDibujo());
            TextView nombre = (TextView) itemView.findViewById(R.id.txtNombre);
            nombre.setText(ObjetoActual.getNombre());
            View vEditar = itemView.findViewById(R.id.txtEditar);
            View vEliminar = itemView.findViewById(R.id.txtEliminar);
            TextView editar = (TextView) vEditar;
            TextView eliminar = (TextView) vEliminar;
            editar.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME_SOLID));
            eliminar.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME_SOLID));
            editar.setText(R.string.fa_icon_edit);
            eliminar.setText(R.string.fa_icon_trash);
            editar.setTag(ObjetoActual.getId() +" / "+ ObjetoActual.getNombre());
            eliminar.setTag(ObjetoActual.getId() +" / "+ ObjetoActual.getNombre());
            OnclickDelTextView(vEditar);
            OnclickDelTextView(vEliminar);
            //vEditar.setTag(ObjetoActual.getId() + ObjetoActual.getNombre());
            //vEliminar.setTag(ObjetoActual.getId() + ObjetoActual.getNombre());
            //vEditar.setOnClickListener(MainActivity.get);
            return itemView;
        }
    }
    public void OnclickDelTextView(View view) {

        // Ejemplo  OnclickDelTextView(R.id.MiTextView);
        // 1 Doy referencia al TextView
        TextView miTextView = (TextView) view;
        //  final String msg = miTextView.getText().toString();
        // 2.  Programar el evento onclick
        miTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // if(msg.equals("Texto")){Mensaje("Texto en el botón ");};
                switch (v.getId()) {
                    case R.id.txtEditar:
                        Mensaje("Modificar: " + v.getTag().toString());
                        break;
                    case R.id.txtEliminar:
                        Mensaje("Eliminar: " + v.getTag().toString());
                        break;
                    default:break; }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelTextView


    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
