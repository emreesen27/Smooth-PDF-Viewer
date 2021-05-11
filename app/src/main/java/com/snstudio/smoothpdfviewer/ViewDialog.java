package com.snstudio.smoothpdfviewer;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Details Dialog
 */

public class ViewDialog {

    public void showDialog(Activity activity, ArrayList<String> detailsList) {
        if (detailsList.size() == 0) {
            Toast.makeText(activity.getApplicationContext(), "There is no open pdf", Toast.LENGTH_LONG).show();
            return;
        }

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