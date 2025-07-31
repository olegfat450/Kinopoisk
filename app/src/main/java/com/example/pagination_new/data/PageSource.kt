package com.example.pagination_new.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pagination_new.data.retrofit.RetrofitInstance.retrofit
import com.example.pagination_new.domain.classesss.PagerAdapterClass
import com.example.pagination_new.domain.classesss.film.Doc
import com.example.pagination_new.domain.classesss.person.Persons
import kotlin.math.roundToInt


class PageSource(val event: Event) : PagingSource<Int, PagerAdapterClass>() {
    val limit = 30
   // val repository = RepositoryImpl()
  //  val getDataUseCase = GetDataUseCase(repository)

    override fun getRefreshKey(state: PagingState<Int, PagerAdapterClass>): Int? {
        Log.d("Ml","getRefreshKeyStarted")
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(limit) ?: page.nextKey?.minus(limit)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PagerAdapterClass> {
        val page: Int = params.key ?: 1
        val page_ = ((page.toDouble()/limit) + 1).roundToInt()


        var data: List<PagerAdapterClass> = listOf()

        var nextKey: Int? = null
        var prevKey: Int? = null
        Log.d("Ml","$page")

       return try {

           when (event) {
               is getAllFilmsEvent -> { Log.d("Ml","getFilms"); data = mapDocToPagerAdapterClass( retrofit.getAllFIlms(page_,limit).body()!!.docs )}
               is getFilmByTitleEvent -> { data = mapDocToPagerAdapterClass(retrofit.getFilmsByTitle(title = event.title, page = page_, limit = limit).body()!!.docs) }
                   is getFilmsWithPosterEvent -> { data = mapDocToPagerAdapterClass(retrofit.getFilmsWithPoster(page_,limit).body()!!.docs) }
               is getFilmsByGenreEvent -> { data = mapDocToPagerAdapterClass(retrofit.getFilmsByGenre(page_,limit,event.genre).body()!!.docs)}
               is getFilmsByGenreWithPosterEvent -> { data = mapDocToPagerAdapterClass (retrofit.getFilmsByGenreWithPoster(page_,limit, genre = event.genre, notNullFields = "poster.url").body()!!.docs)}
               is getFilmsByProfessionEvent -> { data = mapDocToPagerAdapterClass(retrofit.getFilmsByProfession(page_,limit, profession = event.profession,id = event.id).body()!!.docs)}
               is searchPersonsEvent -> { data = mapPersonsToPagerAdapterClass( retrofit.searchPersons(name = event.name, page = page_, limit = limit).body()!!)
               }
           }
          // data = retrofit.getAllFIlms(page_,limit_)

           data?.let {

            //  with(data.body()!!) { Log.d("Ml","page: ${data.body()!!.page}  --  pages: ${pages}  --  limit: ${limit}  --  total: ${total}")  }
               nextKey = if (data.size < limit) null else page + limit

              prevKey = if (page == 1) null else page - limit
               //   Log.d("Ml","${page}  --  ${nextKey}  --  ${prevKey}  --  ${data.size}  --  ${limit}")
             //  with(data.body()!!) { Log.d("Ml","page: ${page}  --  pages: ${pages}  --  limit: ${limit}  --  total: ${total}")  }
             //  Log.d("Ml","nextKey: $nextKey -- prevKey: $prevKey  -- page: ${page} -- page_: ${page_}")

           }

           LoadResult.Page(data = data,prevKey = prevKey, nextKey = nextKey)


        } catch (e:Exception) { Log.d("Ml","${e.message}"); LoadResult.Error(e) }

    }}

fun mapDocToPagerAdapterClass(doc: List<Doc>): List<PagerAdapterClass> {

    var list: List<PagerAdapterClass> = listOf()


    doc.forEach { list += PagerAdapterClass(id = it.id, image = it.poster?.previewUrl ?: "",
        title = it.name?: it.alternativeName,
        text_1 = it.genres?.get(0)?.name?: "",
        text_2 = it.countries?.get(0)?.name?: "",
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
