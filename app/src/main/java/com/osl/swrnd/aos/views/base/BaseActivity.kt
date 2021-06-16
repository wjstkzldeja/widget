package com.osl.swrnd.aos.views.base

import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.osl.swrnd.aos.common.Event
import com.osl.swrnd.aos.common.EventObserver
import com.osl.swrnd.aos.common.UiManager


@Suppress("MemberVisibilityCanBePrivate", "unused")
abstract class BaseActivity<V : ViewDataBinding, VM : ViewModel>
  : AppCompatActivity() {

  protected lateinit var ui: UiManager<V, VM>

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ui = UiManager(
      DataBindingUtil.setContentView(this, layoutRes),
      viewModel
    )

    initViews(savedInstanceState)
    addObservers()
    ui.viewDataBinding.lifecycleOwner = this
  }

  override fun onTouchEvent(ev: MotionEvent?): Boolean {
    currentFocus?.let {
      val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
    return super.onTouchEvent(ev)
  }

  protected fun <T> LiveData<T>.observe(observer: (value: T) -> Unit) {
    observe(this@BaseActivity, Observer(observer))
  }

  protected fun <T> LiveData<Event<T>>.observeEvent(observer: (value: T) -> Unit) {
    observe(this@BaseActivity, EventObserver(observer))
  }

  protected fun LiveData<Event<Unit>>.observeEvent(observer: () -> Unit) {
    observe(this@BaseActivity, EventObserver { observer.invoke() })
  }

  @get:IdRes
  abstract val navRes: Int

  @get:LayoutRes
  protected abstract val layoutRes: Int
  protected abstract val viewModel: VM
  protected abstract fun initViews(savedInstanceState: Bundle?)
  protected abstract fun addObservers()
}