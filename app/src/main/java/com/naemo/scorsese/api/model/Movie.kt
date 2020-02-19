package com.naemo.scorsese.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

  class Movie() : Parcelable{
      override fun writeToParcel(p0: Parcel?, p1: Int) {
          p0?.writeDouble(popularity)
          p0?.writeInt(voteCount)
          p0?.writeByte(if (video) 1 else 0)
          p0?.writeString(posterPath)
          p0?.writeInt(id)
          p0?.writeByte(if (adult) 1 else 0)
          p0?.writeString(backdropPath)
          p0?.writeString(originalLanguage)
          p0?.writeString(originalTitle)
          p0?.writeString(title)
          p0?.writeDouble(voteAverage)
          p0?.writeString(overview)
          p0?.writeString(releaseDate)
      }

      override fun describeContents(): Int {
         return 0
      }

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

      constructor(parcel: Parcel) : this() {
          popularity = parcel.readDouble()
          voteCount = parcel.readInt()
          video = parcel.readByte() != 0.toByte()
          posterPath = parcel.readString().toString()
          id = parcel.readInt()
          adult = parcel.readByte() != 0.toByte()
          backdropPath = parcel.readString().toString()
          originalLanguage = parcel.readString().toString()
          originalTitle = parcel.readString().toString()
          title = parcel.readString().toString()
          voteAverage = parcel.readDouble()
          overview = parcel.readString().toString()
          releaseDate = parcel.readString().toString()
      }

      companion object CREATOR : Parcelable.Creator<Movie> {
          override fun createFromParcel(parcel: Parcel): Movie {
              return Movie(parcel)
          }

          override fun newArray(size: Int): Array<Movie?> {
              return arrayOfNulls(size)
          }
      }
  }