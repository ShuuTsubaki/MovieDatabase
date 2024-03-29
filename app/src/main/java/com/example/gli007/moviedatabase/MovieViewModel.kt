package com.example.gli007.moviedatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class MovieViewModel(application: Application): AndroidViewModel(application){

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob+ Dispatchers.Main


    private val scope = CoroutineScope(coroutineContext)


    private var disposable: Disposable? = null


    private val repository: MovieItemRepository = MovieItemRepository(MovieRoomDatabase.getDatabase(application).movieDao())

    var allMovies: LiveData<List<MovieItem>>

    init {
        allMovies = repository.allMovies

    }


    fun refreshMovies(page: Int){

        //TODO: add your API key from themoviedb.org
        disposable =
            RetrofitService.create("https://api.themoviedb.org/3/").getNowPlaying(
                "cc8951df77a5cb696b126871bb33208f",page).subscribeOn(
                Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                {result -> showResult(result)},
                {error -> showError(error)})
    }

    private fun showError(error: Throwable?) {

       //TODO: handle error
    }

    private fun showResult(movies: Movies?) {
        deleteAll()
        movies?.results?.forEach { movie ->
            insert(movie)
        }
    }

    private fun insert(movie: MovieItem) = scope.launch(Dispatchers.IO) {
        repository.insert(movie)
    }

    private fun deleteAll() = scope.launch (Dispatchers.IO){
        repository.deleteAll()
    }

    fun like(title: String) = scope.launch (Dispatchers.IO){
        repository.like(title)
    }

    fun dislike(title: String) = scope.launch (Dispatchers.IO){
        repository.dislike(title)
    }
}