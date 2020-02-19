package com.naemo.scorsese.ui.all

import android.app.Application
import com.naemo.scorsese.api.model.Movie
import com.naemo.scorsese.data.local.FavoriteDbHelper
import com.naemo.scorsese.ui.base.BaseViewModel

class AllViewModel(application: Application) : BaseViewModel<AllNavigator>(application) {

    var favoriteDbHelper: FavoriteDbHelper? = null


}