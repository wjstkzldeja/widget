package com.osl.swrnd.aos.views.home

import android.os.Bundle
import com.osl.swrnd.aos.R
import com.osl.swrnd.aos.databinding.FragmentHomescreenBinding
import com.osl.swrnd.aos.views.base.BaseFragment
import com.osl.swrnd.aos.views.container.home.HomeViewModel

class HomeScreenFragment : BaseFragment<FragmentHomescreenBinding, HomeScreenViewModel>() {
  override val layoutRes = R.layout.fragment_homescreen
  override val viewModelClass = HomeScreenViewModel::class.java
  override val actViewModelClass = HomeViewModel::class.java


  override fun initViews(savedInstanceState: Bundle?) {
    viewModel.init()
  }

  override fun addObservers() {
  }
}