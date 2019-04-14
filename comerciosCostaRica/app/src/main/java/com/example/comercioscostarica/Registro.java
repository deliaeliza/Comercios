package com.example.comercioscostarica;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.comercioscostarica.Fragments.FragRegEmpresa;
import com.example.comercioscostarica.Fragments.FragRegUser;

public class Registro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
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
        mensajeAB("Regi");
    }

    private void mostrarFrag(boolean mostrarUser){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(mostrarUser) {
            FragRegUser mifrag = new FragRegUser();
            fragmentTransaction.replace(R.id.Reg_contenido, mifrag, "idFragRegUser");
        } else {
            FragRegEmpresa mifrag = new FragRegEmpresa();
            fragmentTransaction.replace(R.id.Reg_contenido, mifrag, "idFragRegEmp");
        }
        fragmentTransaction.commit();
    }


    public void mensajeAB(String msg){getSupportActionBar().setTitle(msg);};
    public void mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
