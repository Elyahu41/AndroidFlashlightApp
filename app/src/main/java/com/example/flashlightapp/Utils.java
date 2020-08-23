package com.example.flashlightapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

class Utils {

    public final static int sREQUEST_CODE_SETTINGS = 100, sREQUEST_CODE_LOCATION_PERMISSION = 100;

    public static void getLocationPermission (Activity activity, int code)
    {
        // Here, activity is the current activity
        if (ContextCompat.checkSelfPermission (activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (activity, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, code);
        }
    }

    private static void showAlertDialog (Context context, String strTitle, String strMsg,
                                         DialogInterface.OnClickListener okListener,
                                         DialogInterface.OnClickListener cancelListener)
    {
        AlertDialog.Builder alertDialogBuilder = getDialogBasicsADB (context, strTitle, strMsg);

        if (okListener == null) {
            // create an OK button only and use a dummy listener for that button and the dialog
            alertDialogBuilder.setNeutralButton (context.getString (android.R.string.ok),
                    getNewEmptyOnClickListener ());
        }
        else {
            alertDialogBuilder.setPositiveButton (context.getString (android.R.string.ok),
                    okListener);
            alertDialogBuilder.setNegativeButton (context.getString (android.R.string.cancel),
                    cancelListener);
        }

        // Create and Show the Dialog
        alertDialogBuilder.show ();
    }

    public static DialogInterface.OnClickListener getNewEmptyOnClickListener ()
    {
        return new DialogInterface.OnClickListener ()
        {
            @Override public void onClick (DialogInterface dialog, int which)
            {

            }
        };
    }

    @NonNull
    private static AlertDialog.Builder getDialogBasicsADB (Context context, String strTitle,
                                                           String strMsg)
    {
        // Create the AlertDialog.Builder object
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (context);

        // Use the AlertDialog's Builder Class methods to set the title, icon, message, et al.
        // These could all be chained as one long statement, if desired
        alertDialogBuilder.setTitle (strTitle);
        alertDialogBuilder.setMessage (strMsg);
        alertDialogBuilder.setIcon (ContextCompat.getDrawable (context, R.mipmap.ic_launcher));
        alertDialogBuilder.setCancelable (true);
        return alertDialogBuilder;
    }

    public static void showInfoDialog (Context context, int titleID, int msgID)
    {
        showInfoDialog (context, context.getString (titleID), context.getString (msgID));
    }

    @SuppressWarnings ("WeakerAccess")
    public static void showInfoDialog (Context context, String strTitle, String strMsg)
    {
        showAlertDialog (context, strTitle, strMsg);
    }

    @SuppressWarnings ("WeakerAccess")
    public static void showOkCancelDialog (Context context, String strTitle, String strMsg,
                                           DialogInterface.OnClickListener okListener,
                                           DialogInterface.OnClickListener cancelListener)
    {
        showAlertDialog (context, strTitle, strMsg, okListener, cancelListener);
    }

    private static void showAlertDialog (Context context, String strTitle, String strMsg)
    {
        showAlertDialog (context, strTitle, strMsg, null, null);
    }
}
