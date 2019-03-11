package com.example.gli007.moviedatabase

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class MovieItemRepository(private val movieDao: MovieItemDao){

    var allMovies: LiveData<List<MovieItem>> = movieDao.getAllMovies()

    @WorkerThread
    fun insert(movie: MovieItem){
        movieDao.insertMovie(movie)
    }

    @WorkerThread
    fun deleteAll(){
        movieDao.DeleteAll()
    }

    @WorkerThread
    fun like(title: String){
        movieDao.likeMovie(title)
    }

    @WorkerThread
    fun dislike(title: String){
        movieDao.dislikeMovie(title)
    }

}