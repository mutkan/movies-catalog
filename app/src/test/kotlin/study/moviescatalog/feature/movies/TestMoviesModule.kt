package study.moviescatalog.feature.movies

import dagger.Module
import dagger.Provides
import study.moviescatalog.TestFaker.Companion.genresFaked
import study.moviescatalog.TestFaker.Companion.moviePage1Faked
import study.moviescatalog.TestFaker.Companion.moviePage2Faked
import study.moviescatalog.di.SchedulerProvider
import study.moviescatalog.di.qualifier.TestScheduler
import study.moviescatalog.features.movies.MoviesActivity
import study.moviescatalog.features.movies.MoviesPresenter
import study.moviescatalog.features.movies.MoviesPresenterImpl
import study.moviescatalog.features.movies.MoviesView
import study.moviescatalog.repository.local.FavoredEvent
import study.moviescatalog.repository.local.FavoritesRepository
import study.moviescatalog.repository.remote.MoviesAPI
import study.moviescatalog.repository.remote.MoviesRepository
import study.moviescatalog.repository.remote.MoviesRepositoryRemote
import io.reactivex.Observable
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@Module
class TestMoviesModule {
    @Provides
    fun provideView(activity: MoviesActivity): MoviesView = activity

    @Provides
    fun providePresenter(mainView: MoviesView,
                         moviesRepository: MoviesRepository,
                         favoritesRepository: FavoritesRepository,
                         @TestScheduler schedulerProvider: SchedulerProvider,
                         favoriteObservable: Observable<FavoredEvent>): MoviesPresenter =
            MoviesPresenterImpl(mainView, moviesRepository, favoriteObservable,
                    favoritesRepository, schedulerProvider)

    @Provides
    fun provideMovieRepository(): MoviesRepository {
        val moviesAPI = mock(MoviesAPI::class.java)
        val apiKey = "12345678"

        `when`(moviesAPI.getGenres(apiKey)).thenReturn(Observable.just(genresFaked))
        `when`(moviesAPI.getPopularMovies(apiKey, 1)).thenReturn(Observable.just(moviePage1Faked))
        `when`(moviesAPI.getPopularMovies(apiKey, 2)).thenReturn(Observable.just(moviePage2Faked))

        return MoviesRepositoryRemote(moviesAPI, apiKey)
    }

}