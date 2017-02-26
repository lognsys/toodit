package com.lognsys.toodit.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.LoginFilter;

import com.lognsys.toodit.LoginActivity;


/**
 * Created by pdoshi on 22/02/17.
 */

public class NetworkStatusDialog extends DialogFragment {
    public static final String ARG_TITLE = "title";
    public static final String ARG_MSG = "message";
    public static final String ARG_INTENT = "intent";


    public NetworkStatusDialog() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString(ARG_TITLE, "");
        String message = args.getString(ARG_MSG, "");
        final Intent i = args.getParcelable(ARG_INTENT);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                        dialog.dismiss();
                    }
                }).create();
    }
}
