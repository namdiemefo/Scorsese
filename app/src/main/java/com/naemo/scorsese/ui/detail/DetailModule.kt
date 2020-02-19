package com.naemo.scorsese.ui.detail

import android.app.Application
import com.naemo.scorsese.ui.all.AllViewModel
import dagger.Module
import dagger.Provides

@Module
class DetailModule {

    @Provides
    fun providesAllViewModel(application: Application): AllViewModel {
        return AllViewModel(application)
    }

}