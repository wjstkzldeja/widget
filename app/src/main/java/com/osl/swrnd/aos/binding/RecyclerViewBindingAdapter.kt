@file:Suppress("unused")

package com.osl.swrnd.aos.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.osl.swrnd.aos.views.base.DataBoundListAdapter
import com.osl.swrnd.common.d

@BindingAdapter(value = ["adapter", "items"], requireAll = false)
fun <T> setRecyclerViewAdapterItems(view: RecyclerView, adapter: DataBoundListAdapter<T>?, items: List<T>?) {
  if (view.adapter != adapter) {
    view.adapter = adapter
  }
  d(items?.size, view)
  adapter?.submitList(items)
}

@BindingAdapter("layoutManager", requireAll = false)
fun setRecyclerViewLayoutManager(view: RecyclerView, layoutManager: RecyclerView.LayoutManager?) {
  view.layoutManager = layoutManager
}

@BindingAdapter("decorator", requireAll = false)
fun setRecyclerViewItemDecorator(view: RecyclerView, decorator: RecyclerView.ItemDecoration?) {
  decorator ?: return
  view.removeItemDecoration(decorator)
  view.addItemDecoration(decorator)
}