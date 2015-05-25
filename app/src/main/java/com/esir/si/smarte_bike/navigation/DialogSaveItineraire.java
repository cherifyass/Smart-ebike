package com.esir.si.smarte_bike.navigation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.esir.si.smarte_bike.R;



/**
 * Dialog permettant de sauvegarder le trajet en cours
 * Created by Loann on 25/05/2015.
 */
public class DialogSaveItineraire extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_text)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Navigation.saveItinerary();
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel(); // close dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
