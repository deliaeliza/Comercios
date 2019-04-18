package com.example.comercios.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comercios.Adapter.viewPagerAdapter;
import com.example.comercios.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.viewpager.widget.ViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragProductoResgistrar extends Fragment {

    private TextInputEditText nombre, descripcion, precio;


    public FragProductoResgistrar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.frag_producto_resgistrar, container, false);

        ViewPager viewpager = (ViewPager) view.findViewById(R.id.fRegProd_viewPager);
        viewPagerAdapter vie = new viewPagerAdapter(getActivity());
        viewpager.setAdapter(vie);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setPageMargin(-120);
        return view;
    }

}
