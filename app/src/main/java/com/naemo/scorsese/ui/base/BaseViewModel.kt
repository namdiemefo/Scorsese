package com.naemo.scorsese.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

open class BaseViewModel<N>(application: Application) : AndroidViewModel(application) {
    private var navigator: N? = null

    fun getNavigator(): N?{
        return navigator
    }

    open fun setNavigator(navigator: N) {
        this.navigator = navigator
    }
}