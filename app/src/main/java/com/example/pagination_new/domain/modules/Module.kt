package com.example.pagination_new.domain.modules

import android.app.Application
import androidx.room.Room
import com.example.pagination_new.data.RepositoryImpl
import com.example.pagination_new.data.retrofit.MainDb
import com.example.pagination_new.domain.useCases.DeleteFilmByIdUseCase
import com.example.pagination_new.domain.useCases.GetActorByIdUseCase
import com.example.pagination_new.domain.useCases.GetAllFilmsUseCase
import com.example.pagination_new.domain.useCases.GetFavoriteFilmsIdUseCase
import com.example.pagination_new.domain.useCases.GetFavoriteFilmsUseCase
import com.example.pagination_new.domain.useCases.GetFilmByIdUseCase
import com.example.pagination_new.domain.useCases.GetFilmsByGenreUseCase
import com.example.pagination_new.domain.useCases.GetFilmsByGenreWithPosterUseCase

import com.example.pagination_new.domain.useCases.GetFilmsWithPosterUseCase
import com.example.pagination_new.domain.useCases.GetGenresUseCase
import com.example.pagination_new.domain.useCases.GetTop250FilmsUseCase
import com.example.pagination_new.domain.useCases.InsertFilmUseCase
import com.example.pagination_new.domain.useCases.SearchFilmsByTitleUseCase
//import com.example.pagination_new.domain.useCases.GetIdByNameUseCse
import com.example.pagination_new.domain.useCases.SearchPersonsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
class Module {

    @Provides
    fun provideRepositoryImpl() = RepositoryImpl()

    @Provides
    fun provideGetAllFilmsUseCase(repository: RepositoryImpl) = GetAllFilmsUseCase(repository)

    @Provides
    fun provideSearchFilmsByTitleUseCase(repository: RepositoryImpl) = SearchFilmsByTitleUseCase(repository)

    @Provides
    fun provideGetFilmsWithPoster(repository: RepositoryImpl) = GetFilmsWithPosterUseCase(repository)


    @Provides
    fun provideGetGenres(repository: RepositoryImpl) = GetGenresUseCase(repository)

    @Provides
    fun provideGetFilmsByGenre(repository: RepositoryImpl) = GetFilmsByGenreUseCase(repository)

    @Provides
    fun provideGetFilmsByGenreWithPoster(repository: RepositoryImpl) = GetFilmsByGenreWithPosterUseCase(repository)

    @Provides
    fun provideGetFilmById(repository: RepositoryImpl) = GetFilmByIdUseCase(repository)

    @Provides
    fun provideGetActorById(repository: RepositoryImpl) = GetActorByIdUseCase(repository)

    @Provides
    fun searchPersons(repository: RepositoryImpl) = SearchPersonsUseCase(repository)

    @Provides
    fun getTop250Films(repository: RepositoryImpl) = GetTop250FilmsUseCase(repository)

    @Provides
    fun provideGetFavoriteFilms(repository: RepositoryImpl) = GetFavoriteFilmsUseCase(repository)

    @Provides
    fun provideGetFavoriteFilmsId(repository: RepositoryImpl) = GetFavoriteFilmsIdUseCase(repository)

    @Provides
    fun provideDeleteFilmById(repository: RepositoryImpl) = DeleteFilmByIdUseCase(repository)

    @Provides
    fun provideInsertFilm(repository: RepositoryImpl) = InsertFilmUseCase(repository)

//    @Provides
//    fun provideMainDb(app: Application): MainDb {
//        return Room.databaseBuilder(
//            app,
//            MainDb::class.java,
//            "favoriteFilms.db"
//        ).build()
//    }
}


// @Module
// @InstallIn(SingletonComponent::class)
//  object AppModule {
//     @Singleton
//     @Provides
// fun provideContext(@ApplicationContext context: Context) = context
//
//  }