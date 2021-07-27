package com.example.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.row_movie.*
import kotlinx.android.synthetic.main.row_movie.view.*
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import java.net.URL

class MovieActivity : AppCompatActivity(), AnkoLogger {
    val TAG = MovieActivity::class.java.simpleName
    var movies : List<Movie>? = null
    val movieretrofit = Retrofit.Builder()
        .baseUrl("https://pastebin.com/raw/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        //https://pastebin.com/ 存放data 網址
        //get json
        //Anko 取代 asynctask
        //https://github.com/Kotlin/anko
        doAsync {
            //val json = URL("https://pastebin.com/raw/P88WBwdP").readText()
            //Log.d(TAG, "MovieActivity: $json")
            //movies = Gson().fromJson<List<Movie>>(json, object : TypeToken<List<Movie>>(){}.type)
            val moviesevice = movieretrofit.create(MovieSevice::class.java)
            movies = moviesevice.listMovies()
                .execute()
                .body()
            movies?.forEach {
                info("${it.Title}" )
            }
                uiThread {
                 //Toast.makeText(it, "Got it", Toast.LENGTH_LONG)
                toast("Got it")
                    movierecycler.layoutManager = LinearLayoutManager(this@MovieActivity)
                    movierecycler.setHasFixedSize(true)
                    movierecycler.adapter = MovieAdapter()
                alert("Got it", "Alert") {
                    okButton {
                    }
                }.show()
            }
        }
    }inner class MovieAdapter() : RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            return MovieHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.row_movie,parent,false))
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = movies?.get(position)
            holder.bindMovie(movie!!)
        }

        override fun getItemCount(): Int {
            val size = movies?.size?:0
            return size
        }
    }


    inner class MovieHolder(view : View) : RecyclerView.ViewHolder(view) , AnkoLogger{
        val titleText = view.movie_title
        val imdbText = view.movie_imdb
        val directorText = view.movie_director
        //https://github.com/bumptech/glide
        val posterImage : ImageView = view.movie_poster
        fun bindMovie(movie : Movie){
            titleText.text = movie.Title
            imdbText.text = movie.imdbRating
            directorText.text = movie.Director
            Glide.with(this@MovieActivity)
                .load(movie.Poster)
                .override(300)
                .into(posterImage)
        }
    }

}

//json setting 外掛 json to kotlin

data class Movie(
    val Actors: String,
    val Awards: String,
    val Country: String,
    val Director: String,
    val Genre: String,
    val Images: List<String>,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String
)

//https://square.github.io/retrofit/
//https://programming.im.ncnu.edu.tw/J_Chapter7.htm
interface   MovieSevice{
    @GET("P88WBwdP")
    fun listMovies(): Call<List<Movie>>
    //fun movemovie()
    }