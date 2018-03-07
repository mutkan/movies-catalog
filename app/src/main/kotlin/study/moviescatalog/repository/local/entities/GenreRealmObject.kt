package study.moviescatalog.repository.local.entities

import study.moviescatalog.repository.remote.entities.Genre
import io.realm.RealmObject


open class GenreRealmObject(var id: Int?,
                            var name: String?) : RealmObject() {

    constructor() : this(null, null)
    fun toGenre(): Genre = Genre(id, name)
}