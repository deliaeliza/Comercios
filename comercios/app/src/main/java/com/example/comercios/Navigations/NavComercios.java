package com.example.comercios.Navigations;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.comercios.Fragments.FragAcercaDe;
import com.example.comercios.Fragments.FragActInfoComercio;
import com.example.comercios.Fragments.FragGestProductosSeccion;
import com.example.comercios.Fragments.FragHomeComercio;
import com.example.comercios.Fragments.FragMenuInferiorComercio;
import com.example.comercios.Fragments.FragProductoListarComercio;
import com.example.comercios.Fragments.FragSeccionListarComercio;
import com.example.comercios.Fragments.FragSeccionModificar;
import com.example.comercios.Fragments.FragActInfoProductos;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Login;
import com.example.comercios.R;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class NavComercios extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_comercios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragHomeComercio  mifrag2 = new FragHomeComercio();
        fragmentTransaction.replace(R.id.comercio_contenedor, mifrag2, "comercios_home");
        fragmentTransaction.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_comercios);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView) header.findViewById(R.id.NavHeaderComercios_txtViewUsuario);
        txtUsuario.setText(GlobalComercios.getInstance().getComercio().getUsuario());
        TextView txtCorreo = (TextView) header.findViewById(R.id.NavHeaderComercio_txtViewCorreo);
        txtCorreo.setText(GlobalComercios.getInstance().getComercio().getCorreo());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            switch (GlobalComercios.getInstance().getVentanaActual()) {
                case R.layout.frag_gest_productos_seccion:
                    FragSeccionModificar anterior = (FragSeccionModificar)fm.findFragmentByTag("comercios_modificar_seccion");
                    FragGestProductosSeccion actual = (FragGestProductosSeccion) fm.findFragmentByTag("comercios_productos_seccion");
                    fragmentTransaction.show(anterior);
                    fragmentTransaction.remove(actual);
                    fragmentTransaction.commit();
                    GlobalComercios.getInstance().setVentanaActual(R.layout.frag_seccion_modificar);
                    mensajeAB("Modificar Sección");
                    break;
                case R.layout.frag_seccion_modificar:
                    FragMenuInferiorComercio anterior2 = (FragMenuInferiorComercio)fm.findFragmentByTag("comercios_secciones");
                    FragSeccionListarComercio lista = (FragSeccionListarComercio)fm.findFragmentByTag("comercios_listar_seccion");
                    lista.actualizarSeccion();
                    FragSeccionModificar actual2 = (FragSeccionModificar)fm.findFragmentByTag("comercios_modificar_seccion");
                    fragmentTransaction.show(anterior2);
                    fragmentTransaction.remove(actual2);
                    fragmentTransaction.commit();
                    GlobalComercios.getInstance().setVentanaActual(R.layout.frag_menu_inferior_comercio);
                    mensajeAB("Secciones");
                    break;
                case R.layout.frag_act_info_productos:
                    FragMenuInferiorComercio comercProd = (FragMenuInferiorComercio)fm.findFragmentByTag("comercios_productos");
                    //FragProductoListarComercio mifrag = (FragProductoListarComercio) fm.findFragmentByTag("comercios_listar_producto");
                    GlobalComercios.getInstance().setPosActProd(-1);
                    GlobalComercios.getInstance().setProducto(null);
                    FragActInfoProductos actualFrag = (FragActInfoProductos) fm.findFragmentByTag("comercios_actualizar_producto");

                    fragmentTransaction.show(comercProd);
                    fragmentTransaction.remove(actualFrag);
                    fragmentTransaction.commit();
                    GlobalComercios.getInstance().setVentanaActual(R.layout.frag_producto_listar_comercio);
                    mensajeAB("Productos");
                    break;
                case R.layout.frag_menu_inferior_comercio:
                case R.layout.frag_home_comercio:
                case R.layout.frag_act_info_comercio:
                case R.layout.frag_producto_listar_comercio:
                case R.layout.frag_acerca_de:
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_comercios);
                    navigationView.getMenu().getItem(0).setChecked(true);
                    FragHomeComercio  home = new FragHomeComercio();
                    fragmentTransaction.replace(R.id.comercio_contenedor, home, "comercios_home");
                    fragmentTransaction.commit();
                    break;
                default:
                    break;
            }
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_comercios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings2) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navComercios_inicio) {
            // Handle the camera action
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragHomeComercio  mifrag2 = new FragHomeComercio();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag2, "comercios_home");
            fragmentTransaction.commit();

        } else if (id == R.id.navComercios_catalogo) {
            GlobalComercios.getInstance().setOpcActual(R.string.catalogo_lbl);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragMenuInferiorComercio mifrag = new FragMenuInferiorComercio();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag, "comercios_secciones");
            fragmentTransaction.commit();

        } else if(id == R.id.navComercios_productos){
            GlobalComercios.getInstance().setOpcActual(R.string.productos_lbl); //En el fragment pregunta cual es.
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragMenuInferiorComercio mifrag = new FragMenuInferiorComercio();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag, "comercios_productos");
            fragmentTransaction.commit();

        }else if (id == R.id.navComercios_cuenta) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragActInfoComercio mifrag = new FragActInfoComercio();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag, "comercios_cuenta");
            fragmentTransaction.commit();

        } else if (id == R.id.navComercios_cerrar) {
            GlobalComercios.getInstance().setComercio(null);
            Intent intento = new Intent(getApplicationContext(), Login.class);
            startActivity(intento);
        } else if (id == R.id.navComercios_acerca) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragAcercaDe mifrag = new FragAcercaDe ();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag, "comercios_acercaDe");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void mensajeAB(String msg){getSupportActionBar().setTitle(msg);};
}
