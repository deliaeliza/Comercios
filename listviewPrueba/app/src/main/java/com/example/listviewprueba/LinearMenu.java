package com.example.listviewprueba;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class LinearMenu extends AppCompatActivity {
    ArrayList<Categoria> categorias = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_menu);
        crearCategorias();
        crearFragments();
    }
    private void crearCategorias(){
        categorias = new ArrayList<Categoria>();
        categorias.add(new Categoria(1, R.string.fa_icon_utensils, "Restaurante"));
        categorias.add(new Categoria(2, R.string.fa_icon_hotel, "Hotel"));
        categorias.add(new Categoria(3, R.string.fa_icon_coffee, "Cafeteria"));
        categorias.add(new Categoria(4, R.string.fa_icon_glassMartiniAlt, "Bar"));
        categorias.add(new Categoria(5, R.string.fa_icon_laptop, "Tenologia"));
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
    private void crearFragments(){
        for (Categoria c: categorias){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            LinearMenu_item  mifrag = new LinearMenu_item ();
            mifrag.setCategoria(c);
            fragmentTransaction.add(R.id.linearMenu_linearLayout, mifrag, "id" + c.getId());
            fragmentTransaction.commit();
        }
    }
}
