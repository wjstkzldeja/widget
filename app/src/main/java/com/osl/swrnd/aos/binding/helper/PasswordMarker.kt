package com.osl.swrnd.aos.binding.helper

import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.view.View


@Suppress("unused")
class PasswordMarker private constructor() : PasswordTransformationMethod() {
  internal var maskChar: Char = '\u2731'
  internal var timeBeforeMask: Long = 1000L
  private var passwordCharSequence: PasswordCharSequence? = null
  private var view: View? = null
  private var handler: Handler? = null
  private var runnable: Runnable? = null

  companion object {
    private var instance: PasswordMarker? = null

    fun getInstance(
      maskChar: Char = '\u2731',
      timeBeforeMask: Long = 1000L
    ): PasswordMarker {
      val tmpInstance = (instance ?: PasswordMarker()).apply {
        this.maskChar = maskChar
        this.timeBeforeMask = timeBeforeMask
      }
      instance = tmpInstance
      return tmpInstance
    }
  }

  override fun getTransformation(source: CharSequence, view: View): CharSequence? {
    passwordCharSequence = PasswordCharSequence(source)
    handler = Handler()
    this.view = view
    return passwordCharSequence
  }

  override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    super.onTextChanged(s, start, before, count)
    if (before <= 0) {
      passwordCharSequence?.showLast = true
      runnable?.let {
        handler?.removeCallbacks(it)
      }
      runnable = Runnable {
        passwordCharSequence?.showLast = false
        view?.requestLayout()
      }
      runnable?.let {
        handler?.postDelayed(it, timeBeforeMask)
      }
    } else {
      runnable?.let {
        handler?.removeCallbacks(it)
      }
      passwordCharSequence?.showLast = false
      view?.requestLayout()
    }
  }

  inner class PasswordCharSequence(private val mSource: CharSequence) : CharSequence {
    var showLast = true

    override val length: Int get() = mSource.length

    override fun get(index: Int) = when {
      index != mSource.length - 1 -> maskChar
      showLast -> mSource[index]
      else -> maskChar
    }

    override fun subSequence(startIndex: Int, endIndex: Int) =
      mSource.subSequence(startIndex, endIndex)
  }
}