package com.example.gli007.moviedatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieItemDao{

    @Query("SELECT * FROM movie_table order BY release_date DESC")
    fun getAllMovies(): LiveData<List<MovieItem>>


    @Query ("DELETE FROM movie_table")
    fun DeleteAll()

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieItem)

    @Query("UPDATE movie_table SET liked = 1 WHERE title = :title")
    fun likeMovie(title: String)

    @Query("UPDATE movie_table SET liked = 0 WHERE title = :title")
    fun dislikeMovie(title: String)

    @Query("SELECT * FROM movie_table order BY release_date ASC")
    fun getAllMoviesAsc(): LiveData<List<MovieItem>>
}