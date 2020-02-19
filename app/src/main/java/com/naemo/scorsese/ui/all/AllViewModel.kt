package com.naemo.scorsese.ui.all

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.naemo.scorsese.db.FavoriteEntity
import com.naemo.scorsese.db.FavoriteRepository
import com.naemo.scorsese.ui.base.BaseViewModel

class AllViewModel(application: Application) : BaseViewModel<AllNavigator>(application) {
    private var repository: FavoriteRepository? = null
    private var favoriteList: LiveData<MutableList<FavoriteEntity>>? = null

    init {
        repository = FavoriteRepository(application)
       // favoriteList = repository?.loadFavorites()
    }

    fun loadFav(): LiveData<MutableList<FavoriteEntity>>? {
        return repository?.loadFavorites()
    }

     fun loadTitle(id: Int): MutableList<FavoriteEntity>? {
        //repository?.loadAll(title)
        Log.d("check", "entered viewModel")
       return repository?.loadAll(id)
    }

    fun insert(favoriteEntity: FavoriteEntity) {
        Log.d("check", "save entered viewModel")
        repository?.insert(favoriteEntity)
    }

    fun update(favoriteEntity: FavoriteEntity) {
        repository?.update(favoriteEntity)
    }

    fun delete(favoriteEntity: FavoriteEntity) {
        repository?.delete(favoriteEntity)
    }

    fun deleteId(id: Int) {
        Log.d("delete", "$id reached viewModel")
        repository?.deleteWithId(id)
    }

    fun loadId(id: Int) {
        repository?.loadWithId(id)
    }
}