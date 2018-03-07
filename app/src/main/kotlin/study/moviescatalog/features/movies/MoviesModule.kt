package study.moviescatalog.features.movies

import dagger.Module
import dagger.Provides
import study.moviescatalog.repository.remote.MoviesRepository
import study.moviescatalog.repository.remote.MoviesRepositoryRemote

@Module
class MoviesModule {
    @Provides
    fun provideView(activity: MoviesActivity): MoviesView = activity

    @Provides
    fun providePresenter(presenter: MoviesPresenterImpl): MoviesPresenter = presenter

    @Provides
    fun provideMovieRepository(moviesRepository: MoviesRepositoryRemote)
            : MoviesRepository = moviesRepository
}