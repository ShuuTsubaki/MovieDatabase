package com.example.gli007.moviedatabase

data class Movies(val results: List<MovieItem>,
                  val total_pages: Int,
                  val page: Int)