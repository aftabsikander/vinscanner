package com.app.advanceautoparts;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.pixotech.android.scanner.library.ScannerActivity;

import io.fabric.sdk.android.Fabric;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends ScannerActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";

    private static final int RC_CAMERA_PERM = 123;
    @Nullable
    @BindView(R.id.vinTextView)
    public AppCompatTextView vinTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        addLayout(R.layout.activity_main);
        ButterKnife.bind(this);
        cameraTask();
    }

    @Override
    public void handleDecode(String s) {
        vibrate();
        vinTextView.setText(s);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        startScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,
                this);
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (hasCameraPermission()) {
            startScan();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_camera),
                    RC_CAMERA_PERM,
                    Manifest.permission.CAMERA);
        }
    }

    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        startScan();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasCameraPermission()) {
                cameraTask();
            }
        }
    }

}