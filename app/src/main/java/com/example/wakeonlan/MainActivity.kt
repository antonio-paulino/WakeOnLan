package com.example.wakeonlan

import com.example.wakeonlan.objects.Pattern
import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wakeonlan.objects.WakeOnLan
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var ipText: EditText
    private lateinit var macText: EditText
    private lateinit var sharedPreferences: SharedPreferences

    private val REQUEST_CODE_PERMISSIONS = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ipText = findViewById(R.id.ipText)
        macText = findViewById(R.id.macText)
        button = findViewById(R.id.button)
        sharedPreferences = getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)

        ipText.setText(sharedPreferences.getString("savedIP", ""))
        macText.setText(sharedPreferences.getString("savedMAC", ""))

        println(ipText)
        println(macText)
        if (!hasPermissions()) {
            requestPermissions()
        }


        button.setOnClickListener {
            val ip = ipText.text.toString()
            val mac = macText.text.toString()


            if (!Pattern.isValidIpAddress(ip)) {
                showSnackBar("Invalid IP address format")
            } else if (!Pattern.isValidMacAddress(mac)) {
                showSnackBar("Invalid MAC address format")
            } else {
                with(sharedPreferences.edit()) {
                    putString("savedIP", ip)
                    putString("savedMAC", mac)
                    apply()
                }

                if (hasPermissions()) {
                    executeNetworkOperations(ip, mac)
                } else {
                    showSnackBar("Permissions not granted")
                }


            }

        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun executeNetworkOperations(ip: String, mac: String) {

        val thread = Thread {
            try {
                WakeOnLan.main(arrayOf(ip, mac))
                runOnUiThread {
                    showSnackBar("Wake-on-Lan packet sent successfully")
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                runOnUiThread {
                    showSnackBar("Invalid argument: ${e.message}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    showSnackBar("Failed to send Wake-on-Lan packet: ${e.message}")
                }
            }

        }
        thread.start()
    }


    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.INTERNET),
            REQUEST_CODE_PERMISSIONS
        )
    }

}