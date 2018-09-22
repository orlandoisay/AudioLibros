package com.orlando.audiolibros.fragments;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.orlando.audiolibros.Aplicacion;
import com.orlando.audiolibros.Libro;
import com.orlando.audiolibros.MainActivity;
import com.orlando.audiolibros.R;

import java.io.IOException;

public class DetalleFragment extends Fragment implements View.OnTouchListener,
        MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    public static String ARG_ID_LIBRO = "id_libro";
    MediaPlayer mediaPlayer;
    MediaController mediaController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle, container, false);
        Bundle args = getArguments();
        if(args != null) {
            int position = args.getInt(ARG_ID_LIBRO);
            ponInfoLibro(position, view);
        } else {
            ponInfoLibro(0, view);
        }
        return view;
    }

    private void ponInfoLibro(int id, View view) {
        Libro libro = ((Aplicacion)getActivity().getApplication()).getVectoLibros().elementAt(id);
        ((TextView)view.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView)view.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView)view.findViewById(R.id.portada)).setImageResource(libro.recursoImagen);

        view.setOnTouchListener(this);

        if(mediaPlayer != null)
            mediaPlayer.release();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(getActivity());
        Uri audio = Uri.parse(libro.urlAudio);
        try {
            mediaPlayer.setDataSource(getActivity(), audio);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("AudioLibros", "ERROR: No se puede reproducir " + audio, e);
            Toast.makeText(getActivity(),"ERROR", Toast.LENGTH_SHORT);
        }
    }

    public void ponInfoLibro(int id) {
        ponInfoLibro(id, getView());
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("AudioLibros", "Entramos en onPrepared");
        mediaPlayer.start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getView());
        mediaController.setEnabled(true);
        mediaController.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mediaController.show();
        return false;
    }


    @Override
    public void onStop() {
        mediaController.hide();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d("AudioLibros", "Error en mediaPlayer.stop()");
        }
        super.onStop();
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        try {
            return mediaPlayer.getCurrentPosition();
        }
        catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    @Override
    public void onResume() {
        DetalleFragment fragment =
                (DetalleFragment)getFragmentManager()
                        .findFragmentById(R.id.detalle_fragment);

        if (fragment == null) {
            ((MainActivity)getActivity()).mostrarElementos(false);
        }
        super.onResume();
    }
}
