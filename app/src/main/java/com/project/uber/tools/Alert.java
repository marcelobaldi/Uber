package com.project.uber.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.project.uber.R;

public class Alert {
    private Runnable p_true = null, n_true = null;
    public void AlertMessage(Context context, String msg){
        AlertDialog.Builder a = new AlertDialog.Builder(context);
        a.setTitle(R.string.app_name);
        a.setNeutralButton("OK", null);
        a.setPositiveButton(null,null);
        a.setNegativeButton(null,null);
        a.setMessage(msg);
        a.create();
    }
    public boolean AlertConfirmation(Context context, String msg, final String pb, String nb, Runnable pb_true, Runnable nb_true){
        p_true = pb_true;
        n_true = nb_true;
        AlertDialog.Builder a = new AlertDialog.Builder(context);
        a.setTitle(R.string.app_name);
        a.setMessage(msg);
        a.setPositiveButton(pb, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                p_true.run();
            }
        });
        a.setNegativeButton(nb, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                n_true.run();
            }
        });
        a.create();
        return true;
    }
}
