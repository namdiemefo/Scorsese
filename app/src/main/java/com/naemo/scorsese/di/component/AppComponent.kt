package com.naemo.scorsese.di.component

import android.app.Application
import com.naemo.scorsese.Scorsese
import com.naemo.scorsese.di.builder.ActivityBuilder
import com.naemo.scorsese.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (AppModule::class), (ActivityBuilder::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder


        fun build(): AppComponent
    }

    fun inject(scorsese: Scorsese)
}