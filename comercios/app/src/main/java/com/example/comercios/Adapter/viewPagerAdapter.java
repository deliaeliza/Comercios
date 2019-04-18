package com.example.comercios.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.comercios.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class viewPagerAdapter extends PagerAdapter {

    private Context context;
    private Integer[] imagenes = {R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera};

    public viewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagenes.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageResource(imagenes[position]);
        container.addView(img,0);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((ImageView) object);
    }
}
