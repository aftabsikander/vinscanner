package com.app.advanceautoparts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;

import com.pixotech.android.scanner.library.ScannerActivity;

import butterknife.BindView;

public class MainActivity extends ScannerActivity {

    @Nullable
    @BindView(R.id.vinTextView)
    public AppCompatTextView vinTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayout(R.layout.activity_main);
    }

    @Override
    public void handleDecode(String s) {
        vibrate();
        vinTextView.setText(s);
        startScan();
    }
}