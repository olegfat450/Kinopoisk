package com.example.pagination_new.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pagination_new.data.retrofit.RetrofitInstance.retrofit
import com.example.pagination_new.domain.classess.Doc
import com.example.pagination_new.domain.classess.Film
import retrofit2.Response


class PageSource(val event: Event) : PagingSource<Int, Doc>() {
    val limit_ = 30
   // val repository = RepositoryImpl()
  //  val getDataUseCase = GetDataUseCase(repository)

    override fun getRefreshKey(state: PagingState<Int, Doc>): Int? {
        Log.d("Ml","getRefreshKeyStarted")
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(limit_) ?: page.nextKey?.minus(limit_)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Doc> {
        var page_: Int = params.key ?: 1

        var data: Response<Film>? = null

        var nextKey: Int? = null
        var prevKey: Int? = null

       return try {

           when (event) {
               is getAllFilmsEvent -> { data = retrofit.getAllFIlms(page_,limit_) }
               is getFilmByTitleEvent -> { data = retrofit.getFilmsByTitle(title = event.title, page = page_, limit = limit_) }
                   is getFilmsWithPosterEvent -> { data = retrofit.getFilmsWithPoster(page_,limit_) }
               is getFilmsByGenreEvent -> { data = retrofit.getFilmsByGenre(page_,limit_,event.genre)}
               is getFilmsByGenreWithPosterEvent -> { data = retrofit.getFilmsByGenreWithPoster(page_,limit_, genre = event.genre, notNullFields = "poster.url")}
           }
          // data = retrofit.getAllFIlms(page_,limit_)

           data?.let {

              with(data.body()!!) { Log.d("Ml","page: ${page}  --  pages: ${pages}  --  limit: ${limit}  --  total: ${total}")  }
               nextKey = if (data.body()?.docs?.size!! < limit_) null else page_ + limit_
              prevKey = if (page_ == 1) null else page_ - limit_
               //   Log.d("Ml","${page}  --  ${nextKey}  --  ${prevKey}  --  ${data.size}  --  ${limit}")
           }

           LoadResult.Page(data = data?.body()!!.docs,prevKey = prevKey, nextKey = nextKey)


        } catch (e:Exception) { Log.d("Ml","${e.message}"); LoadResult.Error(e) }

    }}
