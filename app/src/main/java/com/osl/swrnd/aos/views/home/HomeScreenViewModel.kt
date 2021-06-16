package com.osl.swrnd.aos.views.home

import androidx.lifecycle.MutableLiveData
import com.osl.swrnd.aos.views.base.BaseViewModel
import com.osl.swrnd.common.d
import com.osl.swrnd.common.w
import com.osl.swrnd.domain.usecase.Test2UseCase
import com.osl.swrnd.domain.usecase.TestUseCase
import com.osl.swrnd.entity.local.req.TestReq

class HomeScreenViewModel(
  private val testUseCase: TestUseCase,
  private val test2UseCase: Test2UseCase
) : BaseViewModel() {

  private val test = MutableLiveData("")
  fun init() {
    with(testUseCase) {
      vo("")?.request(
        success = {},
        error = {},
      )?.addTo(disposable)
    }
    with(test2UseCase) {
      TestReq("title").request(
        success = { d(it) },
        error = { w(it) },
        preExecutor = ::onLoading,
        postExecutor = ::onFinishLoading
      )?.addTo(disposable)
    }
  }
}