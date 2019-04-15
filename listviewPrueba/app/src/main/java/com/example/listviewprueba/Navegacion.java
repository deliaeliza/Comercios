package com.example.listviewprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Navegacion extends AppCompatActivity {
    private TabLayout tabLayout;

    private GridView gridView;
    private AdaptadorDeProductos adaptador;

    ArrayList<Categoria> categorias = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);
        tabLayout = (TabLayout) findViewById(R.id.navegacion_tab);
        crearCategorias();
        for(Categoria c: categorias){
            TabLayout.Tab t = tabLayout.newTab();
            t.setText(c.getNombre());
            t.setTag(c.getId());
            //t.setIcon(c.getFont()); //Se le debe pasar el icono
            tabLayout.addTab(t);
        }
        tabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        accionTab(tab);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );
        gridView = (GridView) findViewById(R.id.gridImagenes);
        adaptador = new AdaptadorDeProductos(this);
        gridView.setAdapter(adaptador);
        //gridView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

    }

    public void accionTab(TabLayout.Tab tab){
        String caso = tab.getTag().toString();
        switch (caso){
            case "Restaurante":

                break;

            case "Cafeteria":

                break;
            case "Hotel":

                break;
            case "Bar":

                break;
            case "Tecnologia":

                break;
            case "Supermercado":

                break;
            case "Ropa":

                break;
            case "Deportivo":

                break;
            case "Farmacia":

                break;
            case "Biblioteca":

                break;
            case "Zapateria":

                break;
            case "Ferreteria":

                break;
            case "Musica":

                break;
            case "Videojuegos":

                break;
            case "Jugueteria":

                break;
            case "Otro":

                break;
            case "General":

                break;
            default:
                break;
        }
    }

    private void crearCategorias(){
        categorias = new ArrayList<Categoria>();
        categorias.add(new Categoria(1, R.string.fa_icon_utensils, "Restaurante"));
        categorias.add(new Categoria(2, R.string.fa_icon_hotel, "Hotel"));
        categorias.add(new Categoria(3, R.string.fa_icon_coffee, "Cafeteria"));
        categorias.add(new Categoria(4, R.string.fa_icon_glassMartiniAlt, "Bar"));
        categorias.add(new Categoria(5, R.string.fa_icon_laptop, "Tecnologia"));
        categorias.add(new Categoria(6, R.string.fa_icon_shoppingCart, "Supermercado"));
        categorias.add(new Categoria(7, R.string.fa_icon_tshirt, "Ropa"));
        categorias.add(new Categoria(8, R.string.fa_icon_bicycle, "Deportivo"));
        categorias.add(new Categoria(9, R.string.fa_icon_capsules, "Farmacia"));
        categorias.add(new Categoria(10, R.string.fa_icon_book, "Biblioteca"));
        categorias.add(new Categoria(11, R.string.fa_icon_shoePrints, "Zapateria"));
        categorias.add(new Categoria(12, R.string.fa_icon_hammer, "Ferreteria"));
        categorias.add(new Categoria(13, R.string.fa_icon_guitar, "Musica"));
        categorias.add(new Categoria(14, R.string.fa_icon_gamepad, "Videojuegos"));
        categorias.add(new Categoria(15, R.string.fa_icon_robot, "Jugueteria"));
        categorias.add(new Categoria(16, R.string.fa_icon_storeAlt, "Otro"));
    }
}
