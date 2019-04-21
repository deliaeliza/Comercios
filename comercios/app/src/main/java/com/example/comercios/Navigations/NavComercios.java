package com.example.comercios.Navigations;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.comercios.Fragments.FragAcercaDe;
import com.example.comercios.Fragments.FragActInfoComercio;
import com.example.comercios.Fragments.FragHomeComercio;
import com.example.comercios.Fragments.FragMenuInferiorComercio;
import com.example.comercios.Global.GlobalAdmin;
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

      /*  TextView usuario = (TextView)findViewById(R.id.NavHeaderComercios_txtViewUsuario);
        usuario.setText(GlobalComercios.getInstance().getComercio().getUsuario());
        TextView correo = (TextView)findViewById(R.id.NavHeaderComercio_txtViewCorreo);
        correo.setText(GlobalComercios.getInstance().getComercio().getCorreo());*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragHomeComercio  mifrag2 = new FragHomeComercio();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag2, "Home");
            fragmentTransaction.commit();
            super.onBackPressed();
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
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragHomeComercio  mifrag2 = new FragHomeComercio();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag2, "Home");
            fragmentTransaction.commit();

        } else if (id == R.id.navComercios_catalogo) {
            GlobalComercios.getInstance().setOpcActual(R.string.catalogo_lbl);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragMenuInferiorComercio  mifrag = new FragMenuInferiorComercio();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag, "id");
            fragmentTransaction.commit();

        } else if(id == R.id.navComercios_productos){
            GlobalComercios.getInstance().setOpcActual(R.string.productos_lbl); //En el fragment pregunta cual es.
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragMenuInferiorComercio  mifrag = new FragMenuInferiorComercio();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag, "id");
            fragmentTransaction.commit();

        }else if (id == R.id.navComercios_cuenta) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragActInfoComercio mifrag = new FragActInfoComercio();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag, "id");
            fragmentTransaction.commit();

        } else if (id == R.id.navComercios_cerrar) {
            GlobalComercios.getInstance().setComercio(null);
            Intent intento = new Intent(getApplicationContext(), Login.class);
            startActivity(intento);
        } else if (id == R.id.navComercios_acerca) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragAcercaDe mifrag = new FragAcercaDe ();
            fragmentTransaction.replace(R.id.comercio_contenedor, mifrag, "gestionarCom");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
