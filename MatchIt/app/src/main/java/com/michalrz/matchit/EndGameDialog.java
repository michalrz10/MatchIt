 package com.michalrz.matchit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class EndGameDialog extends AppCompatDialogFragment {

    private GameActivity listener;
    private int score;

    public EndGameDialog(int score)
    {
        this.score=score;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.end_game_layout, null);
        builder.setView(view)
                .setTitle("Wynik: "+String.valueOf(score))
                .setNegativeButton("unknown", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.returnNickname("unknown");
                    }
                })
                .setPositiveButton("zapisz", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.returnNickname(((TextView) view.findViewById(R.id.nickname)).getText().toString() );
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (GameActivity) context;
        } catch (ClassCastException e) {
            //pass
        }
    }
}
