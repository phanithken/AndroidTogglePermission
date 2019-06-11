package com.example.togglepermission

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import android.widget.ToggleButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        FirebaseMessaging.getInstance().unsubscribeFromTopic("GENERAL")
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FirebaseInstance", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID Token
                val token = task.result?.token

                // Log
                Log.d("TOKEN", token)
            })

        val bluetoothToggle: ToggleButton = findViewById(R.id.bluetooth)
        val locationToggle: ToggleButton = findViewById(R.id.location)
        val notificationToggle: ToggleButton = findViewById(R.id.notification)

        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // set onCheckListener to toggle
        notificationToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("GENERAL")
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("GENERAL")
            }
        }

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
