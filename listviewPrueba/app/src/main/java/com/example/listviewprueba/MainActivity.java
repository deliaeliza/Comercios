package com.example.listviewprueba;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

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

            View vPanel = itemView.findViewById(R.id.itemlist_panel);
            View vEditar = itemView.findViewById(R.id.txtEditar);
            View vEliminar = itemView.findViewById(R.id.txtEliminar);

            MaterialCardView panel = (MaterialCardView) vPanel;
            TextView editar = (TextView) vEditar;
            TextView eliminar = (TextView) vEliminar;

            editar.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME_SOLID));
            eliminar.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME_SOLID));
            editar.setText(R.string.fa_icon_edit);
            eliminar.setText(R.string.fa_icon_trash);

            editar.setTag(ObjetoActual.getId() +" / "+ ObjetoActual.getNombre());
            eliminar.setTag(ObjetoActual.getId() +" / "+ ObjetoActual.getNombre());
            panel.setTag(position);

            OnclickDelTextView(vEditar);
            OnclickDelTextView(vEliminar);
            OnclickDelMaterialCardView(panel);
            return itemView;
        }
    }
    public void OnclickDelTextView(View view) {
        TextView miTextView = (TextView) view;
        miTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.txtEditar:
                        Mensaje("Modificar: " + v.getTag().toString());
                        break;
                    case R.id.txtEliminar:
                        Mensaje("Eliminar: " + v.getTag().toString());
                        break;
                    default:break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelTextView

    public void OnclickDelMaterialCardView(final View view) {
        MaterialCardView miMaterialCardView = (MaterialCardView) view;
        miMaterialCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ObjetosxDesplegar ObjEscogido = misObjetos.get((int)v.getTag());
                String message = "Elegiste item No.  " + (1+(int)v.getTag())
                        + " que es un objeto cuyo nombre  " + ObjEscogido.getNombre();
                Mensaje(message);
            }// fin del onclick
        });
    }// fin de OnclickDelMaterialCardView
    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
