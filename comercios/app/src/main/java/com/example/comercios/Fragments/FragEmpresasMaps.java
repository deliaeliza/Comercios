package com.example.comercios.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Global.GlobalGeneral;
import com.example.comercios.Global.GlobalUsuarios;
import com.example.comercios.Modelo.Categorias;
import com.example.comercios.Modelo.Comercio;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FragEmpresasMaps extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap = null;
    MapView mapView;
    private ArrayList<Categorias> categorias;
    private ArrayList<Comercio> comercios;
    private TabLayout tabLayout;
    private final int MIS_PERMISOS = 100;
    private double latitud;
    private double longitud;
    private boolean moverCamara = false;
    private String opcionEscogida = "";

    public FragEmpresasMaps() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_empresas_maps, container, false);
        GlobalUsuarios.getInstance().setVentanaActual(R.layout.frag_empresas_maps);
        mensajeAB("Comercios");
        categorias = new ArrayList<>();
        comercios = new ArrayList<>();
        tabLayout = (TabLayout) view.findViewById(R.id.fragEmpMap_menuNavigationTab);
        cargarCategorias();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mGoogleMap != null) {
                    mGoogleMap.clear();
                    recuperarComerios();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.mapa_contenedor);
        mapFragment.getMapAsync(this);
        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (solicitaPermisosVersionesSuperioresGPS()) {
            locationStart();
        }

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                DialogoInformacion(marker);
            }
        });

        moverCamara = true;
    }

    private void DialogoInformacion(final Marker marker){
        View view = getLayoutInflater().inflate(R.layout.frag_dialogo_maps, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(),R.style.CustomBottomSheetDialogTheme);
        dialog.setContentView(view);
        TextView duracion= (TextView) dialog.findViewById(R.id.fragDialgMaps_duracion);
        TextView distancia= (TextView) dialog.findViewById(R.id.fragDialgMaps_km);
        TextView nombre = (TextView) dialog.findViewById(R.id.fragDialgMaps_nomEmp);
        nombre.setText(marker.getTitle());
        MaterialButton verMas = (MaterialButton) dialog.findViewById(R.id.fragDialgMaps_verMas);
        final MaterialCardView btnBus = (MaterialCardView)dialog.findViewById(R.id.fragDialgMaps_bus);
        final MaterialCardView btnCaminar = (MaterialCardView)dialog.findViewById(R.id.fragDialgMaps_caminar);
        final MaterialCardView btnCar = (MaterialCardView)dialog.findViewById(R.id.fragDialgMaps_car);
        final MaterialCardView btnBicicleta = (MaterialCardView)dialog.findViewById(R.id.fragDialgMaps_bicicleta);
        MaterialCardView btnIr = (MaterialCardView) dialog.findViewById(R.id.fragDialgMaps_ir);

        verMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                FragRegUser mifrag = new FragRegUser();
                fragmentTransaction.replace(R.id.registros_content, mifrag, "regUser");
                fragmentTransaction.commit();*/
            }
        });
        btnBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionEscogida = "transit";
                btnBicicleta.setChecked(false);
                btnCaminar.setChecked(false);
                btnCar.setChecked(false);
                btnBus.setChecked(true);
            }
        });
        btnCaminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionEscogida = "walking";
                btnBicicleta.setChecked(false);
                btnCaminar.setChecked(true);
                btnCar.setChecked(false);
                btnBus.setChecked(false);
            }
        });
        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionEscogida = "driving";
                btnBicicleta.setChecked(false);
                btnCaminar.setChecked(false);
                btnCar.setChecked(true);
                btnBus.setChecked(false);
            }
        });
        btnBicicleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionEscogida = "bicycling";
                btnBicicleta.setChecked(true);
                btnCaminar.setChecked(false);
                btnCar.setChecked(false);
                btnBus.setChecked(false);
            }
        });
        btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!opcionEscogida.equalsIgnoreCase("")){
                    recuperarRuta(new LatLng(latitud,longitud),marker.getPosition(),opcionEscogida);
                }
            }
        });
        dialog.show();
    }

    //-------------------------------------- Mapas rutas _----------------------------------------------------------------

    private void recuperarRuta(LatLng partida, LatLng llegada, String modo) {
        String url = Util.URL_API_DIRECTIONS + "/json?" +
                "origin=" + partida.latitude + "," + partida.longitude +
                "&destination=" + llegada.latitude + "," + llegada.longitude +
                "&mode=" + modo + "&key=" + Util.key;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray routes = response.getJSONArray("routes");
                    JSONObject bounds = routes.getJSONObject(0);
                    JSONArray legs = bounds.optJSONArray("legs");
                    String distance = legs.optJSONObject(0).optJSONObject("distance").getString("text");
                    String duration = legs.optJSONObject(0).optJSONObject("duration").getString("text");
                    JSONArray steps = legs.optJSONObject(0).optJSONArray("steps");


                } catch (JSONException e) {
                    e.printStackTrace();
               }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("No se puede conectar");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    //-------------------------------------- Mapas rutas _----------------------------------------------------------------


    private void recuperarComerios() {
        String query = "SELECT u.*, c.*, COUNT(ca.calificacion) cantidad, IFNULL(AVG(ca.calificacion), 0) calificacion" +
                " FROM Comercios c INNER JOIN Usuarios u ON c.idUsuario = u.id" +
                " LEFT OUTER JOIN Calificaciones ca ON c.idUsuario = ca.idComercio WHERE u.estado='1'";
        //Agregar fitros
        int idCategoria = (int) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getTag();
        if (idCategoria != -1) {
            query += " AND c.idCategoria='" + idCategoria + "'";
        }
        //Limite despues de los filtros
        query += " GROUP BY c.idUsuario";
        String url = Util.urlWebService + "/comerciosListar.php?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    comercios.clear();
                    JSONObject jsonOb = response.getJSONObject("datos");
                    String mensajeError = jsonOb.getString("mensajeError");
                    if (mensajeError.equalsIgnoreCase("")) {
                        if (jsonOb.has("usuarios")) {
                            JSONArray users = jsonOb.getJSONArray("usuarios");
                            if (users.length() != 0) {
                                for (int i = 0; i < users.length(); i++) {
                                    JSONObject usuario = users.getJSONObject(i);
                                    String categoria = "";
                                    for (Categorias c : categorias) {
                                        if (c.getId() == usuario.getInt("categoria")) {
                                            categoria = c.getNombre();
                                            break;
                                        }
                                    }
                                    comercios.add(new Comercio(
                                            usuario.getInt("id"),
                                            usuario.getInt("tipo"),
                                            usuario.getLong("telefono"),
                                            (float) usuario.getDouble("calificacion"),
                                            usuario.getInt("cantidad"),
                                            usuario.getInt("verificado") == 1,
                                            usuario.getInt("estado") == 1,
                                            usuario.getString("correo"),
                                            usuario.getString("usuario"),
                                            usuario.getString("descripcion"),
                                            categoria,
                                            usuario.isNull("urlImagen") ? null : Util.urlWebService + "/" + usuario.getString("urlImagen"),
                                            usuario.getDouble("latitud"),
                                            usuario.getDouble("longitud"),
                                            usuario.getString("ubicacion")));
                                }
                                for (Comercio c : comercios) {
                                    LatLng lg = new LatLng(c.getLatitud(), c.getLongitud());
                                    mGoogleMap.addMarker(new MarkerOptions().position(lg).title(c.getUsuario()).snippet(c.getDescripcion()));
                                }
                            }
                        }
                    } else {
                        mensajeToast(mensajeError);
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

    private void cargarCategorias() {
        String url = Util.urlWebService + "/categoriasObtener.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonCategorias = response.getJSONArray("categoria");
                    JSONObject obj;
                    for (int i = 0; i < jsonCategorias.length(); i++) {
                        obj = jsonCategorias.getJSONObject(i);
                        categorias.add(new Categorias(obj.getInt("id"), obj.getString("nombre")));
                    }
                    cargarTabLayout();
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
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void cargarTabLayout() {
        if (categorias != null) {
            TabLayout.Tab todos = tabLayout.newTab();
            todos.setText("Todos");
            todos.setIcon(recuperarIcono("Todos"));
            todos.setTag(-1);
            tabLayout.addTab(todos);
            for (Categorias c : categorias) {
                TabLayout.Tab t = tabLayout.newTab();
                t.setText(c.getNombre());
                t.setIcon(recuperarIcono(c.getNombre()));
                t.setTag(c.getId());
                tabLayout.addTab(t);
            }
        }
        recuperarComerios();
    }

    private int recuperarIcono(String categoria) {
        switch (categoria) {
            case "Todos":
                return R.drawable.store_alt;
            case "Bar":
                return R.drawable.glass_martini_alt;
            case "Cafe":
                return R.drawable.coffee;
            case "Deportes":
                return R.drawable.bicycle;
            case "Farmacia":
                return R.drawable.capsules;
            case "Ferreteria":
                return R.drawable.hammer;
            case "Hotel":
                return R.drawable.hotel;
            case "Jugueteria":
                return R.drawable.robot;
            case "Libreria":
                return R.drawable.book;
            case "Musica":
                return R.drawable.guitar;
            case "Restaurante":
                return R.drawable.utensils;
            case "Ropa":
                return R.drawable.tshirt;
            case "Tecnologia":
                return R.drawable.laptop;
            case "Videojuegos":
                return R.drawable.gamepad;
            case "Zapateria":
                return R.drawable.shoe_prints;
            case "Otro":
                return R.drawable.shopping_cart;
            default:
                return -1;
        }
    }

    private void mensajeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    ;

    private void cargarDialogoRecomendacionGPS() {
        androidx.appcompat.app.AlertDialog.Builder dialogo = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // mGoogleMap.setMyLocationEnabled(true);
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MIS_PERMISOS);
                // mGoogleMap.setMyLocationEnabled(true);
            }
        });
        dialogo.show();
    }

    private boolean solicitaPermisosVersionesSuperioresGPS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//validamos si estamos en android menor a 6 para no buscar los permisos
            //mGoogleMap.setMyLocationEnabled(true);
            return true;
        }
        //validamos si los permisos ya fueron aceptados
        if ((getActivity().checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && getActivity().checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            return true;
        }
        if ((shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) || (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)))) {
            cargarDialogoRecomendacionGPS();
        } else {
            mGoogleMap.setMyLocationEnabled(true);
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MIS_PERMISOS);
        }
        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MIS_PERMISOS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {//el dos representa los 2 permisos

            }
        } else {
            solicitarPermisosManual();
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones = new androidx.appcompat.app.AlertDialog.Builder(getActivity());//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        FragEmpresasMaps.Localizacion Local = new FragEmpresasMaps.Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }


    public class Localizacion implements LocationListener {
        FragEmpresasMaps mainActivity;

        public FragEmpresasMaps getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(FragEmpresasMaps mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
            if (moverCamara) {
                CameraPosition camaraPosition = CameraPosition.builder().target(new LatLng(latitud, longitud)).zoom(15).build();
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camaraPosition));
                moverCamara = false;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //cordenadas = "GPS Desactivado";
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //cordenadas = "GPS Activado";
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    private void mensajeAB(String msg) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(msg);
    }

    ;


}