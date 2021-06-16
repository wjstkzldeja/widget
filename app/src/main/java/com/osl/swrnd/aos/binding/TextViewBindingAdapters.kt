@file:Suppress("unused")

package com.osl.swrnd.aos.binding

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.osl.swrnd.aos.binding.helper.QuantityTextVo
import com.osl.swrnd.common.d
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@BindingAdapter("text")
fun setTextStringRes(view: TextView, @StringRes stringResId: Int?) {
  stringResId?.also {
    view.setText(it)
  } ?: view.let {
    view.text = null
  }
}

@BindingAdapter(value = ["string", "imeFocus"], requireAll = false)
fun setTextString(view: TextView, string: String?, imeFocus: Boolean? = false) {
  if (string == null) {
    return
  }
  view.text = string
  if (imeFocus == true && view is EditText) {
    MainScope().launch {
      delay(10)
      view.requestFocus()
      val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
      imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
  }
}

@BindingAdapter(value = ["strings", "separate"], requireAll = false)
fun setTextStrings(view: TextView, strings: List<String>?, separate: String? = ", ") {
  view.text = strings?.joinToString(separate ?: ", ")
}

@BindingAdapter("textColor")
fun setTextColor(view: TextView, colorResId: Int) {
  if (colorResId == 0) {
    return
  }
  val color = ContextCompat.getColor(view.context, colorResId)
  d(Integer.toHexString(color))
  view.setTextColor(color)
}

@BindingAdapter(value = ["drawableLeft", "drawableTop", "drawableRight", "drawableBottom"], requireAll = false)
fun setTextViewDrawable(view: TextView, left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
  view.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
}

@BindingAdapter(value = ["quantityTextRes", "quantity"])
fun setQuantityTextResources(view: TextView, resourceArray: Array<QuantityTextVo>, quantity: Int) {
  resourceArray.firstOrNull { it.range.contains(quantity) }?.let {
    view.text = view.resources.getString(it.resId, quantity)
  }
}