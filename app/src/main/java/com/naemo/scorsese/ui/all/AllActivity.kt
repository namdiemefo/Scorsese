package com.naemo.scorsese.ui.all

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.naemo.scorsese.BR
import com.naemo.scorsese.BuildConfig
import com.naemo.scorsese.R
import com.naemo.scorsese.databinding.ActivityAllBinding
import com.naemo.scorsese.api.model.Movie
import com.naemo.scorsese.api.model.MovieResponse
import com.naemo.scorsese.data.local.FavoriteDbHelper
import com.naemo.scorsese.network.Client
import com.naemo.scorsese.ui.adapter.MoviesAdapter
import com.naemo.scorsese.ui.base.BaseActivity
import com.naemo.scorsese.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_all.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class AllActivity : BaseActivity<ActivityAllBinding, AllViewModel>(), AllNavigator, MoviesAdapter.ItemClickListener {

    override fun onItemClicked(id: Int, originalTitle: String, posterPath: String, releaseDate: String, overView: String, rating: Double) {
        val intent = Intent(this@AllActivity, DetailActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("original_title", originalTitle)
        intent.putExtra("poster_path", posterPath)
        intent.putExtra("release_date", releaseDate)
        intent.putExtra("over_view", overView)
        intent.putExtra("rating", rating)
        this@AllActivity.startActivity(intent)
    }

    var allViewModel: AllViewModel? = null
        @Inject set

    var mLayoutId = R.layout.activity_all
        @Inject set

    var mBinder: ActivityAllBinding? = null
    var pd: ProgressBar? = null
    private var movieList: ArrayList<Movie> = ArrayList()
    private var adapter: MoviesAdapter? = null
   private var favoriteDbHelper = FavoriteDbHelper(this)

    private val LIST_STATE = "list_state"
    private var savedRecyclerLayoutState: Parcelable? = null
    private val BUNDLE_RECYCLER_LAYOUT = "recycler_layout"

    override fun getBindingVariable(): Int {
       return BR.viewModel
    }

    override fun getViewModel(): AllViewModel? {
        return allViewModel
    }

    override fun getLayoutId(): Int {
        return mLayoutId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all)
        doBinding()


        if (savedInstanceState != null) {
            movieList = savedInstanceState.getParcelableArrayList<Movie>(LIST_STATE) as ArrayList<Movie>
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT)
            displayData()
        } else {
           // initViews()
            initializeViews()
        }
    }

    private fun displayData() {
        adapter = MoviesAdapter(this, movieList, this)
        if (getActivity()!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recycler_view.layoutManager = GridLayoutManager(this, 2)
        } else {
            recycler_view.layoutManager = GridLayoutManager(this, 4)
        }
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.adapter = adapter
        restoreLayoutManagerPosition()
        adapter?.notifyDataSetChanged()
    }

    private fun restoreLayoutManagerPosition() {
        if (savedRecyclerLayoutState != null) {
            recycler_view.layoutManager?.onRestoreInstanceState(savedRecyclerLayoutState)
        }
    }

    private fun doBinding() {
        mBinder = getViewDataBinding()
        mBinder?.viewModel = allViewModel
        mBinder?.navigator = this
        mBinder?.viewModel?.setNavigator(this)

    }

    private fun initializeViews() {
        initViews()
        swipe_refresh?.setColorSchemeResources(android.R.color.holo_orange_dark)
        swipe_refresh.setOnRefreshListener {
                initViews()
          //  Toast.makeText(this, "movie refreshed", Toast.LENGTH_SHORT).show()
        }
    }

   override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelableArrayList(LIST_STATE, movieList)
        savedInstanceState.putParcelable(
            BUNDLE_RECYCLER_LAYOUT,
            recycler_view?.layoutManager?.onSaveInstanceState()
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        movieList = savedInstanceState.getParcelableArrayList<Movie>(LIST_STATE) as ArrayList<Movie>
        savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT)
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun initViews() {
        pd = ProgressBar(this, null, android.R.attr.progressBarStyleLarge)
        pd?.visibility = View.VISIBLE
        loadMovies()
    }

    fun getActivity(): Activity? {
        var context: Context = this
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null

    }

    private fun loadMovies() {

        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(applicationContext, "No Api Key", Toast.LENGTH_SHORT).show()
                pd?.visibility = View.GONE
                return
            }

           val cal = Client()
            val movieResponseCall: Call<MovieResponse> = cal.getApi().getTopRatedMovies(BuildConfig.MOVIE_DB_API_TOKEN)
            movieResponseCall.enqueue(object : Callback<MovieResponse> {

                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    val movies: ArrayList<Movie>? = response.body()?.result
                    movies?.let { movieList.addAll(it) }
                    if (movies == null) {
                        pd?.visibility = View.GONE
                    }
                    val adapter = movies?.let { MoviesAdapter(application, it, this@AllActivity) }
                    recycler_view?.smoothScrollToPosition(0)
                    if (getActivity()!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        recycler_view.layoutManager = GridLayoutManager(this@AllActivity, 2)
                    } else {
                        recycler_view.layoutManager = GridLayoutManager(this@AllActivity, 4)
                    }
                    recycler_view.adapter = adapter
                    if (swipe_refresh.isRefreshing) {
                        swipe_refresh.isRefreshing = false
                    }
                    pd?.visibility = View.GONE
                }
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.v("Error", t.message!!)
                    Toast.makeText(this@AllActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
                    if (swipe_refresh.isRefreshing) {
                        swipe_refresh.isRefreshing = false
                    }
                    pd?.visibility = View.GONE
                }
            })
        } catch (e: Exception) {
            Log.d("Error", e.message!!)
            Toast.makeText(this@AllActivity, e.toString(), Toast.LENGTH_SHORT).show()
            if (swipe_refresh.isRefreshing) {
                swipe_refresh.isRefreshing = false
            }
            pd?.visibility = View.GONE
        }
    }


   override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fav -> {
                loadFavoriteMovies()
                true
            }
            R.id.pop ->{
                loadMovies()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadFavoriteMovies() {

        CoroutineScope(IO).launch {
            movieList.addAll(favoriteDbHelper.getAllFavorites())
            loadInBg()
        }
        movieList = ArrayList()
        if (movieList.isEmpty()) {
            Toast.makeText(this, "you have no favorite movies", Toast.LENGTH_SHORT).show()
        }
        adapter = MoviesAdapter(this, movieList, this)
        if (getActivity()!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recycler_view.layoutManager = GridLayoutManager(this, 2)
        } else {
            recycler_view.layoutManager = GridLayoutManager(this, 4)
        }
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.adapter = adapter

    }

    private suspend fun loadInBg() {
        notifyUi()
    }

    private suspend fun notifyUi() {
        withContext(Main) {
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
       loadMovies()
        super.onResume()
    }

}
