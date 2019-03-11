package com.example.gli007.moviedatabase

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import androidx.navigation.findNavController


/**
 * A simple [Fragment] subclass.
 *
 */
class   ListFragment : Fragment(), View.OnClickListener {

    private var topInfoText: TextView? = null
    private var refreshButton: Button? = null
    private var likedListButton: Button? = null
    private var sortByTitleButtton: Button? = null
    private var sortByVoteButton: Button? = null
    private var sortByPopuButton: Button? = null
    private var model: MovieViewModel? = null
    private var inLikedList: Boolean? = false
    internal var header = "Movies Sort By: "
    private lateinit var adapter: MovieListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.movie_list)
        adapter = MovieListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?

        model = activity?.let { ViewModelProviders.of(it).get(MovieViewModel::class.java) }

        model?.allMovies?.observe(
            this,
            Observer<List<MovieItem>>{ movies ->
                movies?.let{
                    adapter.setMovies(it)
                }
            }
        )

        topInfoText = view.findViewById<View>(R.id.topBar) as TextView
        refreshButton = view.findViewById<View>(R.id.refresh) as Button
        likedListButton = view.findViewById<View>(R.id.like_list) as Button
        sortByTitleButtton = view.findViewById<View>(R.id.sortByTitle) as Button
        sortByVoteButton = view.findViewById<View>(R.id.sortByVote) as Button
        sortByPopuButton = view.findViewById<View>(R.id.sortByPopu) as Button

        //set up buttons
        refreshButton!!.setOnClickListener(this)
        likedListButton!!.setOnClickListener(this)
        sortByPopuButton!!.setOnClickListener(this)
        sortByVoteButton!!.setOnClickListener(this)
        sortByTitleButtton!!.setOnClickListener(this)

        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.refresh -> clickedOnRefreshButton()
            R.id.sortByTitle -> clickedOnSortByTitleButton()
            R.id.sortByVote -> clickedOnSortByVoteButton()
            R.id.sortByPopu -> clickedOnSortByPopuButton()
            R.id.like_list -> clickedOnLikedButton()
        }
    }

    private fun clickedOnLikedButton() {
        if (inLikedList == false) {
            topInfoText?.text = "Movies You Liked"
            likedListButton?.text = "Go Back"
            adapter.filter_by_liked()
            inLikedList = true
            refreshButton?.isEnabled = false
            sortByPopuButton?.isEnabled = false
            sortByVoteButton?.isEnabled = false
            sortByTitleButtton?.isEnabled = false
        }
        else {
            topInfoText?.text = "Movies Showing Now"
            likedListButton?.text = "Like List"
            adapter.restore()
            inLikedList = false
            refreshButton?.isEnabled = true
            sortByPopuButton?.isEnabled = true
            sortByVoteButton?.isEnabled = true
            sortByTitleButtton?.isEnabled = true
        }

    }

    private fun clickedOnRefreshButton() {
        model?.refreshMovies(1)
    }

    private fun clickedOnSortByTitleButton() {
        topInfoText!!.text = header + "Title"
        adapter.sortByTitle()
    }

    private fun clickedOnSortByVoteButton() {
        topInfoText!!.text = header + "Vote"
        adapter.sortByVote()
    }

    private fun clickedOnSortByPopuButton() {
        topInfoText!!.text = header + "Popular"
        adapter.sortByPopu()
    }

    inner class MovieListAdapter():
        RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>(){
        private var movies = emptyList<MovieItem>()
        private var moviesBackup= emptyList<MovieItem>()

        internal fun setMovies(movies: List<MovieItem>) {
            moviesBackup = movies
            this.movies = movies
            notifyDataSetChanged()
        }

        fun filter_by_liked() {
            movies = movies.filter{it.liked == 1}
            notifyDataSetChanged()
        }

        fun restore(){
            movies = moviesBackup
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {

            return movies.size
        }

        internal fun sortByTitle(){
            movies = movies.sortedBy { it.title }
            notifyDataSetChanged()
        }

        internal fun sortByVote(){
            movies= movies.sortedBy { it.vote_average }
            notifyDataSetChanged()
        }

        internal fun sortByPopu(){
            movies = movies.sortedBy { it.popularity }
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view, parent, false)
            return MovieViewHolder(v)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {


            //holder.bindItems(movieList[position])

            Glide.with(this@ListFragment).load(resources.getString(R.string.picture_base_url)+movies[position].poster_path).apply( RequestOptions().override(128, 128)).into(holder.view.findViewById(R.id.poster))

            holder.view.findViewById<TextView>(R.id.title).text=movies[position].title

            holder.view.findViewById<TextView>(R.id.rating).text=movies[position].vote_average.toString()

            holder.itemView.setOnClickListener(){
                val simpleDate = SimpleDateFormat("dd/MM/yyyy")
                val date = simpleDate.format(movies[position].release_date)
                holder.view.findNavController().navigate(R.id.action_listFragment_to_detailFragment,
                    bundleOf("title" to movies[position].title, "poster" to movies[position].poster_path,
                        "date" to date, "popularity" to movies[position].popularity.toInt().toString(),
                        "rating" to movies[position].vote_average.toString(), "overview" to movies[position].overview,
                    "liked" to movies[position].liked))

            }

        }


        inner class MovieViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
            override fun onClick(view: View?){

                if (view != null) {


                }


            }


        }
    }

}
