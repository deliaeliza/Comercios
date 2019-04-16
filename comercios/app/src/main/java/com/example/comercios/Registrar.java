package com.example.comercios;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Registrar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        mostrarFrag(true);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.Reg_rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbUser = (RadioButton) findViewById(R.id.Reg_radioUser);
                RadioButton rbEmp = (RadioButton) findViewById(R.id.Reg_radioEmpresa);
                if (rbUser.isChecked()) {
                    mostrarFrag(true);
                } else if (rbEmp.isChecked()) {
                    mostrarFrag(false);
                }
            }
        });
        mensajeAB("Unetenos");
    }



    private void mostrarFrag(boolean mostrarUser){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(mostrarUser) {
            com.example.comercios.fragments.FragRegUser mifrag = new com.example.comercios.fragments.FragRegUser();
            fragmentTransaction.replace(R.id.Reg_contenido, mifrag, "idFragRegUser");
        } else {
            com.example.comercios.fragments.FragRegEmpresa mifrag = new com.example.comercios.fragments.FragRegEmpresa();
            fragmentTransaction.replace(R.id.Reg_contenido, mifrag, "idFragRegEmp");
        }
        fragmentTransaction.commit();
    }

    public void mensajeAB(String msg){getSupportActionBar().setTitle(msg);};
    public void mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
