package com.example.listviewprueba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AdaptadorDeProductos extends BaseAdapter {
    private Context context;

    public AdaptadorDeProductos(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Productos.ITEMS.length;
    }

    @Override
    public Productos getItem(int position) {
        return Productos.ITEMS[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item, viewGroup, false);
        }

        ImageView imagenCoche = (ImageView) view.findViewById(R.id.imageItem2);
        TextView nombreCoche = (TextView) view.findViewById(R.id.imageItem2_txt3);
        TextView precio = (TextView) view.findViewById(R.id.imegeItem2_txt3);

        final Productos item = getItem(position);
        imagenCoche.setImageResource(item.getIdDrawable());
        nombreCoche.setText(item.getNombre());
        precio.setText(item.getPrecio());

        return view;
    }

}