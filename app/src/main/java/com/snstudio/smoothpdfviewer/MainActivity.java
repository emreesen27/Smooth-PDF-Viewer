package com.snstudio.smoothpdfviewer;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.shawnlin.numberpicker.NumberPicker;
import com.shockwave.pdfium.PdfDocument;
import com.snstudio.smoothpdfviewer.settings.SettingsActivity;
import com.snstudio.smoothpdfviewer.settings.SettingsUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    private Menu menu;
    private PDFView pdfView;
    private final String TAG = "APP_INFO";
    private final static int REQUEST_CODE = 42;
    private final static int PERMISSION_CODE = 42042;
    private final static int SETTINGS_REQUEST_CODE = 58058;
    private final static String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private FrameLayout actionLayout;
    private TextView pageNumber;
    private ArrayList<String> detailsMap;
    private LinearLayout warningLayout;
    private Uri uri;
    private int pageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineView();
        handleIntent();
        defineActionBar();
        checkPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.open_pdf) {
            launchPicker();
        } else if (item.getItemId() == R.id.settings) {
            goSettings();
        } else if (item.getItemId() == R.id.hide_or_show_button) {
            hideOrShowActionButton();
        } else if (item.getItemId() == R.id.details) {
            ViewDialog alert = new ViewDialog();
            alert.showDialog(this, detailsMap);
        }
        return true;
    }

    private void handleIntent() {
        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            uri = intent.getData();
            createPdfViewer(uri);
        } else if (Intent.ACTION_MAIN.equals(action)) {
            Log.i(TAG, action);
        } else {
            showSnackBar(getString(R.string.err_opening_file), true);
        }
    }


    private void defineView() {
        pdfView = findViewById(R.id.pdf_view);
        actionLayout = findViewById(R.id.action_layout);
        pageNumber = findViewById(R.id.tv_page_number);
        warningLayout = findViewById(R.id.warning);
        detailsMap = new ArrayList<>();
    }

    private void defineActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().
                    getColor(R.color.action_bar_color, getTheme())));
            Spannable title = new SpannableString(actionBar.getTitle());
            title.setSpan(new ForegroundColorSpan(getResources()
                            .getColor(R.color.main_text_color, getTheme())),
                    0, title.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(title);
        }
    }

    private void createPdfViewer(Uri uri) {
        boolean nightMode = SettingsUtils.resolveNightMode(this);
        boolean pageFling = !SettingsUtils.resolveFling(this);
        boolean antiAliasing = SettingsUtils.resolveAntiAliasing(this);
        boolean pageSwipe = SettingsUtils.resolvePageSwipe(this);
        boolean pageSnap = SettingsUtils.resolvePageSnap(this);

        pdfView.fromUri(uri)
                .enableSwipe(true)
                .swipeHorizontal(pageSwipe)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false)
                .enableAntialiasing(antiAliasing)
                .spacing(0)
                .autoSpacing(false)
                .pageFitPolicy(FitPolicy.BOTH)
                .fitEachPage(true)
                .pageSnap(pageSnap)
                .pageFling(pageFling)
                .nightMode(nightMode)
                .onLoad(this)
                .onPageChange(this)
                .onPageError(this)
                .load();
    }

    private void checkPermission() {
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

    private void goSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        intent.putExtra("pdfUri", uri == null ? (String) null : uri.toString());
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    }

    private void launchPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.file_manager_not_working), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "File Picker Error");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
            String strUri = Objects.requireNonNull(data).getStringExtra("pdfUri");
            uri = Uri.parse(strUri);
            createPdfViewer(uri);
            return;
        }
        if (resultCode == RESULT_OK) {
            uri = Objects.requireNonNull(data).getData();
            createPdfViewer(uri);
        }
    }

    public void actionButtonClick(View view) {
        if (pageCount == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_selected_pdf), Toast.LENGTH_LONG).show();
            return;
        }
        final BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
        dialog.setContentView(R.layout.bottom_sheet);
        dialog.setCanceledOnTouchOutside(true);
        NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.number_picker);
        if (numberPicker != null) {
            numberPicker.setMaxValue(pageCount);
            numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> pdfView.jumpTo(newVal, true));
        }
        dialog.show();
    }

    public void openButtonClick(View view) {
        launchPicker();
    }

    private void startAnimation(int state) {
        actionLayout.animate()
                .translationY(state == 0 ? actionLayout.getHeight() : 0)
                .alpha(state == 0 ? 0 : 1.0f)
                .setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                menu.findItem(R.id.hide_or_show_button).setChecked(state != 1);
                super.onAnimationEnd(animation);
            }
        });
    }

    private void hideOrShowActionButton() {
        if (actionLayout.getAlpha() == 1.0)
            startAnimation(0);
        else
            startAnimation(1);
    }

    private void showSnackBar(@NonNull String msg, boolean isError) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main_layout), msg, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(ContextCompat
                .getColor(Objects.requireNonNull(getApplicationContext()),
                        isError ? R.color.err_color : R.color.main_color));
        snackbar.setTextColor(ContextCompat
                .getColor(Objects.requireNonNull(getApplicationContext()), R.color.white));
        snackbar.show();
    }

    @Override
    public void loadComplete(int nbPages) {
        pageCount = nbPages;
        warningLayout.setVisibility(View.GONE);
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        detailsMap.clear();
        detailsMap.add(meta.getTitle());
        detailsMap.add(meta.getAuthor());
        detailsMap.add(meta.getSubject());
        detailsMap.add(meta.getKeywords());
        detailsMap.add(meta.getCreator());
        detailsMap.add(meta.getProducer());
        detailsMap.add(dateFormatter(meta.getCreationDate()));
        detailsMap.add(dateFormatter(meta.getModDate()));
    }

    private String dateFormatter(String data) {
        if (!data.isEmpty()) {
            String date = data.substring(2);
            String y = date.substring(0, 4);
            String m = date.substring(4, 6);
            String d = date.substring(6, 8);
            return y + "/" + m + "/" + d;
        } else {
            return data;
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber.setText(String.valueOf(page + 1));
    }

    @Override
    public void onPageError(int page, Throwable t) {
        showSnackBar(getString(R.string.page_not_loaded) + page, true);
    }
}