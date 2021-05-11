package com.snstudio.smoothpdfviewer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Details Dialog
 */

public class ViewDialog {

    @SuppressLint("SetTextI18n")
    public void showDialog(Activity activity, ArrayList<String> detailsList) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        ListView listView = dialog.findViewById(R.id.details_list_view);
        DetailsListAdapter adapter = new DetailsListAdapter(detailsList, activity.getApplicationContext());
        listView.setAdapter(adapter);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

}