@file:Suppress("unused")

package com.osl.swrnd.aos.binding

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.osl.swrnd.aos.binding.helper.CustomConsumer
import com.osl.swrnd.aos.binding.helper.PasswordMarker
import com.osl.swrnd.aos.binding.helper.TextValidator


@BindingAdapter(value = ["pwMark", "inputValidate"], requireAll = false)
fun setPasswordCustomMark(view: EditText, passwordMark: String?, validate: TextValidator?) {
  if (passwordMark != null) {
    setupPasswordMark(view, passwordMark)
  }
  if (validate != null) {
    setupValidate(view, validate)
  }
}

@BindingAdapter(value = ["onImeOptions", "imeActionId", "inputType"], requireAll = false)
fun setOnImeOptions(
  view: EditText,
  listener: CustomConsumer?,
  imeActionId: Int?,
  inputType: Int?
) {

  if (inputType != null) {
    setupInputType(view, inputType)
  }
  if (listener != null) {
    setupOnImeOptions(view, listener, imeActionId)
  }
}

fun setupInputType(view: EditText, inputType: Int) {
  view.inputType = inputType
}

private fun setupOnImeOptions(view: EditText, listener: CustomConsumer, imeActionId: Int?) {
  view.setOnEditorActionListener { _, actionId, _ ->
    if (actionId == (imeActionId ?: EditorInfo.IME_ACTION_NEXT)) {
      listener.accept(view.text.toString())
      return@setOnEditorActionListener true
    }
    return@setOnEditorActionListener false
  }
}

private fun setupPasswordMark(view: EditText, passwordMark: String) {
  val marker = if (passwordMark.isBlank()) {
    '\u2731'
  } else {
    passwordMark[0]
  }
  view.transformationMethod = PasswordMarker.getInstance(marker)
}

private fun setupValidate(view: EditText, validate: TextValidator) {
  view.addTextChangedListener(validate)
}
