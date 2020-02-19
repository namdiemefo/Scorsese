package com.naemo.scorsese.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naemo.scorsese.R
import com.naemo.scorsese.api.model.Movie
import kotlinx.android.synthetic.main.movie_card.view.*

class MoviesAdapter(private var context: Context, private var movieList: ArrayList<Movie>, private var itemClickListener: ItemClickListener) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mList: Movie = movieList[position]
        val originalTitle = mList.originalTitle
        val releaseDate = mList.releaseDate
        val thumbnail = "https://image.tmdb.org/t/p/w500" + mList.posterPath
        val overView = mList.overview
        val id = mList.id
        val rating = mList.voteAverage
        holder.title.text = originalTitle
        holder.releaseDate.text = releaseDate
        //add placeholder to image
        Glide.with(context).load(thumbnail).into(holder.thumbNail)

        holder.movieFrame.setOnClickListener {itemClickListener.onItemClicked(id, originalTitle, thumbnail, releaseDate, overView, rating)}
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.title
        val releaseDate: TextView = itemView.release_date
        val thumbNail: ImageView = itemView.thumbnail
        val movieFrame: CardView = itemView.movie_frame
    }

    interface ItemClickListener {
        fun onItemClicked(id: Int, originalTitle: String, posterPath: String, releaseDate: String, overView: String, rating: Double)
    }

}