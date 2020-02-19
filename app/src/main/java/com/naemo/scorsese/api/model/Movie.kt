package com.naemo.scorsese.api.model

import com.google.gson.annotations.SerializedName

  class Movie {
      @SerializedName("popularity")
      var popularity: Double = 0.0
      @SerializedName("vote_count")
      var voteCount: Int = 0
      @SerializedName("video")
      var video: Boolean = false
      @SerializedName("poster_path")
      var posterPath: String = ""
      @SerializedName("id")
      var id: Int = 0
      @SerializedName("adult")
      var adult: Boolean = false
      @SerializedName("backdrop_path")
      var backdropPath: String = ""
      @SerializedName("original_language")
      var originalLanguage: String = ""
      @SerializedName("original_title")
      var originalTitle: String = ""
      @SerializedName("genre_ids")
      var genreIds: ArrayList<Int> = arrayListOf()
      @SerializedName("title")
      var title: String = ""
      @SerializedName("vote_average")
      var voteAverage: Double = 0.0
      @SerializedName("overview")
      var overview: String = ""
      @SerializedName("release_date")
      var releaseDate: String = ""
  }