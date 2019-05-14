package com.example.comercios;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.comercios.Fragments.FragHomeUsuarioEstandar;
import com.example.comercios.Fragments.FragRegEmpresa;
import com.example.comercios.Fragments.FragRegUser;
import com.example.comercios.Global.GlobalUsuarios;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Registrar extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        tabLayout = (TabLayout) findViewById(R.id.act_reg_tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Usuario")) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    FragRegUser mifrag = new FragRegUser();
                    fragmentTransaction.replace(R.id.registros_content, mifrag, "regUser");
                    fragmentTransaction.commit();
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    FragRegEmpresa mifrag = new FragRegEmpresa();
                    fragmentTransaction.replace(R.id.registros_content, mifrag, "regEmpresa");
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragRegUser mifrag = new FragRegUser();
        fragmentTransaction.replace(R.id.registros_content, mifrag, "regUser");
        fragmentTransaction.commit();
        mensajeAB("Unetenos");
    }

    @Override
    public void onBackPressed() {
        int ventanaActual = GlobalUsuarios.getInstance().getVentanaActual();
        switch (ventanaActual) {
            case R.layout.frag_reg_empresa:
            case R.layout.frag_reg_user:
                Intent intento = new Intent(getApplicationContext(), Login.class);
                startActivity(intento);
                break;
            default:
                break;
        }
        //super.onBackPressed();

    }

    public void mensajeAB(String msg) {
        getSupportActionBar().setTitle(msg);
    }

    ;

    public void mensaje(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    ;
}
