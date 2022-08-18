package com.app.androidassignment.helpers

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler

// used to handle coroutine exception
object CoroutineHandler {
    private const val TAG = "CoroutineHandler"
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "coroutineExceptionHandler: ", throwable)
    }
}