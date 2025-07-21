package com.example.pagination_new.data.retrofit

import com.example.pagination_new.data.retrofit.Const.Companion.X_API_KEY
import com.example.pagination_new.domain.classess.Film
import com.example.pagination_new.domain.classess.Description
import com.example.pagination_new.domain.classess.genre.Genre_list
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApi {

    @GET("v1.4/movie")
    @Headers(X_API_KEY)
    suspend fun getAllFIlms(
        @Query("page") page: Number,
        @Query("limit") limit: Number
    ): Response<Film>

@GET("v1.4/movie/search")
@Headers(X_API_KEY)
suspend fun getFilmsByTitle(
    @Query("page") page: Number,
    @Query("limit") limit: Number,
     @Query("query") title: String
): Response<Film>

@GET("v1.4/movie")
@Headers(X_API_KEY)
suspend fun getFilmsWithPoster(
    @Query("page") page: Number,
    @Query("limit") limit: Number,
    @Query("notNullFields") notNullFields: String = "poster.url"
): Response<Film>


    @GET("v1/movie/possible-values-by-field")
    @Headers(X_API_KEY)
    suspend fun getGenres(
        @Query("field") field: String = "genres.name"
    ): List<Genre_list>

      @GET("v1.4/movie")
      @Headers(X_API_KEY)
      suspend fun getFilmsByGenre(
          @Query("page") page: Number,
          @Query("limit") limit: Number,
          @Query("genres.name") genre: String
      ): Response<Film>

    @GET("v1.4/movie")
    @Headers(X_API_KEY)
    suspend fun getFilmsByGenreWithPoster(
        @Query("page") page: Number,
        @Query("limit") limit: Number,
        @Query("notNullFields") notNullFields: String = "poster.url",
        @Query("genres.name") genre: String
    ): Response<Film>

    @GET("v1.4/movie/{id}")
    @Headers(X_API_KEY)
    suspend fun getFilmById(
        @Path("id") id: Int
    ): Response<Description>?
}






