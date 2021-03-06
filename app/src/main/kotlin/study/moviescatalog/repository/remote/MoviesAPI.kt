package study.moviescatalog.repository.remote

import study.moviescatalog.repository.remote.entities.Genre
import study.moviescatalog.repository.remote.entities.MovieRemote
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface MoviesAPI {
    @GET("/3/movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String, @Query("page") page: Int) : Observable<List<MovieRemote>>

    @GET("/3/genre/movie/list")
    fun getGenres(@Query("api_key") apiKey: String) : Observable<List<Genre>>
}