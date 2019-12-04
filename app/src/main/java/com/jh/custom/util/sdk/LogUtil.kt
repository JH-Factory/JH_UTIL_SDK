package com.mocoplex.shockping.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log

object LogUtil {
    private val TAG = "LogUtil"
    private var DEBUG = false

    fun init(context: Context) {
        try {
            val appinfo = context.packageManager.getApplicationInfo(context.packageName, 0)
            DEBUG = 0 != appinfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {

        }
    }

    /**
     * Log Level Error
     */
    fun e(message: String) {
        if (DEBUG) Log.e(TAG, buildLogMsg(message))
    }

    /**
     * Log Level Error (Throwable)
     */
    fun e(message: String, error: Throwable) {
        if (DEBUG) Log.e(TAG, buildLogMsg(message), error)
    }

    /**
     * Log Level Warning
     */
    fun w(message: String) {
        if (DEBUG) Log.w(TAG, buildLogMsg(message))
    }

    /**
     * Log Level Information
     */
    fun i(message: String) {
        if (DEBUG) Log.i(TAG, buildLogMsg(message))
    }

    /**
     * Log Level Debug
     */
    fun d(message: String) {
        if (DEBUG) Log.d(TAG, buildLogMsg(message))
    }

    /**
     * Log Level Verbose
     */
    fun v(message: String) {
        if (DEBUG) Log.v(TAG, buildLogMsg(message))
    }

    /**
     * Log Long String
     * Log Level Information
     */
    fun l(message: String) {
        if (!DEBUG) return

        if (message.length > 3000) {
            Log.i(TAG, buildLogMsg(message.substring(0, 3000)))
            l(message.substring(3000))
        } else {
            Log.i(TAG, buildLogMsg(message))
        }
    }

    fun buildLogMsg(message: String): String {

        val ste = Thread.currentThread().stackTrace[4]

        val sb = StringBuilder()

        sb.append("[ ")
        sb.append(ste.fileName.replace(".java", ""))
        sb.append(" :: ")
        sb.append(ste.methodName)
        sb.append(" ] ")
        sb.append(message)

        return sb.toString()
    }
}