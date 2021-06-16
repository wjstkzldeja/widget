package com.osl.swrnd.aos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.osl.swrnd.aos.views.home.HomeScreenViewModel
import com.osl.swrnd.domain.repository.IRepository
import com.osl.swrnd.domain.usecase.Test2UseCase
import com.osl.swrnd.domain.usecase.TestUseCase

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
  repository: IRepository
) :
  ViewModelProvider.NewInstanceFactory() {

  private val testUseCase = TestUseCase(repository)
  private val test2UseCase = Test2UseCase(repository)

  override fun <T : ViewModel?> create(modelClass: Class<T>): T =
    with(modelClass) {
      when {
        isAssignableFrom(HomeScreenViewModel::class.java) -> HomeScreenViewModel(
          testUseCase,
          test2UseCase
        )
        else -> super.create(modelClass)
      }
    } as T
}