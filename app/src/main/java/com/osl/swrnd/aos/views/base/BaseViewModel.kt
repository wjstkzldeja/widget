package com.osl.swrnd.aos.views.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.osl.swrnd.aos.common.Event
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

@Suppress("MemberVisibilityCanBePrivate", "PropertyName", "unused")
abstract class BaseViewModel : ViewModel() {

  val disposable = CompositeDisposable()

  var isOnLoading = false

  protected val _onBackPressed = MutableLiveData<Event<Unit>>()
  val onBackPressed: LiveData<Event<Unit>> get() = _onBackPressed

  private val _onLoading = MutableLiveData<Event<Unit>>()
  val onLoading: LiveData<Event<Unit>> get() = _onLoading

  private val _onFinishLoading = MutableLiveData<Event<Unit>>()
  val onFinishLoading: LiveData<Event<Unit>> get() = _onFinishLoading

  // TODO for bottom navigation [
//  private val _onBottomMenuHome = MutableLiveData<Event<Unit>>()
//  val onBottomMenuHome: LiveData<Event<Unit>> get() = _onBottomMenuHome
//
//  private val _onBottomMenuRecipe = MutableLiveData<Event<Unit>>()
//  val onBottomMenuRecipe: LiveData<Event<Unit>> get() = _onBottomMenuRecipe
//
//  private val _onBottomMenuShopping = MutableLiveData<Event<Unit>>()
//  val onBottomMenuShopping: LiveData<Event<Unit>> get() = _onBottomMenuShopping
//
//  private val _onBottomMenuSaladBar = MutableLiveData<Event<Unit>>()
//  val onBottomMenuSaladBar: LiveData<Event<Unit>> get() = _onBottomMenuSaladBar
//
//  private val _onBottomMenuMyPage = MutableLiveData<Event<Unit>>()
//  val onBottomMenuMyPage: LiveData<Event<Unit>> get() = _onBottomMenuMyPage
  // TODO for bottom navigation ]

  override fun onCleared() {
    super.onCleared()
    disposable.clear()
  }

  fun onLoading() {
    isOnLoading = true
    _onLoading.value = Event(Unit)
  }

  fun onFinishLoading() {
    isOnLoading = false
    _onFinishLoading.value = Event(Unit)
  }

  fun onBackPressed() {
    _onBackPressed.value = Event(Unit)
  }

  // TODO for bottom navigation [
//  fun onClickBottomMenuHome() {
//    _onBottomMenuHome.value = Event(Unit)
//  }
//
//  fun onClickBottomMenuRecipe() {
//    _onBottomMenuRecipe.value = Event(Unit)
//  }
//
//  fun onClickBottomMenuShopping() {
//    _onBottomMenuShopping.value = Event(Unit)
//  }
//
//  fun onClickBottomMenuSaladBar() {
//    _onBottomMenuSaladBar.value = Event(Unit)
//  }
//
//  fun onClickBottomMenuMyPage() {
//    _onBottomMenuMyPage.value = Event(Unit)
//  }
// TODO for bottom navigation ]

  protected fun Disposable.addTo(disposable: CompositeDisposable) = disposable.add(this)
  protected fun <T> MutableLiveData<T>.post(value: T?, delay: Long) = apply {
    Single.timer(delay, TimeUnit.MILLISECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { _ ->
        postValue(value)
      }.addTo(disposable)
  }
}
