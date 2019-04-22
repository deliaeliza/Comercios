package com.example.comercios.Navigations;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.example.comercios.Fragments.FragAcercaDe;
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
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragHomeAdmin mifrag = new FragHomeAdmin ();
        fragmentTransaction.replace(R.id.contentAdmin, mifrag, "adminHome");
        fragmentTransaction.commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragHomeAdmin mifrag = new FragHomeAdmin ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "adminHome");
            fragmentTransaction.commit();
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.adminRegistrar) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragRegAdmin mifrag = new FragRegAdmin ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "agreagarAdmin");
            fragmentTransaction.commit();
        } else if (id == R.id.adminUsuarios) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragGestEstandarLista mifrag = new FragGestEstandarLista ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "gestionarEstandar");
            fragmentTransaction.commit();
        } else if (id == R.id.adminComercios) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragGestComercioLista mifrag = new FragGestComercioLista ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "gestionarComercio");
            fragmentTransaction.commit();
        } else if (id == R.id.adminCerrarSesion) {
            GlobalAdmin.getInstance().setAdmin(null);
            Intent intento = new Intent(getApplicationContext(), Login.class);
            startActivity(intento);
        } else if (id == R.id.nav_send) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragAcercaDe mifrag = new FragAcercaDe ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "gestionarCom");
            fragmentTransaction.commit();
        }else if(id == R.id.adminHome){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragHomeAdmin mifrag = new FragHomeAdmin ();
            fragmentTransaction.replace(R.id.contentAdmin, mifrag, "adminHome");
            fragmentTransaction.commit();
        }else if(id == R.id.adminActInformacion){
            return false;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
