package study.moviescatalog.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import study.moviescatalog.TestFaker.Companion.fakerSubject
import study.moviescatalog.TestFaker.Companion.favoredMovies
import study.moviescatalog.TestFaker.Companion.moviePage1MatchedFaked
import study.moviescatalog.TestSchedulerProvider
import study.moviescatalog.di.qualifier.TestScheduler
import study.moviescatalog.repository.local.FavoredEvent
import study.moviescatalog.repository.local.FavoritesRepository
import io.reactivex.Observable
import org.mockito.Mockito.*
import javax.inject.Singleton

@Module
class TestApplicationModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideFavoriteRepository(): FavoritesRepository {
        val favoriteRepositoryMock: FavoritesRepository = mock(FavoritesRepository::class.java)

        `when`(favoriteRepositoryMock.fetch())
                .thenReturn(Observable.fromIterable(favoredMovies))
        `when`(favoriteRepositoryMock.favorite(moviePage1MatchedFaked[0]))
                .thenReturn(Observable.just(moviePage1MatchedFaked[0]))
        doNothing().`when`(favoriteRepositoryMock)
                .unfavorite(moviePage1MatchedFaked[0].id!!)

        return favoriteRepositoryMock
    }

    @Provides
    @Singleton
    fun provideFavoriteObservable(): Observable<FavoredEvent> = fakerSubject

    @Provides
    @TestScheduler
    @Singleton
    fun provideScheduler(): SchedulerProvider = TestSchedulerProvider()
}