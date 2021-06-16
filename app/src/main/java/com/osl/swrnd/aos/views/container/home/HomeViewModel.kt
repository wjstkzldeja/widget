package com.osl.swrnd.aos.views.container.home

import android.animation.ValueAnimator
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.swrnd.aos.views.base.BaseViewModel
import com.osl.swrnd.aos.views.base.ILoadingHandler
import com.osl.swrnd.common.d

class HomeViewModel : BaseViewModel(), ILoadingHandler {

  private val _loadingVisibility = MutableLiveData(View.GONE)
  val loadingVisibility: LiveData<Int> get() = _loadingVisibility

  private val _loadingMaxAlpha = MutableLiveData(0.5f)
  val loadingMaxAlpha: LiveData<Float> get() = _loadingMaxAlpha

  private val _loadingBgAnimator = MutableLiveData(ValueAnimator.ofFloat(0f, 0f).apply {
    duration = 500
  })
  val loadingBgAnimator: LiveData<ValueAnimator> get() = _loadingBgAnimator

  private val _loadingAnimator = MutableLiveData(ValueAnimator.ofFloat(0f, 0f).apply {
    duration = 500
  })
  val loadingAnimator: LiveData<ValueAnimator> get() = _loadingAnimator

  override fun requestLoading() {
    d()
    _loadingVisibility.value = View.VISIBLE
  }

  override fun requestFinishLoading() {
    d()
    _loadingVisibility.value = View.GONE
  }
}