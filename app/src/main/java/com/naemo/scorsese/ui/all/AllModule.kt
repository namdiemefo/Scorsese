package com.naemo.scorsese.ui.all

import android.app.Application
import com.naemo.scorsese.R
import dagger.Module
import dagger.Provides

@Module
class AllModule {

    @Provides
    fun providesAllViewModel(application: Application): AllViewModel {
        return AllViewModel(application)
    }

    @Provides
    fun layoutId(): Int {
       return R.layout.activity_all
    }
}