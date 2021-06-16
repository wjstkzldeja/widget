@file:Suppress("unused")

package com.osl.swrnd.aos.common

import com.osl.swrnd.common.d
import java.util.*

class MultiValueTask(
  private val callback: (Map<String, Any>) -> Unit,
  vararg keys: String
) {
  private val keys = ArrayList<String>()
  private val values: MutableMap<String, Any> = hashMapOf()

  init {
    this.keys.addAll(listOf(*keys))
  }

  @Synchronized
  operator fun set(key: String, value: Any) {
    require(keys.contains(key)) { "$key is not contains preset keys" }
    d {
      val log = StringBuilder()
      keys.forEach {
        log.append("$it[${values[it]}]\t")
      }
      log
    }
    values[key] = value
    if (values.size == keys.size) {
      callback.invoke(values)
    }
  }

  fun forceCompleted() {
    callback.invoke(values)
  }

  fun clear() {
    values.clear()
  }
}