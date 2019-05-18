package com.example.comercios.Fragments;


        import android.graphics.Bitmap;
        import android.icu.util.ValueIterator;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.ImageRequest;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.example.comercios.Global.GlobalComercios;
        import com.example.comercios.Global.GlobalUsuarios;
        import com.example.comercios.Modelo.Categorias;
        import com.example.comercios.Modelo.Util;
        import com.example.comercios.Modelo.VolleySingleton;
        import com.example.comercios.R;
        import com.jaredrummler.materialspinner.MaterialSpinner;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.math.BigDecimal;
        import java.math.RoundingMode;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;
        import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragHomeComercio extends Fragment {

    public FragHomeComercio() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mensajeAB(Util.nombreApp);
        View view = inflater.inflate(R.layout.frag_home_comercio, container, false);
        GlobalComercios.getInstance().setVentanaActual(R.layout.frag_home_comercio);
        ImageView fotoComercioHome = (ImageView) view.findViewById(R.id.FHomComercio_ImgLocal);
        ImageView verificado = (ImageView) view.findViewById(R.id.FHomComercio_verificado);
        TextView usuario = (TextView) view.findViewById(R.id.FHomComercio_viewUsuario);
        TextView descripcion =(TextView) view.findViewById(R.id.FHomComercio_viewDescripcion);
        TextView categoria = (TextView)view.findViewById(R.id.FHomComercio_viewCategoria);
        TextView telefono = (TextView) view.findViewById(R.id.FHomComercio_viewTelefono);
        TextView cantVotos = (TextView) view.findViewById(R.id.FHomComercio_cantVotos);
        RatingBar ratingBarCali = (RatingBar)view.findViewById(R.id.FHomComercio_ratingBar);

        usuario.setText(GlobalComercios.getInstance().getComercio().getUsuario());
        categoria.setText(GlobalComercios.getInstance().getComercio().getCategoria());
        descripcion.setText(GlobalComercios.getInstance().getComercio().getDescripcion());

        if(!GlobalComercios.getInstance().getComercio().isVerificado()){
            verificado.setVisibility(View.GONE);
        }

        if(GlobalComercios.getInstance().getComercio().getTelefono() != -1)
            telefono.setText(Long.toString(GlobalComercios.getInstance().getComercio().getTelefono()));
        else{
            telefono.setVisibility(View.GONE);
        }

        if(GlobalComercios.getInstance().getComercio().getUrlImagen() != null && GlobalComercios.getInstance().getComercio().getUrlImagen() != ""){
            cargarWebServicesImagen(GlobalComercios.getInstance().getComercio().getUrlImagen(), fotoComercioHome);
        }
        ratingBarCali.setRating(GlobalComercios.getInstance().getComercio().getCalificacion());
        cantVotos.setText(GlobalComercios.getInstance().getComercio().getCantCalificaciones() + " Votos");
        return view;
    }

    private void cargarWebServicesImagen(String ruta_foto, final ImageView imageView) {
        ImageRequest imagR = new ImageRequest(ruta_foto, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Error al cargar la imagen");
            }
        });
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(imagR);
    }
    private void mensajeToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    private void mensajeAB(String msg){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(msg);};
}
