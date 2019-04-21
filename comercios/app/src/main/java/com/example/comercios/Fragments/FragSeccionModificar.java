package com.example.comercios.Fragments;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.Modelo.Seccion;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class FragSeccionModificar extends Fragment {

    private TextInputEditText nombre;
    private TextInputLayout tilNombre;

    public FragSeccionModificar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_seccion_modificar, container, false);
        mensajeAB("Modificar Sección");
        nombre = (TextInputEditText) view.findViewById(R.id.sec_modificar_TextInputEditText);
        tilNombre = (TextInputLayout) view.findViewById(R.id.sec_modificar_TextInputLayout);
        OnclickDelMaterialButtom(view.findViewById(R.id.sec_modificar_MaterialButtonAct));
        OnclickDelMaterialButtom(view.findViewById(R.id.sec_modificar_MaterialButtonEliminar));
        OnclickDelMaterialButtom(view.findViewById(R.id.sec_modificar_MaterialButtonProductos));
        nombre.setText(GlobalComercios.getInstance().getSeccion().getNombre());
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarDatos();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        return view;
    }

    private void OnclickDelMaterialButtom(View view) {
        MaterialButton miMaterialButtom = (MaterialButton) view;
        miMaterialButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.sec_modificar_MaterialButtonAct:
                        if (validarDatos()) {
                            actualizarSeccion();
                        }
                        break;

                    case R.id.sec_modificar_MaterialButtonEliminar:
                        DialogSiNO();
                        break;
                    case R.id.sec_modificar_MaterialButtonProductos:
                        //Pasar gestionar productos
                        break;
                    default:
                        break;
                }
            }// fin del onclick
        });
    }

    private boolean validarDatos() {
        String dato = nombre.getText().toString();
        if (dato.length() > 51)
            return false;
        if (dato.length() > 0 && dato.length() <= 50 && Util.PATRON_UN_CARACTER_ALFANUMERICO.matcher(dato).find()) {
            tilNombre.setError(null);
            return true;
        }
        tilNombre.setError("Nombre invalido");
        return false;
    }

    private void actualizarSeccion() {
        final String dato = nombre.getText().toString();
        String url = Util.urlWebService + "/seccionActualizar.php?id=" +
              GlobalComercios.getInstance().getSeccion().getId() + "&nombre=" + dato;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonA = response.getJSONArray("respuesta");
                    JSONObject mensajeError = jsonA.getJSONObject(0);
                    if (mensajeError.getString("mensajeError").equalsIgnoreCase("")) {
                        mensajeToast("Exito: Nombre actualizado");
                        GlobalComercios.getInstance().getSeccion().setNombre(dato);
                    } else {
                        mensajeToast(mensajeError.getString("mensajeError"));
                        nombre.setText(GlobalComercios.getInstance().getSeccion().getNombre());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("No se puede conectar " + error.toString());
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    /*private void recuperarSeccion() {
        //String url = Util.urlWebService + "/seccionRecuperar.php?id=" + GlobalComercios.getInstance().getIdSecModificar();
        String url = Util.urlWebService + "/seccionRecuperar.php?id=" + 4;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if (mensajeError.equalsIgnoreCase("")) {
                        JSONObject sec = jsonOb.getJSONObject("seccion");
                        //if(GlobalComercios.getInstance().getComercio().getId() == sec.getInt("idComercio")) {
                        seccion = new Seccion(sec.getInt("id"), sec.getString("nombre"));
                        nombre.setText(seccion.getNombre());
                        //} else {
                        //Pasar a lista secciones
                        //}
                    } else {
                        mensajeToast(mensajeError);
                        remplazoConListaSecciones();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("No se puede conectar " + error.toString());
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }*/


    private void eliminarSeccion() {
        String url = Util.urlWebService + "/seccionEliminar.php?";

        StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("")) {
                    mensajeToast("Exito: Se elimino correctamente");
                    remplazoConListaSecciones();
                } else {
                    mensajeToast(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("No se puede conectar " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", Integer.toString(GlobalComercios.getInstance().getSeccion().getId()));
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
    }

    private void remplazoConListaSecciones(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragSeccionListarComercio  mifrag = new FragSeccionListarComercio ();
        fragmentTransaction.replace(R.id.menuInferiorComercios_contenido, mifrag, "IdMenuInferior");
        fragmentTransaction.commit();
    }

    private void DialogSiNO(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle("¿Eliminar sección?");
        builder1.setMessage("Ya no abrá productos relacionados a esta sección");
        builder1.setCancelable(true);
        builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    } });
        builder1.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliminarSeccion();
                    } });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    };

    private void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};

}
