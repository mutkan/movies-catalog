package study.moviescatalog.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import study.moviescatalog.di.qualifier.RealScheduler
import study.moviescatalog.repository.local.FavoredEvent
import study.moviescatalog.repository.local.FavoritesRepository
import study.moviescatalog.repository.local.FavoritesRepositoryLocal
import study.moviescatalog.repository.remote.utils.AppSchedulerProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.realm.RealmConfiguration
import javax.inject.Singleton


@Module
class ApplicationModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideFavoriteRepository(realmConfiguration: RealmConfiguration)
            : FavoritesRepository = FavoritesRepositoryLocal(realmConfiguration)

    @Provides
    @Singleton
    fun provideFavoritePublisher()
            : PublishSubject<FavoredEvent> = PublishSubject.create()

    @Provides
    @Singleton
    fun provideFavoriteObservable(publishSubject: PublishSubject<FavoredEvent>)
            : Observable<FavoredEvent> = publishSubject

    @Provides
    @RealScheduler
    fun provideScheduler(): SchedulerProvider = AppSchedulerProvider()
}
