package com.naemo.scorsese.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class FavoriteEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "movie_id")
    var movieId: Int,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "rating")
    var rating: Double,
    @ColumnInfo(name = "poster_path")
    var posterPath: String,
    @ColumnInfo(name = "plot_synopsis")
    var overview: String,
    @ColumnInfo(name = "release_date")
    var releaseDate: String

) {
    constructor(movieId: Int, title: String, rating: Double, posterPath: String, overview: String, releaseDate: String): this(
        Int.MIN_VALUE, movieId, title, rating, posterPath, overview, releaseDate)
}