@file:Suppress("DEPRECATION")

package com.app.androidassignment.helpers

import android.content.Context
import android.net.ConnectivityManager

object NetworkHandler {

    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


}