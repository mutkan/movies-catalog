package study.moviescatalog.feature.details

import dagger.Module
import dagger.Provides
import study.moviescatalog.TestFaker.Companion.fakerSubject
import study.moviescatalog.features.details.DetailsPresenter
import study.moviescatalog.features.details.DetailsPresenterImpl
import study.moviescatalog.features.details.DetailsView
import study.moviescatalog.repository.local.FavoritesRepository
import org.mockito.Mockito.mock

@Module
class TestDetailsModule {
    @Provides
    fun provideView(): DetailsView = mock(DetailsView::class.java)

    @Provides
    fun providePresenter(detailsView : DetailsView,
                         favoritesRepository: FavoritesRepository): DetailsPresenter =
            DetailsPresenterImpl(detailsView, favoritesRepository, fakerSubject)
}