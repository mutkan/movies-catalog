package study.moviescatalog.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import study.moviescatalog.features.details.DetailsModule
import study.moviescatalog.features.details.DetailsActivity
import study.moviescatalog.features.movies.MoviesActivity
import study.moviescatalog.features.movies.MoviesModule

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(modules = arrayOf(MoviesModule::class, RemoteModule::class))
    internal abstract fun bindMainActivity(): MoviesActivity
    @ContributesAndroidInjector(modules = arrayOf(DetailsModule::class))
    internal abstract fun bindDetailsActivity(): DetailsActivity
}