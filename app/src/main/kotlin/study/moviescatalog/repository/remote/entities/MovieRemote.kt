package study.moviescatalog.repository.remote.entities

import com.google.gson.annotations.SerializedName
import study.moviescatalog.domain.Movie
import java.util.*


data class MovieRemote(val id: Int,
                       @SerializedName("vote_average") val voteAverage: Double,
                       val title: String,
                       val popularity: Double,
                       @SerializedName("backdrop_path") val backDropPath: String,
                       val adult: Boolean,
                       @SerializedName("genre_ids") val genreIds: List<Int>,
                       @SerializedName("release_date") val releaseDate: Date,
                       val overview: String)

fun MovieRemote.toMovie(genresRemotes: List<Genre>): Movie {
    val gender = genreIds.map { genreId -> genresRemotes.first { it.id == genreId } }
    return Movie(id, -1, false, voteAverage, title, popularity,
            "http://image.tmdb.org/t/p/w500$backDropPath", adult, gender, releaseDate, overview)
}