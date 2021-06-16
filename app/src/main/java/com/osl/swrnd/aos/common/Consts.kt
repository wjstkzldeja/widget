package com.osl.swrnd.aos.common

import android.content.res.Resources
import android.view.View
import android.view.WindowManager
import java.util.regex.Pattern


const val CACHE_PARENT = "MEDIA_CACHE"

const val FLAG_FULLSCREEN =
  View.SYSTEM_UI_FLAG_LOW_PROFILE or
    View.SYSTEM_UI_FLAG_FULLSCREEN or
    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

const val FLAG_DISPLAY_CUTOUT_SHORT_EDGES =
  WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

const val FLAG_TRANSPARENT_DARK_MODE =
  View.SYSTEM_UI_FLAG_LOW_PROFILE or
    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

val DEVICE_MAC_ADDR_PATTERN: Pattern =
  "98:[dD]3:[cC]1:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}".let {
  Pattern.compile(it, Pattern.CASE_INSENSITIVE)
}

val NICKNAME_PATTERN: Pattern = ("[a-zA-Z0-9ㄱ-힇]{2,6}").let {
  Pattern.compile(it)
}

val NAME_PATTERN = NICKNAME_PATTERN

val AGE_PATTERN: Pattern = ("[0-9]{1,2}").let {
  Pattern.compile(it)
}

val EMAIL_PATTERN: Pattern = ("([a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
  "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
  "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
  ")+)").let {
  Pattern.compile(it)
}

val PHONE_PATTERN = "\\d{2,3}-?\\d{3,4}-?\\d{4}$".let {
  Pattern.compile(it)
}

val PASSWORD_PATTERN: Pattern = "([a-zA-Z\\d]{6,20})".let {
  Pattern.compile(it)
}

val Float.dp: Float get() = this * Resources.getSystem().displayMetrics.density
val Int.dp: Float get() = toFloat().dp