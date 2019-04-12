package com.example.comercioscostarica;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        OnclickDelButton(R.id.Login_btnIgresar);
        OnclickDelButton(R.id.button);

        OnclickDelTextView(R.id.Login_txtRegistrar);
        OnclickDelTextView(R.id.Login_txtOlvido);
        mensajeAB("Ingresar");
    }

    public String enviarDatosGet(String usuario, String pas) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;

        try {
            url = new URL("http://192.168.0.13/ServiciosWeb/Prueba.php?email=" +usuario+ "&pass=" + pas);
            HttpURLConnection cnx = (HttpURLConnection)url.openConnection();
            respuesta = cnx.getResponseCode();
            resul = new StringBuilder();
            if(respuesta == HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(cnx.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine())!=null){
                    resul.append(linea);
                }
            }
        } catch (Exception e) {
            mensajeOK(e.getMessage());
        }
        return resul.toString();
    }

    public void OnclickDelButton(int ref) {
        View view = findViewById(ref);
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Login_btnIgresar:
                        final EditText correo = (EditText) findViewById(R.id.Login_edtEmail);
                        final EditText pass = (EditText) findViewById(R.id.Login_edtPass);
                        Thread tr = new Thread() {
                            @Override
                            public void run() {
                                final String resultado = enviarDatosGet(correo.getText().toString(), pass.getText().toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            JSONArray json = new JSONArray(resultado);
                                            mensajeOK(json.toString());
                                        }catch(Exception ex){

                                        }
                                    }
                                });
                            }
                        };
                        tr.start();
                        break;
                    case R.id.button:
                        Intent intento = new Intent(getApplicationContext(), NavAdmin.class);
                        startActivity(intento);
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton
    public void OnclickDelTextView(int ref) {

        // Ejemplo  OnclickDelTextView(R.id.MiTextView);
        // 1 Doy referencia al TextView
        View view =findViewById(ref);
        TextView miTextView = (TextView) view;
        //  final String msg = miTextView.getText().toString();
        // 2.  Programar el evento onclick
        miTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // if(msg.equals("Texto")){Mensaje("Texto en el botón ");};
                switch (v.getId()) {

                    case R.id.Login_txtRegistrar:
                        Intent intento = new Intent(getApplicationContext(), Registro.class);
                        startActivity(intento);
                        break;

                    case R.id.Login_txtOlvido:
                        mensaje("Implementar activity de olvido");
                        break;
                    default:break; }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelTextView


    public void mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
    public void mensajeAB(String msg){getSupportActionBar().setTitle(msg);};
    public void mensajeOK(String msg) {
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
