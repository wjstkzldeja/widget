@file:Suppress("unused")

package com.osl.swrnd.aos.binding

import android.animation.Animator
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter("lottieEnded")
fun setLottiEndedListener(view: LottieAnimationView, endListener: Runnable) {
  view.addAnimatorListener(object : Animator.AnimatorListener {
    override fun onAnimationEnd(animation: Animator?) = endListener.run()
    override fun onAnimationStart(animation: Animator?) = Unit
    override fun onAnimationCancel(animation: Animator?) = Unit
    override fun onAnimationRepeat(animation: Animator?) = Unit
  })
}