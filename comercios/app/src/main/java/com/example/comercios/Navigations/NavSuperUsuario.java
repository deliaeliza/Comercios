package com.example.comercios.Navigations;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.comercios.Fragments.FragAcercaDe;
import com.example.comercios.Fragments.FragGestAdminLista;
import com.example.comercios.Fragments.FragGestComercioLista;
import com.example.comercios.Fragments.FragGestEstandarLista;
import com.example.comercios.Fragments.FragHomeAdmin;
import com.example.comercios.Fragments.FragHomeComercio;
import com.example.comercios.Fragments.FragHomeSuperUsuario;
import com.example.comercios.Fragments.FragRegAdmin;
import com.example.comercios.Global.GlobalAdmin;
import com.example.comercios.Global.GlobalSuperUsuario;
import com.example.comercios.Login;
import com.example.comercios.R;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import org.w3c.dom.Text;

public class NavSuperUsuario extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_super_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragHomeSuperUsuario mifrag = new FragHomeSuperUsuario();
        fragmentTransaction.replace(R.id.superUsuario_contenedor, mifrag, "gestAdmins");
        fragmentTransaction.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view4);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        TextView txtUsuario = (TextView) header.findViewById(R.id.NavHeaderSuper_txtViewUsuario);
        txtUsuario.setText(GlobalSuperUsuario.getInstance().getAdmin().getUsuario());
        TextView txtCorreo = (TextView) header.findViewById(R.id.NavHeaderSuper_txtViewCorreo);
        txtCorreo.setText(GlobalSuperUsuario.getInstance().getAdmin().getCorreo());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int ventanaActual = GlobalSuperUsuario.getInstance().getVentanaActual();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            switch (ventanaActual) {
                case R.layout.frag_home_super_usuario:
                case R.layout.frag_reg_admin:
                case R.layout.frag_acerca_de:
                case R.layout.frag_gest_comercio_lista:
                case R.layout.frag_gest_estandar_lista:
                case R.layout.frag_gest_admin_lista:
                    FragHomeSuperUsuario mifrag2 = new FragHomeSuperUsuario();
                    fragmentTransaction.replace(R.id.superUsuario_contenedor, mifrag2, "HomeSU");
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
        getMenuInflater().inflate(R.menu.nav_super_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings4) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.superUsuarioHome){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragHomeSuperUsuario mifrag = new FragHomeSuperUsuario();
            fragmentTransaction.replace(R.id.superUsuario_contenedor, mifrag, "gestAdmins");
            fragmentTransaction.commit();
        } else if (id == R.id.superUsuarioAdmin) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragGestAdminLista mifrag = new FragGestAdminLista();
            fragmentTransaction.replace(R.id.superUsuario_contenedor, mifrag, "gestAdmins");
            fragmentTransaction.commit();
        } else if (id == R.id.superUsuarioUsuarios) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragGestEstandarLista mifrag = new FragGestEstandarLista ();
            fragmentTransaction.replace(R.id.superUsuario_contenedor, mifrag, "gestionarEstandarSU");
            fragmentTransaction.commit();
        } else if (id == R.id.superUsuarioComercios) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragGestComercioLista mifrag = new FragGestComercioLista ();
            fragmentTransaction.replace(R.id.superUsuario_contenedor, mifrag, "gestionarComercioSU");
            fragmentTransaction.commit();
        } else if (id == R.id.superUsuarioCerrarSesion) {
            GlobalSuperUsuario.getInstance().setAdmin(null);
            Intent intento = new Intent(getApplicationContext(), Login.class);
            startActivity(intento);
        } else if (id == R.id.nav_send4) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragAcercaDe mifrag = new FragAcercaDe ();
            fragmentTransaction.replace(R.id.superUsuario_contenedor, mifrag, "gestionarComercioSU");
            fragmentTransaction.commit();
        }else if(id == R.id.superUsuarioadminRegistrar){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragRegAdmin mifrag = new FragRegAdmin ();
            fragmentTransaction.replace(R.id.superUsuario_contenedor, mifrag, "agreagarAdminSU");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
