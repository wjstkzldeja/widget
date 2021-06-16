package com.osl.swrnd.aos

import android.app.Application
import com.facebook.stetho.Stetho
import com.osl.swrnd.data.pref.PrefManager
import com.osl.swrnd.domain.repository.IRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class App : Application() {

  companion object {
    val diskIO: ExecutorService = Executors.newSingleThreadExecutor()

    var instance: App? = null
      private set
  }

  val viewModelFactory by lazy { ViewModelFactory(dataRepository) }
  private val dataRepository: IRepository
    get() = RepositoryLocator.provideRepository(this)

  override fun onCreate() {
    super.onCreate()
    instance = this
    PrefManager.initContext(this)
    Stetho.initializeWithDefaults(this)
  }

  override fun onTerminate() {
    onDestroy()
    super.onTerminate()
  }

  private fun onDestroy() {
    instance = null
    RepositoryLocator.clear()
    PrefManager.terminate()
  }
}