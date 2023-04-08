package com.example.checkaccessiblityservices

import androidx.appcompat.app.AppCompatActivity
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure
import android.provider.Settings.SettingNotFoundException
import android.view.accessibility.AccessibilityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var accessibilityManager: AccessibilityManager
    private lateinit var officialServicesTextView: TextView
    private lateinit var sideLoadingServicesTextView: TextView
    private lateinit var blackListTextView: TextView
    private lateinit var addToBlackListEditText: EditText
    private lateinit var addButton: Button
    private lateinit var blacklistedServicesDetectListTextView: TextView

    private var isAccessibilityEnabled = false
    private var officialServicesList: MutableList<String> = mutableListOf()
    private var sideLoadingServicesList: MutableList<String> = mutableListOf()
    private var blackList: MutableList<String> = mutableListOf()
    private var officialServicesSources = listOf("com.android.vending","com.huawei.appmarke","com.amazon.venezia","com.sec.android.app.samsungapps")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get references to UI elements
        accessibilityManager =
            getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        officialServicesTextView = findViewById(R.id.officialServicesListTextView)
        sideLoadingServicesTextView = findViewById(R.id.sideLoadingServicesListTextView)
        blackListTextView = findViewById(R.id.blacklistedServicesListTextView)
        addToBlackListEditText = findViewById(R.id.blacklistedServiceEditText)
        addButton = findViewById(R.id.blacklistButton)
        blacklistedServicesDetectListTextView = findViewById(R.id.blacklistedServicesDetectListTextView)

        // Set up the "Add" button to add a package name to the black list
        addButton.setOnClickListener {
            val packageName = addToBlackListEditText.text.toString().trim()
            if (!TextUtils.isEmpty(packageName)) {
                blackList.add(packageName)
                updateBlackList()
                addToBlackListEditText.setText("")
            }
        }

        // Continuously update the information while the application is running
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                // Update the accessibility status, official services, side-loading services, and black list
                updateAccessibilityStatus()
                updateOfficialServices()
                updateSideLoadingServices()
                val detectedServices = checkForBlackListedServices()
                var message = "No blacklisted services are enabled."
                if (detectedServices.isNotEmpty()) {
                    // If any blacklisted services are detected, show a warning message
                    message = "${
                        detectedServices.joinToString("\n")
                    }"
                }
                blacklistedServicesDetectListTextView.text = message
                handler.postDelayed(this, 1000)
            }
        })
    }

    // Update the status of Accessibility setting on the device
    private fun updateAccessibilityStatus() {
        isAccessibilityEnabled = accessibilityManager.isEnabled
        val statusTextView = findViewById<TextView>(R.id.accessibilitySettingStatusValueTextView)
        if (isAccessibilityEnabled) {
            statusTextView.text = "Accessibility Setting: Enabled"
        } else {
            statusTextView.text = "Accessibility Setting: Disabled"
        }
    }

    // Update the list of official Accessibility Services
    private fun updateOfficialServices() {
        val packageManager = packageManager
        val accessibilityServices = accessibilityManager.getEnabledAccessibilityServiceList(FEEDBACK_ALL_MASK)
        officialServicesList.clear()
        for (info in accessibilityServices) {
            val packageName = info.resolveInfo.serviceInfo.packageName
            val installerPackageName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                packageManager.getInstallSourceInfo(packageName).initiatingPackageName
            } else {
                packageManager.getInstallerPackageName(packageName)
            }
            if (officialServicesSources.contains(installerPackageName)) {
                val label = info.resolveInfo.loadLabel(packageManager)
                officialServicesList.add("App Name: ${label}\nPackage: $packageName\n")
            }
        }
        officialServicesTextView.text = "${officialServicesList.joinToString("\n")}"
    }

    // Update the list of side-loading Accessibility Services
    @SuppressLint("QueryPermissionsNeeded")
    private fun updateSideLoadingServices() {
        val packageManager = packageManager
        val accessibilityServices =
            accessibilityManager.getEnabledAccessibilityServiceList(FEEDBACK_ALL_MASK)
        sideLoadingServicesList.clear()
        for (info in accessibilityServices) {
            val packageName = info.resolveInfo.serviceInfo.packageName
            val installerPackageName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                packageManager.getInstallSourceInfo(packageName).initiatingPackageName
            } else {
                packageManager.getInstallerPackageName(packageName)
            }
            if (installerPackageName == null || !officialServicesSources.contains(installerPackageName)) {
                val label = info.resolveInfo.loadLabel(packageManager)
                sideLoadingServicesList.add("App Name: ${label}\nPackage: $packageName\n")
            }
        }
        sideLoadingServicesTextView.text =
            "${sideLoadingServicesList.joinToString("\n")}"
    }

    // Check for any blacklisted services that are enabled
    private fun checkForBlackListedServices(): List<String> {
        val enabledServices = mutableListOf<String>()
        val enabledServicesList =
            accessibilityManager.getEnabledAccessibilityServiceList(FEEDBACK_ALL_MASK)
        for (info in enabledServicesList) {
            val packageName = info.resolveInfo.serviceInfo.packageName
            if (blackList.contains(packageName)) {
                enabledServices.add(packageName)
            }
        }
        return enabledServices
    }

    // Update the list of black listed Accessibility Services
    private fun updateBlackList() {
        blackListTextView.text = "${blackList.joinToString("\n")}"
    }
}
