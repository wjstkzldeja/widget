@file:Suppress("unused")

package com.osl.swrnd.aos.binding

import android.widget.CompoundButton
import androidx.databinding.BindingAdapter
import com.osl.swrnd.aos.binding.helper.CheckStateChangeListener


@BindingAdapter("isOn")
fun setSwitchState(view: CompoundButton, isOn: Boolean?) {
  view.isChecked = isOn ?: false
}

@BindingAdapter("checkedId")
fun setCheckedState(view: CompoundButton, checkedId: Int) {
  view.isChecked = view.id == checkedId
}

@BindingAdapter("onCheck")
fun setOnCheckStateChangeListener(view: CompoundButton, listener: CheckStateChangeListener) {
  view.setOnCheckedChangeListener { _, isChecked -> listener.onCheckStateChanged(isChecked) }
}