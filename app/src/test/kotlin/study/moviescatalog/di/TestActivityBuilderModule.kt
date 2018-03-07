package study.moviescatalog.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import study.moviescatalog.feature.details.TestDetailsModule
import study.moviescatalog.feature.movies.TestMoviesModule
import study.moviescatalog.features.details.DetailsActivity
import study.moviescatalog.features.movies.MoviesActivity


@Module
abstract class TestActivityBuilderModule {
    @ContributesAndroidInjector(modules = arrayOf(TestMoviesModule::class))
    internal abstract fun bindMainActivity(): MoviesActivity

    @ContributesAndroidInjector(modules = arrayOf(TestDetailsModule::class))
    internal abstract fun bindDetailsActivity(): DetailsActivity
}