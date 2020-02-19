package com.naemo.scorsese.di.builder

import com.naemo.scorsese.ui.all.AllActivity
import com.naemo.scorsese.ui.all.AllModule
import com.naemo.scorsese.ui.base.BaseModule
import com.naemo.scorsese.ui.detail.DetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [AllModule::class, BaseModule::class])
    abstract fun bindAllActivity(): AllActivity

    @ContributesAndroidInjector(modules = [AllModule::class, BaseModule::class])
    abstract fun bindDetailActivity(): DetailActivity
}