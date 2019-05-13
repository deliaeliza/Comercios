package com.example.comercios.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comercios.Modelo.Categorias;
import com.example.comercios.Modelo.Util;
import com.example.comercios.Modelo.VolleySingleton;
import com.example.comercios.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
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

    GoogleMap mGoogleMap;
    MapView mapView;
    private ArrayList<Categorias> categorias;
    private TabLayout tabLayout;
    private final int MIS_PERMISOS = 100;
    private double latitud;
    private double longitud;

    public FragEmpresasMaps() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_empresas_maps, container, false);
        categorias = new ArrayList<>();
        cargarCategorias();
        tabLayout = (TabLayout)view.findViewById(R.id.fragEmpMap_menuNavigationTab);
        if(solicitaPermisosVersionesSuperioresGPS()){
            locationStart();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.mapa_contenedor);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        MapsInitializer.initialize(getActivity());
        mGoogleMap = googleMap;
        //mGoogleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //googleMap.setMapStyle(MapStyleOptions.lo)
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title("Estatua de la libertad").snippet("Espero ir un dia"));
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        CameraPosition camaraPosition =  CameraPosition.builder().target(new LatLng(latitud,longitud)).zoom(30).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camaraPosition));
    }

    private void cargarCategorias(){
        String url = Util.urlWebService + "/categoriasObtener.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonCategorias= response.getJSONArray("categoria");
                    JSONObject obj;
                    for(int i= 0;i<jsonCategorias.length();i++) {
                        obj = jsonCategorias.getJSONObject(i);
                        categorias.add(new Categorias(obj.getInt("id"),obj.getString("nombre")));
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

    private void cargarTabLayout(){
        if(categorias != null){
            TabLayout.Tab todos = tabLayout.newTab();
            todos.setText("Todos");
            todos.setIcon(recuperarIcono("Todos"));
            todos.setTag(-1);
            tabLayout.addTab(todos);
            for(Categorias c: categorias){
                TabLayout.Tab t = tabLayout.newTab();
                t.setText(c.getNombre());
                t.setIcon(recuperarIcono(c.getNombre()));
                t.setTag(c.getId());
                tabLayout.addTab(t);
            }
        }
    }
    private int recuperarIcono(String categoria){
        switch (categoria){
            case "Todos": return R.drawable.store_alt;
            case "Bar": return R.drawable.glass_martini_alt;
            case "Cafe": return R.drawable.coffee;
            case "Deportes": return R.drawable.bicycle;
            case "Farmacia": return R.drawable.capsules;
            case "Ferreteria": return R.drawable.hammer;
            case "Hotel": return R.drawable.hotel;
            case "Jugueteria": return R.drawable.robot;
            case "Libreria": return R.drawable.book;
            case "Musica": return R.drawable.guitar;
            case "Restaurante": return R.drawable.utensils;
            case "Ropa": return R.drawable.tshirt;
            case "Tecnologia": return R.drawable.laptop;
            case "Videojuegos": return R.drawable.gamepad;
            case "Zapateria": return R.drawable.shoe_prints;
            case "Otro": return R.drawable.shopping_cart;
            default: return -1;
        }
    }

    private void mensajeToast(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};

    private void cargarDialogoRecomendacionGPS() {
        androidx.appcompat.app.AlertDialog.Builder dialogo = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MIS_PERMISOS);
            }
        });
        dialogo.show();
    }

    private boolean solicitaPermisosVersionesSuperioresGPS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }
        //validamos si los permisos ya fueron aceptados
        if ((getActivity().checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && getActivity().checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if ((shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) || (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)))) {
            cargarDialogoRecomendacionGPS();
        } else {
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

            //String Text = loc.getLatitude() + "()" + loc.getLongitude();
            //cordenadas = Text;
            //this.mainActivity.setLocation(loc);
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

}
