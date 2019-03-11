package com.example.gli007.moviedatabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


/**
 * A simple [Fragment] subclass.
 *
 */
class DetailFragment : Fragment() {

    var poster_img: String? = null
    var title_text: String? = null
    var date_text: String? = null
    var vote_text: String? = null
    var popularity_text: String? = null
    var overview_text: String? = null
    var liked_bool: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_detail, container, false)
        val model = activity?.let { ViewModelProviders.of(it).get(MovieViewModel::class.java) }

        poster_img = this.arguments?.getString("poster")
            view.findViewById<ImageView>(R.id.moviePoster)
        title_text = this.arguments?.getString("title")
        date_text = this.arguments?.getString("date")
        vote_text =  this.arguments?.getString("rating")
        popularity_text = this.arguments?.getString("popularity")
        overview_text = this.arguments?.getString("overview")
        liked_bool = this.arguments?.getInt("liked")

        if (liked_bool == 0){
            view.findViewById<Button>(R.id.like_button).text = "like"
        }
        else {
            view.findViewById<Button>(R.id.like_button).text = "dislike"
        }
        view.findViewById<Button>(R.id.like_button).setOnClickListener {
            if (liked_bool == 0){
                title_text?.let { it1 -> model?.like(it1) }
                view.findViewById<Button>(R.id.like_button).text = "dislike"
                liked_bool = 1
            }
            else{
                title_text?.let { it1 -> model?.dislike(it1) }
                view.findViewById<Button>(R.id.like_button).text = "like"
                liked_bool = 0
            }
        }

        view.findViewById<TextView>(R.id.movieTitle).text = title_text
        view.findViewById<TextView>(R.id.releaseDate).text = date_text
        view.findViewById<TextView>(R.id.voteText).text = "Rating: " + vote_text
        view.findViewById<TextView>(R.id.popularText).text = "Popularity: " + popularity_text
        view.findViewById<TextView>(R.id.overviewText).text = "Overview: " + overview_text

        Glide.with(this@DetailFragment)
            .load(view.context.resources.getString(R.string.picture_base_url) + poster_img)
            .apply(RequestOptions().centerInside().override(500, 500)).into(view.findViewById(R.id.moviePoster))


        return view
    }


}
