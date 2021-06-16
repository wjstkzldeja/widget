package com.osl.swrnd.aos.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import androidx.core.view.updatePadding
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.osl.swrnd.aos.BR

class UiManager<V : ViewDataBinding, VM : ViewModel>(
  val viewDataBinding: V,
  val viewModel: VM
) {

  init {
    viewDataBinding.setVariable(BR.viewModel, viewModel)
    viewDataBinding.setVariable(BR.glide, Glide.with(viewDataBinding.root))
    viewDataBinding.executePendingBindings()
  }

  @SuppressLint("ObsoleteSdkInt")
  fun setNavigationBarSafeArea(view: View, activity: Activity?) {
    activity ?: return

    // Check whether the app is in multi-mode or not
    val isInMultiWindowMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      activity.isInMultiWindowMode
    } else {
      false
    }

    @Suppress("ControlFlowWithEmptyBody")
    if (!isInMultiWindowMode) {
      // Only when app is not in multi-mode
      val context: Context = activity.baseContext
      val showNavigationBarInt: Int = context.resources
        .getIdentifier("config_showNavigationBar", "bool", "android")

      // Check if the navigation bar is showing or not.
      // This would be true only if the application is showing the navigation bar in translucent mode
      var showNavigationBar =
        showNavigationBarInt > 0 && context.resources.getBoolean(showNavigationBarInt)

      // This is specific for the emulator since it always gives as false for showNavigationBar
      // Especially for Google Devices
      if (Build.FINGERPRINT.contains("generic")) showNavigationBar = true

      @Suppress("ControlFlowWithEmptyBody")
      if (showNavigationBar) {
        // Once we know navigation bar is showing we need to identify the height of navigation bar
        var navigationBarHeight: Int = context.resources
          .getIdentifier("navigation_bar_height", "dimen", "android")
        if (navigationBarHeight > 0) {
          navigationBarHeight = context.resources.getDimensionPixelSize(navigationBarHeight)
        }

        // Apply the height of navigation bar as a padding to the view which is the bottom-most view in your screen
        // Very important to do this in onCreate of the view else it would end up applying more padding from bottom
        view.updatePadding(bottom = navigationBarHeight)
      } else {
        // Ignore since navigation bar is not showing in translucent mode
      }
    } else {
      // Ignore since the application is in multi-mode
    }
  }
}
