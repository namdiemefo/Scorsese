package com.naemo.scorsese.db

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FavoriteRepository(application: Application) : CoroutineScope {
    val context: Context? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var favoriteDao: FavoriteDao? = null
    private var favoriteList: LiveData<MutableList<FavoriteEntity>>? = null
    var favList: MutableList<FavoriteEntity>? = null

    init {
        val database = AppDataBase.invoke(application)
        favoriteDao = database.favoriteDao()
        favoriteList = favoriteDao?.loadAllFavorite()
    }

    fun loadFavorites(): LiveData<MutableList<FavoriteEntity>>? {
        //launch { favoriteList = getAllMovies() }
        return favoriteDao?.loadAllFavorite()
    }

 /*   private suspend fun getAllMovies(): LiveData<MutableList<FavoriteEntity>>? {
        return withContext(IO) {
            Log.d("repo", "get all movies")
             favoriteDao?.loadAllFavorite()
        }
    }*/

    fun loadAll(id: Int): MutableList<FavoriteEntity>? {
           Log.d("check in repo", id.toString())
           //return findMovie(id)
        /*return withContext(IO) {
            Log.d("more check in repo", id.toString())
            favoriteDao?.loadAll(id)
        }*/
        CoroutineScope(IO).launch {
            favList = favoriteDao?.loadAll(id)
            Log.d("more check in repo", id.toString())
        }
        return favList
    }

    private suspend fun findMovie(id: Int): MutableList<FavoriteEntity>? {
        return withContext(IO) {
            Log.d("more check in repo", id.toString())
            favoriteDao?.loadAll(id)
        }
    }

/*
    fun loadAll1(title: String): MutableList<FavoriteEntity> {
        Log.d("check", "searching")
        return launch { searchFav(title) }
    }*/
/*

    private suspend fun searchFav(title: String) {
        withContext(IO) {
            favoriteDao?.loadAll(title)
        }
    }
*/


    /*  fun loadAll(title: String) = launch { loadA(title) }

      private suspend fun loadA(title: String) {
          withContext(IO) {
              favoriteDao?.loadAll(title)
          }
      }*/

    fun insert(favoriteEntity: FavoriteEntity) = launch {
        Log.d("check", "save inserting")
        insertFav(favoriteEntity) }

    private suspend fun insertFav(favoriteEntity: FavoriteEntity) {
        withContext(IO) {
            Log.d("more check", "save inserted")
            favoriteDao?.insertFavorite(favoriteEntity)
        }
    }

    fun update(favoriteEntity: FavoriteEntity) = launch { updateFav(favoriteEntity) }

    private suspend fun updateFav(favoriteEntity: FavoriteEntity) {
        withContext(IO) {
            favoriteDao?.updateFavorite(favoriteEntity)
        }
    }

    fun delete(favoriteEntity: FavoriteEntity) = launch { deleteFav(favoriteEntity) }

    private suspend fun deleteFav(favoriteEntity: FavoriteEntity) {
        withContext(IO) {
            favoriteDao?.deleteFavorite(favoriteEntity)
        }
    }

    fun deleteWithId(id: Int) = launch {
        Log.d("delete", "$id reached repo")
        deleteFavWithId(id)
    }

    private suspend fun deleteFavWithId(id: Int) {
        withContext(IO) {
            Log.d("final delete", "$id reached dao")
            favoriteDao?.deleteFavoriteWithId(id)
        }
    }

    fun loadWithId(id: Int) = launch { loadFavWithId(id) }

    private suspend fun loadFavWithId(id: Int) {
        withContext(IO) {
            favoriteDao?.loadFavoriteById(id)
        }
    }
}