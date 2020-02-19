package com.naemo.scorsese.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.naemo.scorsese.R
import com.naemo.scorsese.api.model.Movie
import com.naemo.scorsese.db.AppDataBase
import com.naemo.scorsese.db.FavoriteEntity
import com.naemo.scorsese.ui.all.AllViewModel
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.ArrayList
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    var allViewModel: AllViewModel? = null
        @Inject set

    internal var entries: MutableList<FavoriteEntity> = ArrayList<FavoriteEntity>()
    private var mDb: AppDataBase? = null
    private val found = false

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

        //val dbHelper = FavoriteDbHelper(this)
        //  val mdb: SQLiteDatabase = dbHelper.writableDatabase
        check(id)

      /*  fun exists(id: Int): Boolean {
            val allViewModel = AllViewModel(application)
            Log.d("check", "entered activity")
            val entry = allViewModel.loadTitle(id)
            Log.d("entry", "entered after")
            *//* val projection = arrayOf(FavoriteDbHelper.COLUMN_MOVIE_ID,
                 FavoriteDbHelper.COLUMN_MOVIE_ID,
                 FavoriteDbHelper.COLUMN_TITLE,
                 FavoriteDbHelper.COLUMN_USER_RATING,
                 FavoriteDbHelper.COLUMN_POSTER_PATH,
                 FavoriteDbHelper.COLUMN_PLOT_SYNOPSIS)

            val selection: String = FavoriteDbHelper.COLUMN_TITLE + "=?"
            val selectionArgs = arrayOf(searchItem)
            val limit = "1"
            val cursor: Cursor = mdb.query(FavoriteDbHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null, limit)
            val exists: Boolean = cursor.count > 0
            cursor.close()*//*
            //return exists
            return entry?.size == 0
        }*/

    }

   private fun saveFavorite() {
       // val favoriteDbHelper = FavoriteDbHelper(this)
      // val allViewModel = AllViewModel(application)
        val intent = intent
        val movieId = intent.getIntExtra("id", 0)
        val rating = intent.getDoubleExtra("rating", 0.0)
        val title = intent.getStringExtra("original_title")
        val thumbnail = intent.getStringExtra("poster_path")
        val overview = intent.getStringExtra("over_view")
        val release = intent.getStringExtra("release_date")

       val favorite = Movie()
       val fav = FavoriteEntity(0, movieId, title!!, rating, thumbnail!!, overview!!, release!!)
     /*  fav.movieId = movieId.toString()
       fav.rating = rating.toString()
       fav.title = title
       fav.posterPath = thumbnail
       fav.overview = overview
       fav.releaseDate = release*/

/*        favorite.id = movieId
        favorite.originalTitle = title!!
        favorite.voteAverage = rating
        favorite.posterPath = thumbnail!!
        favorite.overview = overview!!
        favorite.releaseDate = release!!*/

       //mDb?.favoriteDao()?.insertFavorite(fav)
      allViewModel?.insert(fav)
       // favoriteDbHelper.addFavorite(favorite)

    }

    private fun deleteFavorite(movieId: Int) {
        //val allViewModel = AllViewModel(application)
        Log.d("delete", "$movieId reached function")
        allViewModel?.deleteId(movieId)
    }

    fun check(movieId: Int) {
       // val movieId = intent.getIntExtra("id", 0)
        //val allViewModel = AllViewModel(application)
        Log.d("check", "entered activity")
        val entry = allViewModel?.loadTitle(movieId)

        Log.d("last check in activity", movieId.toString())
        if (entry?.size != null || entry?.size != 0) {

            favorite.isFavorite = true
            Log.d("check finished", movieId.toString())
            favorite.setOnFavoriteChangeListener { buttonView, favorite ->
                if (favorite == true) {
                    saveFavorite()
                    Snackbar.make(buttonView!!, "added to favorite", Snackbar.LENGTH_SHORT).show()
                } else if (favorite == false){
                    // val favdb = FavoriteDbHelper(this)
                    //  favdb.deleteFavorite(movieId)
                    Log.d("delete", "$movieId reached act")
                    deleteFavorite(movieId)
                    Snackbar.make(buttonView!!, "removed from favorite", Snackbar.LENGTH_SHORT).show()
                }
            }
        } else if (entry.size == 0) {
            Log.d("check finished", "empty")
            favorite.isFavorite = false
            favorite.setOnFavoriteChangeListener { buttonView, favorite ->
                if (favorite == true) {
                    saveFavorite()
                    Snackbar.make(buttonView!!, "added to favorite", Snackbar.LENGTH_SHORT).show()
                } else {
                    deleteFavorite(movieId)
                    //  val favoriteDbHelper = FavoriteDbHelper(this)
                    // favoriteDbHelper.deleteFavorite(id)
                    Snackbar.make(buttonView!!, "removed from favorite", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }



}
