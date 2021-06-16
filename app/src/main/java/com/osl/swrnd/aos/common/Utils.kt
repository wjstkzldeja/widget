package com.osl.swrnd.aos.common

import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator


fun ValueAnimator?.setupAnimator(from: Int?, to: Int?, updater: (Int) -> Unit): ValueAnimator {
  this?.cancel()

  return ValueAnimator.ofFloat(from?.toFloat() ?: 0f, to?.toFloat() ?: 0f).apply {
    duration = 150
    interpolator = AccelerateInterpolator()
    addUpdateListener {
      updater.invoke((it.animatedValue as Float).toInt())
    }
    start()
  }
}