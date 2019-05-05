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
import com.example.comercios.Fragments.FragActInfoUsuario;
import com.example.comercios.Fragments.FragHomeSuperUsuario;
import com.example.comercios.Fragments.FragHomeUsuarioEstandar;
import com.example.comercios.Fragments.FragVerProductosGrid;
import com.example.comercios.Global.GlobalAdmin;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Global.GlobalSuperUsuario;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Login;
import com.example.comercios.R;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class NavUsuarios extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_usuarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragHomeUsuarioEstandar mifrag2 = new FragHomeUsuarioEstandar();
        fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag2, "HomeSU");
        fragmentTransaction.commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view3);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView) header.findViewById(R.id.NavHeaderUsuario_txtViewUsuario);
        txtUsuario.setText(GlobalUsuarios.getInstance().getUserE().getUsuario());
        TextView txtCorreo = (TextView) header.findViewById(R.id.NavHeaderUsuario_txtViewCorreo);
        txtCorreo.setText(GlobalUsuarios.getInstance().getUserE().getCorreo());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int ventanaActual = GlobalUsuarios.getInstance().getVentanaActual();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            switch (ventanaActual) {
                case R.layout.frag_home_usuario_estandar:
                case R.layout.frag_act_info_usuario:
                case R.layout.frag_acerca_de:
                    FragHomeUsuarioEstandar mifrag2 = new FragHomeUsuarioEstandar();
                    fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag2, "HomeSU");
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
        getMenuInflater().inflate(R.menu.nav_usuarios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings3) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.usuarioInicio) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragHomeUsuarioEstandar mifrag2 = new FragHomeUsuarioEstandar();
            fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag2, "HomeSU");
            fragmentTransaction.commit();
        } else if (id == R.id.usuarioMapa) {
            return false;
        } else if (id == R.id.usuarioTodosComercios) {
            //QUITAR DE AUÍ
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragVerProductosGrid mifrag = new FragVerProductosGrid();
            fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag, "contendorProductos");
            fragmentTransaction.commit();
        } else if (id == R.id.usuarioActInformacion) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragActInfoUsuario mifrag = new FragActInfoUsuario();
            fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag, "contendorActInformacion");
            fragmentTransaction.commit();
        } else if (id == R.id.usuarioEstandarcerrarSeion) {
            GlobalUsuarios.getInstance().setUserE(null);
            Intent intento = new Intent(getApplicationContext(), Login.class);
            startActivity(intento);
        } else if (id == R.id.acercaDe) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragAcercaDe mifrag = new FragAcercaDe();
            fragmentTransaction.replace(R.id.Usuario_contenedor, mifrag, "gestionarCom");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
