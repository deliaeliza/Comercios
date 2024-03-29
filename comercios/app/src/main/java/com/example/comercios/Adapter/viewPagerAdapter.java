package com.example.comercios.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.example.comercios.Global.GlobalComercios;
import com.example.comercios.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class viewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Bitmap> imagenes;
    private View item;
    private int id;
    public viewPagerAdapter(Context context, ArrayList<Bitmap> imagenes) {
        this.context = context;
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return imagenes.size();
    }

    @Override
    public int getItemPosition(Object object){ return POSITION_NONE; }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        ImageView img = new ImageView(context);

        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(item != null){
                    item.findViewById(id).performClick();
                }
            }// fin del onclick
        });
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageBitmap(imagenes.get(position));
        container.addView(img,0);
        GlobalComercios.getInstance().setImgActual(position);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((ImageView) object);
    }
    public void setItem(View item){
        this.item = item;
    }
    public void setId(int id){
        this.id = id;
    }

    public ArrayList<Bitmap> getImagenes() {
        return imagenes;
    }
}

