package com.example.comercioscostarica;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OnclickDelButton(R.id.button);
    }

    public String enviarDatosGet(String usuario, String pas) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;

        try {
            url = new URL("http://10.251.33.47/ServiciosWeb/Prueba.php?email=" +usuario+ "&pass=" + pas);
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
            MensajeOK(e.getMessage());
        }
        return resul.toString();
    }

    public int obtDatosJSON(String respuesta){
        int res = 0;
        try{
            JSONArray json = new JSONArray(respuesta);
            if(json.length()>0){
                res = 1;
            }
        }catch(Exception ex){

        }
        return res;
    }

    public void OnclickDelButton(int ref) {
        View view = findViewById(ref);
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button:
                        final EditText correo = (EditText) findViewById(R.id.edtEmail);
                        final EditText pass = (EditText) findViewById(R.id.edtPass);
                        Thread tr = new Thread() {
                            @Override
                            public void run() {
                                final String resultado = enviarDatosGet(correo.getText().toString(), pass.getText().toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int r = obtDatosJSON(resultado);
                                        if (r > 0) {
                                            MensajeOK("Todo bien");
                                        }else{
                                            MensajeOK("Todo mal");
                                        }
                                    }
                                });
                            }
                        };

                        tr.start();
                        //MensajeOK(add);
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton

    public void MensajeOK(String msg) {
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
