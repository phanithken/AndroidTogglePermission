package com.example.togglepermission

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bluetoothToggle: ToggleButton = findViewById(R.id.bluetooth)
        val locationToggle: ToggleButton = findViewById(R.id.location)
        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // set onCheckListener to toggle
        bluetoothToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (bluetoothAdapter != null) {
                    // make change
                    if (!bluetoothAdapter.isEnabled) {
                        // enable
                        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        startActivityForResult(intent, 1)
                    } else {
                        Toast.makeText(this, "BLUETOOTH ALREADY ENABLE", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "DEVICE DOES NOT SUPPORT BLUETOOTH", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (bluetoothAdapter != null) {
                    // make change
                    if (bluetoothAdapter.isEnabled) {
                        // enable
                        bluetoothAdapter.disable()
                    } else {
                        Toast.makeText(this, "BLUETOOTH ALREADY DISABLE", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "DEVICE DOES NOT SUPPORT BLUETOOTH", Toast.LENGTH_SHORT).show()
                }
            }
        }

        locationToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // request permission
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 200)
                } else {
                    Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
