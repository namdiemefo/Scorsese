package com.naemo.scorsese.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.naemo.scorsese.api.model.Movie
import java.lang.Exception

class FavoriteDbHelper(var context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME,
    factory,
    DATABASE_VERSION
) {


    companion object {
        val factory: SQLiteDatabase.CursorFactory? = null
        private const val DATABASE_NAME = "favorite.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "favorite"
        const val ID = "id"
        const val COLUMN_MOVIE_ID = "movieId"
        const val COLUMN_TITLE = "title"
        const val COLUMN_USER_RATING = "vote_average"
        const val COLUMN_POSTER_PATH = "poster_path"
        const val COLUMN_PLOT_SYNOPSIS = "overview"
        const val COLUMN_RELEASE_DATE = "release_date"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_FAVORITE_TABLE = ("CREATE TABLE $TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_MOVIE_ID INTEGER," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_USER_RATING REAL," +
                "$COLUMN_POSTER_PATH TEXT," +
                "$COLUMN_PLOT_SYNOPSIS TEXT," +
                "$COLUMN_RELEASE_DATE TEXT)")
        db?.execSQL(CREATE_FAVORITE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

     fun addFavorite(movie: Movie) {
         val db: SQLiteDatabase = this.writableDatabase
         val contentValues = ContentValues()
         contentValues.put(COLUMN_MOVIE_ID, movie.id)
         contentValues.put(COLUMN_TITLE, movie.originalTitle)
         contentValues.put(COLUMN_USER_RATING, movie.voteAverage)
         contentValues.put(COLUMN_POSTER_PATH, movie.posterPath)
         contentValues.put(COLUMN_PLOT_SYNOPSIS, movie.overview)
         contentValues.put(COLUMN_RELEASE_DATE, movie.releaseDate)
         try {
             db.insert(TABLE_NAME, null, contentValues)
         } catch (e: Exception) {
             Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
         }
         db.close()
     }

    fun deleteFavorite(id: Int) {
        val db: SQLiteDatabase = this.writableDatabase
        val query = "Delete From $TABLE_NAME where $COLUMN_MOVIE_ID = $id"
        db.execSQL(query)
    }

   fun getAllFavorites() : ArrayList<Movie> {
        val columns = arrayOf(
            ID,
            COLUMN_MOVIE_ID,
            COLUMN_TITLE,
            COLUMN_USER_RATING,
            COLUMN_POSTER_PATH,
            COLUMN_PLOT_SYNOPSIS,
            COLUMN_RELEASE_DATE
        )
        val sortOrder: String = ID
        val favoriteList: ArrayList<Movie> = ArrayList()
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_NAME, columns, null, null, null, null, sortOrder)

        if (cursor.moveToFirst()) {
            do {
                val movie = Movie()
                movie.id = (cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_ID))).toInt()
                movie.originalTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                movie.voteAverage = (cursor.getString(cursor.getColumnIndex(COLUMN_USER_RATING))).toDouble()
                movie.posterPath = cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH))
                movie.overview = cursor.getString(cursor.getColumnIndex(COLUMN_PLOT_SYNOPSIS))
                movie.releaseDate = cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE))

                favoriteList.add(movie)

            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return favoriteList
   }
}