package com.snstudio.smoothpdfviewer;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private PDFView pdfView;
    private final static int REQUEST_CODE = 42;
    private final static int PERMISSION_CODE = 42042;
    private final static String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        defineView();
    }

    void defineView() {
        actionButton = findViewById(R.id.action_button);
    }

    void createPdfViewer(Uri uri) {
        pdfView = findViewById(R.id.pdfView);
        pdfView.fromUri(uri)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false)
                .enableAntialiasing(true)
                .spacing(0)
                .autoSpacing(false)
                .pageFitPolicy(FitPolicy.BOTH)
                .fitEachPage(false)
                .pageSnap(false)
                .pageFling(true)
                .nightMode(false)
                .load();
    }

    void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }

    void launchPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "File manager not working", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = Objects.requireNonNull(data).getData();
            createPdfViewer(uri);
        }
    }

    public void actionButtonClick(View view) {
        launchPicker();
    }

}