package com.naemo.scorsese.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.naemo.scorsese.R
import com.naemo.scorsese.api.model.Movie
import com.naemo.scorsese.ui.adapter.MoviesAdapter
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteActivity : AppCompatActivity() {

    private var movieList: ArrayList<Movie>? = null
    private var adapter: MoviesAdapter? = null
    private var itemClickListener: MoviesAdapter.ItemClickListener? = null
  //  private var favoriteDbHelper = FavoriteDbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        initViews()
    }

    private fun initViews() {
        //movieList = ArrayList()
        adapter = itemClickListener?.let { MoviesAdapter(this, movieList!!, it) }
        recycler?.layoutManager = GridLayoutManager(this, 2)
        recycler?.itemAnimator = DefaultItemAnimator()
        CoroutineScope(IO).launch {
            loadFavorite()
        }
    }

    private suspend fun loadFavorite() {
        movieList = ArrayList()
        movieList?.clear()
      //  movieList?.addAll(favoriteDbHelper.getAllFavorites())
        notifyUi()
    }

    private suspend fun notifyUi() {
        withContext(Main) {
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        CoroutineScope(IO).launch {
            loadFavorite()
        }
        super.onResume()
    }
}
