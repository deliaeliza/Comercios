package com.example.comercios.Navigations;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.comercios.Fragments.FragAcercaDe;
import com.example.comercios.Fragments.FragActInfoAdmin;
import com.example.comercios.Fragments.FragGestComercioLista;
import com.example.comercios.Fragments.FragGestEstandarLista;
import com.example.comercios.Fragments.FragHomeAdmin;
import com.example.comercios.Fragments.FragRegAdmin;
import com.example.comercios.Global.GlobalAdmin;
import com.example.comercios.Login;
import com.example.comercios.R;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class NavAdmin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragHomeAdmin mifrag = new FragHomeAdmin ();
        fragmentTransaction.replace(R.id.contentAdmin, mifrag, "adminHome");
        fragmentTransaction.commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_Admin);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView) header.findViewById(R.id.NavHeaderAdmin_txtViewUsuario);
        txtUsuario.setText(GlobalAdmin.getInstance().getAdmin().getUsuario());
        TextView txtCorreo = (TextView) header.findViewById(R.id.NavHeaderAdmin_txtViewCorreo);
        txtCorreo.setText(GlobalAdmin.getInstance().getAdmin().getCorreo());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int ventanaActual = GlobalAdmin.getInstance().getVentanaActual();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            switch (ventanaActual) {
                case R.layout.frag_home_admin:
                case R.layout.frag_reg_admin:
                case R.layout.frag_acerca_de:
                case R.layout.frag_gest_comercio_lista:
                case R.layout.frag_gest_estandar_lista:
                case R.layout.frag_act_info_admin:
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_Admin);
                    navigationView.getMenu().getItem(0).setChecked(true);
                    FragHomeAdmin mifrag = new FragHomeAdmin ();
                    fragmentTransaction.replace(R.id.contentAdmin, mifrag, "adminHome");
                    fragmentTransaction.commit();
                    break;
                default:
                    break;
            }
            //super.onBackPressed();
        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.adminRegistrar) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragRegAdmin mifrag = new FragRegAdmin ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "agreagarAdmin");
            fragmentTransaction.commit();
        } else if (id == R.id.adminUsuarios) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragGestEstandarLista mifrag = new FragGestEstandarLista ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "gestionarEstandar");
            fragmentTransaction.commit();
        } else if (id == R.id.adminComercios) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragGestComercioLista mifrag = new FragGestComercioLista ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "gestionarComercio");
            fragmentTransaction.commit();
        } else if (id == R.id.adminCerrarSesion) {
            //elimino la preferencias
            SharedPreferences.Editor editor =
                    getApplicationContext().getSharedPreferences("usuarioSesion", MODE_PRIVATE).edit();
            if(editor !=null){
                editor.clear().apply();
            }
            //********************************************************
            GlobalAdmin.getInstance().setAdmin(null);
            Intent intento = new Intent(getApplicationContext(), Login.class);
            startActivity(intento);
        } else if (id == R.id.nav_send) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragAcercaDe mifrag = new FragAcercaDe ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "gestionarCom");
            fragmentTransaction.commit();
        }else if(id == R.id.adminHome){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragHomeAdmin mifrag = new FragHomeAdmin ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "adminHome");
            fragmentTransaction.commit();
        }else if(id == R.id.adminActInformacion){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragActInfoAdmin mifrag = new FragActInfoAdmin ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "actInfoAdmin");
            fragmentTransaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
