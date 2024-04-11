@file:Suppress("unused")
@file:JvmName("ContextUtil")

package com.yenaly.yenaly_libs.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.util.TypedValue
import androidx.activity.ComponentActivity
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.yenaly.yenaly_libs.ActivitiesManager

/**
 * Global application context.
 */
@set:JvmSynthetic
lateinit var applicationContext: Context
    internal set

/**
 * Get the application instance.
 */
val application get() = applicationContext as Application

/**
 * Extension property to get the Activity from a Context.
 */
val Context.activity: Activity?
    get() {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

/**
 * Extension function to find an Activity of a specific type from a Context.
 */
inline fun <reified T : Activity> Context.findActivity(): T {
    var context = this
    while (context is ContextWrapper) {
        if (context is T) {
            return context
        }
        context = context.baseContext
    }
    error("No activity of type ${T::class.java.simpleName} found")
}

/**
 * Extension function to get the ComponentActivity from a Context.
 * This is mainly used for AlertDialogs which require a Context with a window token.
 */
fun Context.requireComponentActivity() =
    (this.activity ?: ActivitiesManager.currentActivity) as ComponentActivity

/**
 * Extension property to get the Lifecycle from a Context.
 */
val Context.lifecycle: Lifecycle
    get() {
        var context: Context? = this
        while (true) {
            when (context) {
                is LifecycleOwner -> return context.lifecycle
                !is ContextWrapper -> error("This should never happen!")
                else -> context = context.baseContext
            }
        }
    }

/**
 * Extension function to get a theme color from a Context.
 */
@ColorInt
fun Context.getThemeColor(@AttrRes attrColor: Int): Int {
    return TypedValue().apply {
        theme.resolveAttribute(attrColor, this, true)
    }.data
}