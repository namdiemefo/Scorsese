package com.naemo.scorsese

import android.app.Activity
import android.app.Application
import com.naemo.scorsese.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class Scorsese : Application(), HasActivityInjector {

    internal var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null
        @Inject set

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
    override fun activityInjector(): AndroidInjector<Activity>?{
        return activityDispatchingAndroidInjector
    }
}