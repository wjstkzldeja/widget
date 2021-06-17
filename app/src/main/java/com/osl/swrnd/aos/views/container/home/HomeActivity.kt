package com.osl.swrnd.aos.views.container.home

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.osl.swrnd.aos.App
import com.osl.swrnd.aos.AppWidget
import com.osl.swrnd.aos.R
import com.osl.swrnd.aos.databinding.ActivityHomeBinding
import com.osl.swrnd.aos.views.base.BaseActivity

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {
    override val navRes: Int = R.id.nav_host
    override val layoutRes: Int get() = R.layout.activity_home
    override val viewModel: HomeViewModel by viewModels { (application as App).viewModelFactory }

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.tvTest.setOnClickListener {
            send()
        }
        ui.viewDataBinding.tvTest2.setOnClickListener {
            send2()
        }
    }

    override fun addObservers() {
    }

    private fun send() {
        val intent = Intent(this@HomeActivity, AppWidget::class.java)
        intent.putExtra("test", "value_1")
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        this@HomeActivity.sendBroadcast(intent)
    }

    private fun send2() {
        val intent = Intent(this@HomeActivity, AppWidget::class.java)
        intent.putExtra("test", "value_2")
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        this@HomeActivity.sendBroadcast(intent)
    }
}
