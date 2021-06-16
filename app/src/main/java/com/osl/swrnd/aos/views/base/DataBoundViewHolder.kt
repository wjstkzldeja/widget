package com.osl.swrnd.aos.views.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView

class DataBoundViewHolder(
  val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

  private val lifecycleRegistry = LifecycleRegistry(this)
  private var wasPaused: Boolean = false

  init {
    lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
  }

  fun markCreated() {
    lifecycleRegistry.currentState = Lifecycle.State.CREATED
  }

  fun markAttach() {
    if (wasPaused) {
      lifecycleRegistry.currentState = Lifecycle.State.RESUMED
      wasPaused = false
    } else {
      lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }
  }

  fun markDetach() {
    wasPaused = true
    lifecycleRegistry.currentState = Lifecycle.State.CREATED
  }

  fun markDestroyed() {
    lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
  }

  override fun getLifecycle(): Lifecycle {
    return lifecycleRegistry
  }
}