package study.moviescatalog.repository.local

import study.moviescatalog.domain.Movie
import study.moviescatalog.repository.local.entities.GenreRealmObject
import study.moviescatalog.repository.local.entities.MovieRealmObject
import io.reactivex.Observable
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.Sort
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import java.util.*


interface FavoritesRepository {
    fun favorite(movie: Movie): Observable<Movie>
    fun unfavorite(id: Int)
    fun fetch(): Observable<Movie>
    fun deleteAll()
}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FavoritesRepositoryLocal(configuration: RealmConfiguration) :
        AbstractRealmRepository(configuration), FavoritesRepository {

    companion object {
        private val CLAZZ_MOVIE = MovieRealmObject::class.java
        private val CLAZZ_GENRE = GenreRealmObject::class.java
    }

    override fun fetch(): Observable<Movie> {
        realm().use {
            val favorites = it.copyFromRealm(it.where(CLAZZ_MOVIE).findAllSorted("favoredAt", Sort.ASCENDING))
            return Observable.fromIterable(favorites.map(MovieRealmObject::toMovie))
        }
    }

    override fun favorite(movie: Movie): Observable<Movie> {
        var favorite: MovieRealmObject? = null

        movie.apply {
            val list = RealmList<GenreRealmObject>()
            genres?.forEach { list.add(GenreRealmObject(it.id, it.name)) }

            favorite = MovieRealmObject(id, Date(), releaseDate, voteAverage,
                    title, popularity, backDropPath, adult, list, overview)
        }

        try {
            realm().use {
                it.executeTransaction {
                    it.copyToRealm(favorite)
                }
            }
        } catch (e: RealmPrimaryKeyConstraintException) {
            return Observable.error(e)
        }

        return Observable.just(favorite?.toMovie())
    }

    override fun unfavorite(id: Int) {
        realm().use {
            val movie: MovieRealmObject? = it.where(CLAZZ_MOVIE).equalTo("id", id).findFirst()
            it.executeTransaction {
                movie?.deleteFromRealm()
            }
        }
    }

    override fun deleteAll() =
            realm().use {
                it.executeTransaction { realm ->
                    realm.delete(CLAZZ_MOVIE)
                    realm.delete(CLAZZ_GENRE)
                }
            }
}

data class FavoredEvent(val favored: Boolean, val movieId: Int)

