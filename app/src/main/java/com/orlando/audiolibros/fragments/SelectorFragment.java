package com.orlando.audiolibros.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orlando.audiolibros.AdaptadorLibros;
import com.orlando.audiolibros.Aplicacion;
import com.orlando.audiolibros.Libro;
import com.orlando.audiolibros.MainActivity;
import com.orlando.audiolibros.R;

import java.util.Vector;

public class SelectorFragment extends Fragment {

    private Activity activity;
    private RecyclerView recyclerView;
    private AdaptadorLibros adaptadorLibros;
    private Vector<Libro> vectorLibros;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
            Aplicacion app = (Aplicacion) activity.getApplication();
            adaptadorLibros = app.getAdaptador();
            vectorLibros = app.getVectoLibros();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_selector, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));

        adaptadorLibros.setOnItemClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) activity).mostrarDetalle(recyclerView.getChildAdapterPosition(v));
            }
        });

        adaptadorLibros.setOnItemLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {
                final int id = recyclerView.getChildAdapterPosition(v);
                AlertDialog.Builder menu = new AlertDialog.Builder(activity);
                CharSequence[] opciones = {"Compartir", "Borrar", "Insertar"};
                menu.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Libro libro = vectorLibros.elementAt(id);
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(Intent.EXTRA_SUBJECT, libro.titulo);
                                i.putExtra(Intent.EXTRA_TEXT, libro.urlAudio);
                                startActivity(Intent.createChooser(i, "Compartir"));
                                break;
                            case 1:
                                Snackbar.make(v, "¿Estas seguro?", Snackbar.LENGTH_LONG)
                                        .setAction("SI", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                vectorLibros.remove(id);
                                                adaptadorLibros.notifyDataSetChanged();
                                            }
                                        })
                                        .show();
                                break;
                            case 2:
                                Snackbar.make(v, "Libro insertado", Snackbar.LENGTH_INDEFINITE)
                                        .show();
                                vectorLibros.add(vectorLibros.elementAt(id));
                                adaptadorLibros.notifyDataSetChanged();
                                break;
                        }
                    }
                });

                menu.create().show();
                return true;
            }
        });

        recyclerView.setAdapter(adaptadorLibros);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_selector, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_ultimo) {
            ((MainActivity)activity).irUltimoVisitado();
            return true;
        } else if(id == R.id.menu_buscar) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}