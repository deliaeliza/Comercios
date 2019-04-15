package com.example.listviewprueba;

import android.content.Context;
import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MenuImages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_images);
        LlenarListaObjetos();
        MyAdapter adapter = new MyAdapter(misObjetos);
        RecyclerView list = (RecyclerView) findViewById(R.id.recyclerMenuImages);
        list.setAdapter(adapter);
        //list.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<ObjetosxDesplegarImageMenu> misObjetos = new ArrayList<ObjetosxDesplegarImageMenu>();
    private void LlenarListaObjetos() {
        misObjetos.add(new ObjetosxDesplegarImageMenu("Restaurante", R.string.fa_icon_utensils, 1));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Hotel", R.string.fa_icon_hotel,2));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Cafeteria", R.string.fa_icon_coffee, 3));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Bar", R.string.fa_icon_glassMartiniAlt, 4));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Tecnologia", R.string.fa_icon_laptop, 5));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Supermercado", R.string.fa_icon_shoppingCart, 6));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Ropa", R.string.fa_icon_tshirt,7));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Deportivo", R.string.fa_icon_bicycle, 8));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Farmacia", R.string.fa_icon_capsules, 9));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Biblioteca", R.string.fa_icon_book, 10));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Zapateria", R.string.fa_icon_shoePrints, 11));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Ferreteria", R.string.fa_icon_hammer, 12));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Musica", R.string.fa_icon_guitar, 13));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Videojuegos", R.string.fa_icon_gamepad, 14));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Jugueteria", R.string.fa_icon_robot, 15));
        misObjetos.add(new ObjetosxDesplegarImageMenu("Otro", R.string.fa_icon_storeAlt, 16));
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView nombre;
            public TextView imagen;
            public ConstraintLayout panel;
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);
                panel = (ConstraintLayout) itemView.findViewById(R.id.imageMenu_item);
                nombre = (TextView) itemView.findViewById(R.id.imageMenu_txtNombre);
                imagen = (TextView) itemView.findViewById(R.id.imageMenu_txtImage);
            }
        }

        private List<ObjetosxDesplegarImageMenu> listaObjetos;

        // Pass in the contact array into the constructor
        public MyAdapter(List<ObjetosxDesplegarImageMenu> objetos) {
            listaObjetos = objetos;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.item_menu_image, parent, false);
            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            final ObjetosxDesplegarImageMenu objetoActual = listaObjetos.get(position);

            // Set item views based on your views and data model
            TextView image = viewHolder.imagen;
            image.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME_SOLID));
            image.setText(objetoActual.getNumDibujo());
            TextView name = viewHolder.nombre;
            name.setText(objetoActual.getCategoria());
            ConstraintLayout contenedor = viewHolder.panel;
            contenedor.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Mensaje(objetoActual.getCategoria());
                    ConstraintLayout panel = (ConstraintLayout) v.findViewById(R.id.imageMenu_item);
                    TextView nombre = (TextView) v.findViewById(R.id.imageMenu_txtNombre);
                    TextView imagen = (TextView) v.findViewById(R.id.imageMenu_txtImage);
                    nombre.setTextColor(Color.GREEN);
                    imagen.setTextColor(Color.GREEN);
                }
            });
        }

        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            return listaObjetos.size();
        }
    }
    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
