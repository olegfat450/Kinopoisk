package com.example.pagination_new.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pagination_new.data.retrofit.RetrofitInstance.retrofit
import com.example.pagination_new.domain.classess.Doc
import com.example.pagination_new.domain.classess.Film
import retrofit2.Response
import kotlin.math.roundToInt


class PageSource(val event: Event) : PagingSource<Int, Doc>() {
    val limit = 30
   // val repository = RepositoryImpl()
  //  val getDataUseCase = GetDataUseCase(repository)

    override fun getRefreshKey(state: PagingState<Int, Doc>): Int? {
        Log.d("Ml","getRefreshKeyStarted")
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(limit) ?: page.nextKey?.minus(limit)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Doc> {
        val page: Int = params.key ?: 1
        val page_ = ((page.toDouble()/limit) + 1).roundToInt()


        var data: Response<Film>? = null

        var nextKey: Int? = null
        var prevKey: Int? = null

       return try {

           when (event) {
               is getAllFilmsEvent -> { data = retrofit.getAllFIlms(page_,limit) }
               is getFilmByTitleEvent -> { data = retrofit.getFilmsByTitle(title = event.title, page = page_, limit = limit) }
                   is getFilmsWithPosterEvent -> { data = retrofit.getFilmsWithPoster(page_,limit) }
               is getFilmsByGenreEvent -> { data = retrofit.getFilmsByGenre(page_,limit,event.genre)}
               is getFilmsByGenreWithPosterEvent -> { data = retrofit.getFilmsByGenreWithPoster(page_,limit, genre = event.genre, notNullFields = "poster.url")}
               is getFilmsByProfessionEvent -> { data = retrofit.getFilmsByProfession(page_,limit, profession = event.profession,id = event.id)}
           }
          // data = retrofit.getAllFIlms(page_,limit_)

           data?.let {

            //  with(data.body()!!) { Log.d("Ml","page: ${data.body()!!.page}  --  pages: ${pages}  --  limit: ${limit}  --  total: ${total}")  }
               nextKey = if (data.body()?.docs?.size!! < limit) null else page + limit

              prevKey = if (page == 1) null else page - limit
               //   Log.d("Ml","${page}  --  ${nextKey}  --  ${prevKey}  --  ${data.size}  --  ${limit}")
             //  with(data.body()!!) { Log.d("Ml","page: ${page}  --  pages: ${pages}  --  limit: ${limit}  --  total: ${total}")  }
             //  Log.d("Ml","nextKey: $nextKey -- prevKey: $prevKey  -- page: ${page} -- page_: ${page_}")

           }

           LoadResult.Page(data = data?.body()!!.docs,prevKey = prevKey, nextKey = nextKey)


        } catch (e:Exception) { Log.d("Ml","${e.message}"); LoadResult.Error(e) }

    }}
