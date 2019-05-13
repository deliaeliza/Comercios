package com.example.comercios.Navigations;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.comercios.Fragments.FragAcercaDe;
import com.example.comercios.Fragments.FragActInfoUsuario;
import com.example.comercios.Fragments.FragEmpresasMaps;
import com.example.comercios.Fragments.FragHomeUsuarioEstandar;
import com.example.comercios.Fragments.FragVerComerciosLista;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Login;
import com.example.comercios.R;
import com.example.comercios.ViewPager.ViewPagerNoSwipe;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class NavUsuarios extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MyPagerAdapter myPagerAdapter;
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
        GlobalUsuarios.viewPagerNoSwipe = (ViewPagerNoSwipe) findViewById(R.id.content_nav_usuarios_viewpager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        GlobalUsuarios.viewPagerNoSwipe.setAdapter(myPagerAdapter);
        GlobalUsuarios.viewPagerNoSwipe.setCurrentItem(GlobalUsuarios.PAGINA_INICIO, false);
        GlobalUsuarios.viewPagerNoSwipe.setOffscreenPageLimit(0);
        GlobalUsuarios.viewPagerNoSwipe.setPagingEnabled(false);
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
            switch (ventanaActual) {
                case R.layout.frag_home_usuario_estandar:
                case R.layout.frag_act_info_usuario:
                case R.layout.frag_acerca_de:

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
            GlobalUsuarios.viewPagerNoSwipe.setCurrentItem(GlobalUsuarios.PAGINA_INICIO, false);
            GlobalUsuarios.viewPagerNoSwipe.setOffscreenPageLimit(0);
        } else if (id == R.id.usuarioMapa) {
            GlobalUsuarios.viewPagerNoSwipe.setOffscreenPageLimit(0);
            //GlobalUsuarios.viewPagerNoSwipe.setCurrentItem(GlobalUsuarios.PAGINA_MAPA, false);
        } else if (id == R.id.usuarioTodosComercios) {
            GlobalUsuarios.viewPagerNoSwipe.setCurrentItem(GlobalUsuarios.PAGINA_LISTACOMERCIOS, false);
            GlobalUsuarios.viewPagerNoSwipe.setOffscreenPageLimit(0);
        } else if (id == R.id.usuarioActInformacion) {
            GlobalUsuarios.viewPagerNoSwipe.setCurrentItem(GlobalUsuarios.PAGINA_CUENTA, false);
            GlobalUsuarios.viewPagerNoSwipe.setOffscreenPageLimit(0);
        } else if (id == R.id.usuarioEstandarcerrarSeion) {
            GlobalUsuarios.getInstance().setUserE(null);
            Intent intento = new Intent(getApplicationContext(), Login.class);
            startActivity(intento);
        } else if (id == R.id.acercaDe) {
            GlobalUsuarios.viewPagerNoSwipe.setCurrentItem(GlobalUsuarios.PAGINA_ACERCA, false);
            GlobalUsuarios.viewPagerNoSwipe.setOffscreenPageLimit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case GlobalUsuarios.PAGINA_INICIO:
                    return new FragHomeUsuarioEstandar();
                case GlobalUsuarios.PAGINA_CUENTA:
                    return new FragActInfoUsuario();
                case GlobalUsuarios.PAGINA_ACERCA:
                    return new FragAcercaDe();
                /*case GlobalUsuarios.PAGINA_MAPA:
                    return new FragEmpresasMaps();*/
                case GlobalUsuarios.PAGINA_LISTACOMERCIOS:
                    return new FragVerComerciosLista();
                /*case GlobalUsuarios.PAGINA_VER_COMERCIOS:
                    //return new*/
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}
