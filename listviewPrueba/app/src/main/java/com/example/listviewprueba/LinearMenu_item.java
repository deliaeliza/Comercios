package com.example.listviewprueba;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LinearMenu_item extends Fragment {

    private TextView nombre;
    private TextView image;
    private ConstraintLayout panel;
    private Categoria categoria = null;
    public LinearMenu_item() {
        // Required empty public constructor
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_linear_menu_item, container, false);
        nombre = (TextView) view.findViewById(R.id.linearMenu_name);
        image = (TextView) view.findViewById(R.id.linearMenu_image);
        panel = (ConstraintLayout) view.findViewById(R.id.linearMenu_panel);
        image.setTypeface(FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.FONTAWESOME_SOLID));
        panel.setTag(categoria);
        image.setText(categoria.getFont());
        nombre.setText(categoria.getNombre());
        panel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Categoria cat = (Categoria) panel.getTag();
                if(categoria != null){
                    //panel.setBackgroundColor(Color.LTGRAY);
                    mensaje(categoria.getNombre());

                    if(categoria.getNombre().equalsIgnoreCase("Restaurante")) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        BlankFragment01 mifrag = new BlankFragment01();
                        fragmentTransaction.replace(R.id.linearMenu_content, mifrag, "id");
                        fragmentTransaction.commit();
                    } else {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        BlankFragment02 mifrag = new BlankFragment02();
                        fragmentTransaction.replace(R.id.linearMenu_content, mifrag, "id");
                        fragmentTransaction.commit();
                    }
                } else {
                    mensaje("La categoria no puede ser nula");
                }
            }
        });
        return view;
        //return inflater.inflate(R.layout.fragment_linear_menu_item, container, false);
    }

    public void mensaje(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};

}
