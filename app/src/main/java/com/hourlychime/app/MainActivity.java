package com.hourlychime.app;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1001;
    private Button toggleButton;
    private TextView statusText;
    private boolean isServiceRunning = false;
    private ServiceStateReceiver serviceStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = findViewById(R.id.toggleButton);
        statusText = findViewById(R.id.statusText);

        toggleButton.setOnClickListener(v -> {
            if (isServiceRunning) {
                stopChimeService();
            } else {
                if (checkAndRequestPermissions()) {
                    startChimeService();
                }
            }
        });

        serviceStateReceiver = new ServiceStateReceiver();
        registerReceiver(serviceStateReceiver, new IntentFilter(ChimeService.ACTION_SERVICE_STATE));
    }

    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startChimeService();
            } else {
                Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startChimeService() {
        Intent serviceIntent = new Intent(this, ChimeService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void stopChimeService() {
        Intent serviceIntent = new Intent(this, ChimeService.class);
        stopService(serviceIntent);
    }

    private class ServiceStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ChimeService.ACTION_SERVICE_STATE.equals(intent.getAction())) {
                boolean running = intent.getBooleanExtra("running", false);
                isServiceRunning = running;
                updateUI();
            }
        }
    }

    private void updateUI() {
        if (isServiceRunning) {
            toggleButton.setText(R.string.disable_chime);
            statusText.setText(R.string.chime_enabled);
        } else {
            toggleButton.setText(R.string.enable_chime);
            statusText.setText(R.string.chime_disabled);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceStateReceiver != null) {
            unregisterReceiver(serviceStateReceiver);
        }
    }
}
