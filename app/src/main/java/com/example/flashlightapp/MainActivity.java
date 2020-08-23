package com.example.flashlightapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import static com.example.flashlightapp.Utils.sREQUEST_CODE_SETTINGS;

public class MainActivity extends AppCompatActivity {

    private final int CAMERA_REQUEST_CODE = 2;
    boolean hasCameraFlash = false;
    private boolean isFlashOff = false;
    RelativeLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupFlashlightButton();
        setupNightMode();
        hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        background = findViewById(R.id.background);
        askPermission(Manifest.permission.CAMERA,CAMERA_REQUEST_CODE);
    }

    public void onResume() {
        super.onResume();
        // set background color based on preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int bgc = Color.parseColor(sharedPref.getString("background_color", "#333232"));
        background.setBackgroundColor(bgc);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFlashlightButton() {
        FloatingActionButton flashLightButton = findViewById(R.id.fab);
        flashLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraFlash) {
                    if (isFlashOff) {
                        Snackbar.make(view,"Flashlight is Off!", Snackbar.LENGTH_LONG).show();
                        flashLightOnOff(false);
                        isFlashOff = false;
                    } else {
                        Snackbar.make(view,"Flashlight is On!", Snackbar.LENGTH_LONG).show();
                        flashLightOnOff(true);
                        isFlashOff = true;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No flash available on your device",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void flashLightOnOff(boolean turnOn) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = Objects.requireNonNull(cameraManager).getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, turnOn);
        } catch (CameraAccessException ignored) {
        }
    }

    private void askPermission(String permission,int requestCode) {
        if (ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
            // We Don't have permission
            ActivityCompat.requestPermissions(this,new String[]{permission},requestCode);
        }
    }

    private void setupNightMode() {
        int nightModeOn = Build.VERSION.SDK_INT >= 28 ? AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM : AppCompatDelegate.MODE_NIGHT_AUTO_TIME;
        AppCompatDelegate.setDefaultNightMode (nightModeOn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSettings(MenuItem item) {
        Intent intent = new Intent (getApplicationContext (), SettingsActivity.class);
        startActivityForResult (intent, sREQUEST_CODE_SETTINGS);
    }

    public void showAbout (MenuItem item)
    {
        Utils.showInfoDialog (MainActivity.this, R.string.about_dialog_title,
                R.string.about_dialog_banner);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("FlashState", isFlashOff); //save whether the flash is on
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getBoolean("FlashState", isFlashOff);
    }
}
