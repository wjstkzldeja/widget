package com.osl.swrnd.aos.views.container.home

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import androidx.activity.viewModels
import com.osl.swrnd.aos.App
import com.osl.swrnd.aos.R
import com.osl.swrnd.aos.databinding.ActivityHomeBinding
import com.osl.swrnd.aos.views.base.BaseActivity

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {
  override val navRes: Int = R.id.nav_host
  override val layoutRes: Int get() = R.layout.activity_home
  override val viewModel: HomeViewModel by viewModels { (application as App).viewModelFactory }

  override fun initViews(savedInstanceState: Bundle?) {
    val appWidgetManager = AppWidgetManager.getInstance(this)
  }

  override fun addObservers() {
  }
}
