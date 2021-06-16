package com.osl.swrnd.aos.views.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.osl.swrnd.aos.App

@Suppress("unused")
abstract class DataBoundListAdapter<T>(
  diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, DataBoundViewHolder>(
  AsyncDifferConfig.Builder<T>(diffCallback)
    .setBackgroundThreadExecutor(App.diskIO)
    .build()
) {

  private val viewHolders: MutableList<DataBoundViewHolder> = mutableListOf()

  init {
    stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder {
    val binding = createBinding(parent, viewType)
    val viewHolder = DataBoundViewHolder(binding)
    binding.lifecycleOwner = viewHolder
    viewHolder.markCreated()
    viewHolders.add(viewHolder)

    return viewHolder
  }

  override fun onViewRecycled(holder: DataBoundViewHolder) {
    viewHolders.remove(holder)
    holder.markDestroyed()
    super.onViewRecycled(holder)
  }

  override fun onBindViewHolder(holder: DataBoundViewHolder, position: Int) {
    if (position < itemCount) {
      bind(holder.binding, getItem(position))
      holder.binding.executePendingBindings()
    }
  }

  override fun onViewAttachedToWindow(holder: DataBoundViewHolder) {
    super.onViewAttachedToWindow(holder)
    holder.markAttach()
  }

  override fun onViewDetachedFromWindow(holder: DataBoundViewHolder) {
    super.onViewDetachedFromWindow(holder)
    holder.markDetach()
  }

  fun destroyed() {
    viewHolders.forEach {
      it.markDestroyed()
    }
  }

  protected abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding

  protected abstract fun bind(binding: ViewDataBinding, item: T)
}
