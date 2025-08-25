package com.example.pagination_new.data

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pagination_new.App
import com.example.pagination_new.data.retrofit.MainDb
import com.example.pagination_new.data.retrofit.RetrofitInstance.retrofit
import com.example.pagination_new.databinding.ActivityMainBinding
import com.example.pagination_new.di.MainActivity
import com.example.pagination_new.domain.classesss.FavoriteFilm
import com.example.pagination_new.domain.classesss.PagerAdapterClass
import com.example.pagination_new.domain.classesss.film.Doc
import com.example.pagination_new.domain.classesss.person.Persons
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

interface Progress { fun show(); fun noshow() }
var this_progress: Progress? = null


class PageSource ( val event: Event? = null) : PagingSource<Int, PagerAdapterClass>() {
    val limit = 30

        fun execute( progress: Progress )  {  this_progress = progress };

    override fun getRefreshKey(state: PagingState<Int, PagerAdapterClass>): Int? {

        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(limit) ?: page.nextKey?.minus(limit)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PagerAdapterClass> {
        val page: Int = params.key ?: 1
        val page_ = ((page.toDouble()/limit) + 1).roundToInt()


        var data: List<PagerAdapterClass>? = null

        var nextKey: Int? = null
        var prevKey: Int? = null

        this_progress?.show()


       return try {

           when (event) {



               is getAllFilmsEvent -> { data = mapDocToPagerAdapterClass( retrofit.getAllFIlms(page_,limit).body()!!.docs ); }
               is getFilmByTitleEvent -> { data = mapDocToPagerAdapterClass(retrofit.getFilmsByTitle(title = event.title, page = page_, limit = limit).body()!!.docs) }
               is getFilmsWithPosterEvent -> { data = mapDocToPagerAdapterClass(retrofit.getFilmsWithPoster(page_,limit).body()!!.docs) }
               is getFilmsByGenreEvent -> { data = mapDocToPagerAdapterClass(retrofit.getFilmsByGenre(page_,limit,event.genre).body()!!.docs)}
               is getFilmsByGenreWithPosterEvent -> { data = mapDocToPagerAdapterClass (retrofit.getFilmsByGenreWithPoster(page_,limit, genre = event.genre, notNullFields = "poster.url").body()!!.docs)}
               is searchPersonsEvent -> { data = mapPersonsToPagerAdapterClass( retrofit.searchPersons(name = event.name, page = page_, limit = limit).body()!!) }
               is getTop250FilmsEvent -> { data = mapDocToPagerAdapterClass(retrofit.getTop250Films(page_,limit).body()!!.docs) }
               is getFavoriteFilmsEvent -> {

                  

                      data = mapFavoriteFilmToAdapterClass(event.db.dao().getFavoriteFilms());
                   data.forEach { Log.d("Ml","${it}") }


                   }
           }

           data?.let {

               this_progress?.noshow()



               nextKey = if (data!!.size < limit) null else page + limit

              prevKey = if (page == 1) null else page - limit


           }

           LoadResult.Page(data = data?: emptyList(),prevKey = prevKey, nextKey = nextKey)




        } catch (e:Exception) { Log.d("Ml","Error: ${e.message}"); LoadResult.Error(e) }
    }

}



fun mapFavoriteFilmToAdapterClass(favorite: List<FavoriteFilm>): List<PagerAdapterClass> {

     var list: List<PagerAdapterClass> = listOf()

    favorite.forEach { list += PagerAdapterClass(id = it.id, title = it.name, image = it.poster, text_1 = it.genres, text_2 = it.countries, text_3 = "Год: ${it.year}", text_4 = "Imdb: ${it.rating}") }

   return list
}

fun mapDocToPagerAdapterClass(doc: List<Doc>): List<PagerAdapterClass> {

    var list: List<PagerAdapterClass> = listOf()

    doc.forEach {


        list += PagerAdapterClass(id = it.id, image = it.poster?.previewUrl ?: "",
        title = it.name?: it.alternativeName,
        text_1 = it.genres?.getOrNull(0)?.name?: "",
        text_2 = it.countries?.getOrNull(0)?.name?: "",
        text_3 = "Год: ${it.year?.toString()?: "н/д"}",
        text_4 = "Imdb: ${it.rating?.imdb?.toString()?: "н/д"}"
        ) }
    return list

}
fun mapPersonsToPagerAdapterClass(persons: Persons): List<PagerAdapterClass>{
    var list: List<PagerAdapterClass> = listOf()

   persons.docs.forEach { list += PagerAdapterClass(id = it.id, image = it.photo.toString(), title = if( (it.name == null) || it.name.isEmpty()) it.enName else it.name, age = it.age) }
    return list
}

