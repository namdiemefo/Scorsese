package com.naemo.scorsese.ui.detail

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.naemo.scorsese.R
import com.naemo.scorsese.api.model.Movie
import com.naemo.scorsese.data.local.FavoriteDbHelper
import com.naemo.scorsese.ui.all.AllViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    var allViewModel: AllViewModel? = null
        @Inject set


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initViews()
        AndroidInjection.inject(this)
    }

    private fun initViews() {

        val intent = intent
        val id = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("original_title")
        val thumbnail = intent.getStringExtra("poster_path")
        val date = intent.getStringExtra("release_date")
        val overview = intent.getStringExtra("over_view")
        val rating = intent.getDoubleExtra("rating", 0.0)

        movie_title.text = title
        Glide.with(this).load(thumbnail).into(image)
        plot_synopsis.text = overview
        user_rating.text = rating.toString()
        release_date.text = date

        val dbHelper = FavoriteDbHelper(this)
        val mdb: SQLiteDatabase = dbHelper.writableDatabase


       fun exists(searchItem: String): Boolean {
            Log.d("check", "entered activity")
            Log.d("entry", "entered after")
             val projection = arrayOf(FavoriteDbHelper.ID,
                 FavoriteDbHelper.COLUMN_MOVIE_ID,
                 FavoriteDbHelper.COLUMN_TITLE,
                 FavoriteDbHelper.COLUMN_USER_RATING,
                 FavoriteDbHelper.COLUMN_POSTER_PATH,
                 FavoriteDbHelper.COLUMN_PLOT_SYNOPSIS,
                 FavoriteDbHelper.COLUMN_RELEASE_DATE)

            val selection: String = FavoriteDbHelper.COLUMN_TITLE + "=?"
            val selectionArgs = arrayOf(searchItem)
            val limit = "1"
            val cursor: Cursor = mdb.query(FavoriteDbHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null, limit)
            val exists: Boolean = cursor.count > 0
            cursor.close()
            return exists

        }

        if (exists(title!!)) {
            favorite.isFavorite = true

            favorite.setOnFavoriteChangeListener { buttonView, favorite ->
                if (favorite == true) {
                    saveFavorite()
                    Snackbar.make(buttonView!!, "added to favorite", Snackbar.LENGTH_SHORT).show()
                } else {
                    val favdb = FavoriteDbHelper(this)
                    favdb.deleteFavorite(id)
                    Snackbar.make(buttonView!!, "removed from favorite", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
            else {
                favorite.setOnFavoriteChangeListener { buttonView, favorite ->
                    if (favorite == true) {
                        saveFavorite()
                        Snackbar.make(buttonView!!, "added to favorite", Snackbar.LENGTH_SHORT).show()
                    } else {
                          val favoriteDbHelper = FavoriteDbHelper(this)
                          favoriteDbHelper.deleteFavorite(id)
                        Snackbar.make(buttonView!!, "removed from favorite", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }


    }

   private fun saveFavorite() {
        val favoriteDbHelper = FavoriteDbHelper(this)
        val intent = intent
        val movieId = intent.getIntExtra("id", 0)
        val rating = intent.getDoubleExtra("rating", 0.0)
        val title = intent.getStringExtra("original_title")
        val thumbnail = intent.getStringExtra("poster_path")
        val overview = intent.getStringExtra("over_view")
        val release = intent.getStringExtra("release_date")

       val favorite = Movie()


        favorite.id = movieId
        favorite.originalTitle = title!!
        favorite.voteAverage = rating
        favorite.posterPath = thumbnail!!
        favorite.overview = overview!!
        favorite.releaseDate = release!!


       favoriteDbHelper.addFavorite(favorite)


    }





}
