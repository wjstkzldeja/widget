package com.osl.swrnd.aos.views.base

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.AnimBuilder
import androidx.navigation.fragment.findNavController
import com.osl.swrnd.aos.App
import com.osl.swrnd.aos.R
import com.osl.swrnd.aos.common.*
import com.osl.swrnd.common.d


@Suppress("MemberVisibilityCanBePrivate", "RedundantOverride")
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : Fragment() {

  protected lateinit var ui: UiManager<V, VM>
  protected lateinit var actViewModel: BaseViewModel
  protected open val needSpaceBottomNavigation = false
  protected val viewModel: VM get() = ui.viewModel
  protected val baseNavAnimBuilder: AnimBuilder.() -> Unit = {
    enter = R.anim.fragment_open_enter
    exit = R.anim.fragment_open_exit
    popEnter = R.anim.fragment_close_enter
    popExit = R.anim.fragment_close_exit
  }

  // TODO for bottom navigation [
//  private val bottomMenus = arrayListOf<Int>(
//    R.id.MainFragment,
//    R.id.RecipeFragment,
//    R.id.ShoppingFragment,
//    R.id.SaladBarFragment,
//    R.id.MyPageFragment
//  )
//
//  private val bottomMenuButtons = arrayListOf<Int>(
//    R.id.tvBtnBottomMenuHome,
//    R.id.tvBtnBottomMenuRecipe,
//    R.id.tvBtnBottomMenuShopping,
//    R.id.tvBtnBottomMenuSaladBar,
//    R.id.tvBtnBottomMenuMyPage
//  )
  // TODO for bottom navigation ]

  private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
    ui.setNavigationBarSafeArea(ui.viewDataBinding.root, activity)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val viewLifecycleOwner = this.viewLifecycleOwner
    ui = UiManager(
      DataBindingUtil.inflate(inflater, layoutRes, container, false),
      ViewModelProvider(this, App.instance!!.viewModelFactory).get(viewModelClass)
    )
    actViewModel = ViewModelProvider(requireActivity(), App.instance!!.viewModelFactory).get(actViewModelClass)
    d(viewModel, actViewModel)

    ui.viewDataBinding.lifecycleOwner = viewLifecycleOwner

    // TODO for bottom navigation [
//    ui.viewDataBinding.root.findViewById<View>(R.id.clBottomMenu)?.apply {
//      postponeEnterTransition()
//      viewTreeObserver.addOnPreDrawListener {
//        startPostponedEnterTransition()
//        true
//      }
//    }
    // TODO for bottom navigation ]

    activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          if (!viewModel.isOnLoading) {
            viewModel.onBackPressed()
          }
        }
      })

//    ui.setNavigationBarSafeArea(ui.viewDataBinding.root, activity)
    // TODO for bottom navigation [
//    setupBottomNavigationSpace(true)
    // TODO for bottom navigation ]
    activity?.window?.let {
      windowSoftInputMode(it)
    }

    return ui.viewDataBinding.root
  }

  // TODO for bottom navigation [
//  private fun setupBottomNavigationSpace(isSet: Boolean) {
//    if (needSpaceBottomNavigation) {
//      if (isSet) {
//        ui.viewDataBinding.root.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
//      } else {
//        ui.viewDataBinding.root.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
//      }
//    }
//  }
  // TODO for bottom navigation ]

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    null.setActionBar()
    initViews(savedInstanceState)
    addObservers()
    addLoadingObservers()
    viewModel.onBackPressed.observeEvent(::onBackPressed)
    // TODO for bottom navigation [
//    addBottomMenuObservers()
    // TODO for bottom navigation ]
  }

  override fun onDestroyView() {
    // TODO for bottom navigation [
//    setupBottomNavigationSpace(false)
    // TODO for bottom navigation ]
    super.onDestroyView()
  }

  // TODO for bottom navigation [
//  private fun addBottomMenuObservers() {
//    viewModel.onBottomMenuHome.observeEvent(this::onBottomMenuHome)
//    viewModel.onBottomMenuRecipe.observeEvent(this::onBottomMenuRecipe)
//    viewModel.onBottomMenuShopping.observeEvent(this::onBottomMenuShopping)
//    viewModel.onBottomMenuSaladBar.observeEvent(this::onBottomMenuSaladBar)
//    viewModel.onBottomMenuMyPage.observeEvent(this::onBottomMenuMyPage)
//  }
//
//  private fun onBottomMenuTo(to: Int) {
//    val navDestination = findNavController().currentDestination
//    if (navDestination?.id == to) {
//      onRefresh()
//      return
//    }
//    if (bottomMenus.indexOf(to) == bottomMenuButtons.indexOf(viewModel.selectedBottomMenuId.value)) {
//      d(bottomMenus.indexOf(to), bottomMenuButtons.indexOf(viewModel.selectedBottomMenuId.value))
//      onRefresh()
//      return
//    }
//
//    val bottomMenu = ui.viewDataBinding.root.findViewById<View>(R.id.clBottomMenu)
////    val bottomMenuHome = ui.viewDataBinding.root.findViewById<View>(R.id.tvBtnBottomMenuHome)
//    val extra = bottomMenu?.let {
//      FragmentNavigatorExtras(
//        bottomMenu to bottomMenu.transitionName
////      bottomMenuHome to bottomMenuHome.transitionName
//      )
//    }
//    navOptions {
//      val currentId = navDestination?.id ?: return@navOptions
////      if (currentId != R.id.MainFragment) {
//      navDestination.let {
//        popUpTo = R.id.MainFragment
//      }
////      }
//      launchSingleTop = true
////      anim {
////        if (bottomMenus.indexOf(to) < bottomMenus.indexOf(currentId)) {
////          enter = R.anim.pop_enter_from_left_full
////          exit = R.anim.exit_to_right_full
////          popEnter = R.anim.pop_enter_from_left_full
////          popExit = R.anim.pop_exit_to_right
////        } else {
////          enter = R.anim.pop_enter_from_right_full
////          exit = R.anim.exit_to_left_full
////          popEnter = R.anim.pop_enter_from_left_full
////          popExit = R.anim.pop_exit_to_right
////        }
////      }
//    }.let {
//      findNavController().navigate(
//        to, bundleOf(
//        "userVo" to viewModel.userVo.toParcel()
//      ), it, extra
//      )
//    }
//  }
//
//  protected open fun onBottomMenuHome() {
//    onBottomMenuTo(R.id.MainFragment)
////    val navDestination = findNavController().currentDestination
////    if (navDestination?.id == R.id.MainFragment) {
////      onRefresh()
////      return
////    }
////    findNavController().navigateUp()
////    navOptions {
////      navDestination?.let {
////        popUpTo = R.id.MainFragment
////      }
////      launchSingleTop = true
////    }.let {
////      findNavController().navigate(R.id.MainFragment, null, it)
////    }
//  }
//
//  protected open fun onBottomMenuRecipe() {
//    onBottomMenuTo(R.id.RecipeFragment)
//  }
//
//  protected open fun onBottomMenuShopping() {
//    onBottomMenuTo(R.id.ShoppingFragment)
//  }
//
//  protected open fun onBottomMenuSaladBar() {
//    onBottomMenuTo(R.id.SaladBarFragment)
//  }
//
//  protected open fun onBottomMenuMyPage() {
//    onBottomMenuTo(R.id.MyPageFragment)
//  }
//
//  protected open fun onRefresh() {
//  }
  // TODO for bottom navigation ]

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.home -> {
      onBackPressed()
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  protected fun <T> LiveData<T>.observe(observer: (value: T) -> Unit) {
    observe(viewLifecycleOwner, Observer(observer))
  }

  protected fun <T> LiveData<Event<T>>.observeEvent(observer: (value: T) -> Unit) {
    observe(viewLifecycleOwner, EventObserver(observer))
  }

  protected fun LiveData<Event<Unit>>.observeEvent(observer: () -> Unit) {
    observe(viewLifecycleOwner, EventObserver { observer.invoke() })
  }

  protected open fun setFullScreenTheme(isSet: Boolean = true) {
    val activity = activity ?: return
    val w = activity.window ?: return
    val v = view ?: return
    if (isSet) {
      w.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
      )
      v.systemUiVisibility = FLAG_FULLSCREEN
    } else {
      w.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
      v.systemUiVisibility = v.systemUiVisibility and FLAG_FULLSCREEN.inv()
    }
    displayCutoutShortMode(w)
  }

  protected open fun setTransparentLightTheme() {
    val activity = activity ?: return
    val w = activity.window ?: return
    w.setFlags(
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
    w.decorView.systemUiVisibility = 0
    displayCutoutShortMode(w)
  }

  @SuppressLint("ObsoleteSdkInt")
  protected open fun setDarkTheme() {
    val activity = activity ?: return
    val w = activity.window ?: return

    w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    w.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    w.clearFlags(View.SYSTEM_UI_FLAG_LOW_PROFILE)
    w.clearFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      w.clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      w.clearFlags(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    }
    w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    w.setFlags(
      WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
      WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
    )
    w.statusBarColor = ContextCompat.getColor(activity, R.color.defaultBg)
    w.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
  }

  protected open fun setTransparentDarkTheme() {
    val activity = activity ?: return
    val w = activity.window
    w.setFlags(
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
    w.decorView.systemUiVisibility = FLAG_TRANSPARENT_DARK_MODE
    displayCutoutShortMode(w)
  }

  protected fun hideStatusBar() {
    val activity = activity ?: return
    val w = activity.window
    w.decorView.systemUiVisibility = w.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_FULLSCREEN
    displayCutoutShortMode(w)
//    ui.viewDataBinding.root.fitsSystemWindows = true
  }

  protected open fun windowSoftInputMode(w: Window) {
    w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
  }

  protected open fun onBackPressed() {
    if (!findNavController().navigateUp()) {
      activity?.finish()
    }
  }

  private fun addLoadingObservers() {
    viewModel.onLoading.observeEvent { (actViewModel as? ILoadingHandler)?.requestLoading() }
    viewModel.onFinishLoading.observeEvent { (actViewModel as? ILoadingHandler)?.requestFinishLoading() }
  }

  private fun displayCutoutShortMode(w: Window?) {
    if (w == null) {
      return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      w.attributes.layoutInDisplayCutoutMode = FLAG_DISPLAY_CUTOUT_SHORT_EDGES
    }
  }

  @get:LayoutRes
  protected abstract val layoutRes: Int
  protected abstract val viewModelClass: Class<VM>
  protected abstract val actViewModelClass: Class<out BaseViewModel>
  protected abstract fun initViews(savedInstanceState: Bundle?)
  protected abstract fun addObservers()

  protected fun Toolbar?.setActionBar(
    enableNavigationUp: Boolean = false,
    navIcon: Int? = null/* = R.drawable.ic_nav_up*/,
    navUpListener: () -> Unit = ::onBackPressed
  ) {
    this ?: return
    if (enableNavigationUp) {
      navIcon?.let(this::setNavigationIcon)
      setNavigationOnClickListener { navUpListener.invoke() }
    }

  }

  protected fun Fragment.waitForTransition(targetView: View) {
    postponeEnterTransition()
    targetView.doOnPreDraw { startPostponedEnterTransition() }
  }

  protected fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

  protected fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
  }
}

// TODO for bottom navigation [
//@BindingAdapter("selectedBottomMenu")
//fun setSelectedBottomMenu(view: CheckedTextView, selectedBottomMenuId: Int?) {
//  view.isChecked = view.id == selectedBottomMenuId
//}
// TODO for bottom navigation ]