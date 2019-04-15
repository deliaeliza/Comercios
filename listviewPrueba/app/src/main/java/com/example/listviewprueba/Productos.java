package com.example.listviewprueba;

public class Productos {

    private String nombre;
    private int idDrawable;
    private int precio;

    public Productos(String nombre, int idDrawable, int prec) {
        this.nombre = nombre;
        this.idDrawable = idDrawable;
        this.precio = prec;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public int getPrecio() {
        return precio;
    }


    public int getId() {
        return nombre.hashCode();
    }

    public static Productos[] ITEMS = {
            new Productos("Jaguar F-Type 2015", R.drawable.artelec,145200),
            new Productos("Mercedes AMG-GT", R.drawable.artelec,1200),
            new Productos("Mazda MX-5", R.drawable.artelec,12030),
            new Productos("Porsche 911 GTS", R.drawable.artelec,12500),
            new Productos("BMW Serie 6", R.drawable.artelec,1200),
            new Productos("Ford Mondeo", R.drawable.artelec,12080),
            new Productos("Volvo V60 Cross Country", R.drawable.artelec,58747),
            new Productos("Jaguar XE", R.drawable.artelec,12500),
            new Productos("VW Golf R Variant", R.drawable.artelec,12800),
            new Productos("Seat León ST Cupra", R.drawable.artelec,12400),
    };

    /**
     * Obtiene item basado en su identificador
     *
     * @param id identificador
     * @return Coche
     */
    public static Productos getItem(int id) {
        for (Productos item : ITEMS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

}
