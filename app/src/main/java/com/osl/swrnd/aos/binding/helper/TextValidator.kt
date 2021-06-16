@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.osl.swrnd.aos.binding.helper

import android.text.Editable
import android.text.TextWatcher
import java.util.regex.Pattern

class TextValidator(
  var validator: Pattern?,
  var validCallback: ((String) -> Unit)? = null,
  var invalidCallback: (() -> Unit)? = null
) : TextWatcher {

  override fun afterTextChanged(s: Editable?) {
    val it = s ?: return
    val validator = validator ?: return
    when {
      it.matches(validator.toRegex()) -> validCallback?.invoke(s.toString())
      else -> invalidCallback?.invoke()
    }
  }

  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
}