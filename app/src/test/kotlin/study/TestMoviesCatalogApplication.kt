package study

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import study.moviescatalog.di.DaggerTestApplicationComponent
import study.moviescatalog.di.TestApplicationComponent


class TestMoviesCatalogApplication : DaggerApplication() {

    companion object {
        lateinit var testApplicationComponent: TestApplicationComponent
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        testApplicationComponent = DaggerTestApplicationComponent.builder()
                .application(this)
                .build()

        testApplicationComponent.inject(this)
        return testApplicationComponent
    }

}