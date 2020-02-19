package com.naemo.scorsese.ui.all

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.naemo.scorsese.BR
import com.naemo.scorsese.BuildConfig
import com.naemo.scorsese.R
import com.naemo.scorsese.databinding.ActivityAllBinding
import com.naemo.scorsese.api.model.Movie
import com.naemo.scorsese.api.model.MovieResponse

import com.naemo.scorsese.db.FavoriteEntity
import com.naemo.scorsese.helper.Helper
import com.naemo.scorsese.network.Client
import com.naemo.scorsese.network.Service
import com.naemo.scorsese.ui.adapter.MoviesAdapter
import com.naemo.scorsese.ui.base.BaseActivity
import com.naemo.scorsese.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_all.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    private var movieList: ArrayList<Movie>? = null
    private var adapter: MoviesAdapter? = null
   // private var favoriteDbHelper = FavoriteDbHelper(this)
    private val cacheSize: Long = 10 * 1024 * 1024

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
        initializeViews()
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

    private fun initViews() {
        pd = ProgressBar(this, null, android.R.attr.progressBarStyleLarge)
        pd?.visibility = View.VISIBLE
        loadMovies()
    }

    private fun loadMovies() {
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(applicationContext, "No Api Key", Toast.LENGTH_SHORT).show()
                pd?.visibility = View.GONE
                return
            }

           // val cal = Client()
            val network = Helper()
            val cache = Cache(cacheDir, cacheSize)
            val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor { chain ->
                    var request = chain.request()
                    if (!network.isNetworkConnected(this)) {
                        val maxStale = 60 * 60 * 24 * 28
                        request = request
                            .newBuilder()
                            .header("Cache-control", "public, only-if-cached, max stale $maxStale")
                            .build()
                    }
                    chain.proceed(request)
                }
                .build()

            val builder: Retrofit.Builder = Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())

            val retrofit: Retrofit = builder.build()
            val apiService: Service = retrofit.create(Service::class.java)

            val movieResponseCall: Call<MovieResponse> = apiService.getTopRatedMovies(BuildConfig.MOVIE_DB_API_TOKEN)
            movieResponseCall.enqueue(object : Callback<MovieResponse> {

                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    val movies: ArrayList<Movie>? = response.body()?.result
                    if (movies == null) {
                        pd?.visibility = View.GONE
                    }
                    val adapter = movies?.let { MoviesAdapter(application, it, this@AllActivity) }
                    recycler_view?.smoothScrollToPosition(0)
                    recycler_view.layoutManager = GridLayoutManager(this@AllActivity, 2)
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
        //movieList = favoriteDbHelper.getAllFavorites()
       // val allViewModel = AllViewModel(application)
        val mList = allViewModel?.loadFav()
        if (mList == null) {
            Toast.makeText(this, "you have no favorite movies", Toast.LENGTH_SHORT).show()
        }
        allViewModel?.loadFav()?.observe(this, object : Observer<MutableList<FavoriteEntity>> {
           override fun onChanged(@Nullable imageEntries: MutableList<FavoriteEntity>) {
                val movies = java.util.ArrayList<Movie>()
                for (entry in imageEntries) {
                    val movie = Movie()
                    movie.id = entry.movieId
                    movie.overview = entry.overview
                    movie.originalTitle = entry.title
                    movie.posterPath = entry.posterPath
                    movie.voteAverage = entry.rating

                    movies.add(movie)
                }

                adapter?.setMovies(movies)
            }
        })
       /* allViewModel.loadFav()?.observe(this, Observer<MutableList<FavoriteEntity>> {
            val movies: MutableList<Movie> = ArrayList()
         *//*   for (entity: FavoriteEntity in it) {
                var movie = Movie()
                movie.id = entity.id
                movie.releaseDate = entity.releaseDate
                movie.overview = entity.overview
                movie.posterPath = entity.posterPath
                movie.voteAverage = entity.rating.toDouble()
                movies.add(movie)
            }
            adapter?.setMovies(movies as ArrayList<Movie>)*//*
            recycler_view.layoutManager = GridLayoutManager(this, 2)
            recycler_view.itemAnimator = DefaultItemAnimator()
            recycler_view.adapter = adapter
            adapter?.setFavorites(it)
           // adapter = MoviesAdapter(this, arrayOf(it), this)
        })*/
        //adapter = MoviesAdapter(this, this.movieList!!, this)

        //adapter!!.notifyDataSetChanged()
      /*  CoroutineScope(Dispatchers.IO).launch {
            movieList?.addAll(favoriteDbHelper.getAllFavorites())
                 notifyUi()
        }*/
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
