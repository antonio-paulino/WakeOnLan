package com.example.wakeonlan.objects

import java.util.regex.Pattern

object Pattern {
    private val ipPattern = Pattern.compile(
        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\$"
    )
    private val macPattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")


    fun isValidIpAddress(ip: String): Boolean {

        return ipPattern.matcher(ip).matches()
    }

    fun isValidMacAddress(mac: String): Boolean {
        return macPattern.matcher(mac).matches()
    }
}