package com.naemo.scorsese.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_table")
    fun loadAllFavorite(): LiveData<MutableList<FavoriteEntity>>

    @Query("SELECT * FROM favorite_table WHERE movie_id = :movieId")
    fun loadAll(movieId: Int): MutableList<FavoriteEntity>

    @Insert
    fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavorite(favoriteEntity: FavoriteEntity)

    @Delete
    fun deleteFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorite_table WHERE movie_id = :movie_id")
    fun deleteFavoriteWithId(movie_id: Int)

    @Query("SELECT * FROM favorite_table WHERE id = :id")
    fun loadFavoriteById(id: Int): LiveData<FavoriteEntity>

}