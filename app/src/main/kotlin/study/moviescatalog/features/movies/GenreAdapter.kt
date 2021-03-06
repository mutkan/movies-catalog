package study.moviescatalog.features.movies

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import study.moviescatalog.R
import study.moviescatalog.repository.remote.entities.Genre
import java.util.*

class GenreAdapter(private var genres: List<Genre>? = null,
                   private val context: Context,
                   private val itemClick: (genres: HashMap<Int, Boolean>) -> Unit?) :
        RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    var selectedGenresFilter: HashMap<Int, Boolean> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_list, parent, false)
        return GenreViewHolder(view)
    }

    fun getSelectedGenres(): HashMap<Int, Boolean> =
            selectedGenresFilter.filter { it.value } as HashMap<Int, Boolean>

    fun getAllGenres(): HashMap<Int, Boolean> = selectedGenresFilter

    fun setSelectedGenres(selectedGenresFilter: HashMap<Int, Boolean>) {
        this.selectedGenresFilter = selectedGenresFilter
    }

    fun setGenres(genres: List<Genre>?) {
        if (this.genres == null) {
            this.genres = genres
            genres?.map { it.id?.let { it1 -> selectedGenresFilter.put(it1, true) } }
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        genres?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = genres?.size ?: 0

    inner class GenreViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val categoryCheckBox by lazy { view.findViewById<CheckBox>(R.id.category) }

        fun bind(genre: Genre) {
            categoryCheckBox.text = genre.name
            categoryCheckBox.isChecked = selectedGenresFilter[genre.id]!!

            categoryCheckBox.setOnClickListener {
                selectedGenresFilter.put(genre.id!!, !selectedGenresFilter[genre.id]!!)
                itemClick(selectedGenresFilter.filter { it.value } as HashMap<Int, Boolean>)
            }
        }
    }
}