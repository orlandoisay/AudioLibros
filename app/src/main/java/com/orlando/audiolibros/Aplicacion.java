package com.orlando.audiolibros;

import android.app.Application;

import java.util.Vector;

public class Aplicacion extends Application {

    private Vector<Libro> vectoLibros;
    private AdaptadorLibros adaptador;

    @Override
    public void onCreate() {
        super.onCreate();
        vectoLibros = Libro.ejemploLibros();
        adaptador = new AdaptadorLibros(this, vectoLibros);
    }

    public AdaptadorLibros getAdaptador() {
        return adaptador;
    }

    public Vector<Libro> getVectoLibros() {
        return vectoLibros;
    }

}
