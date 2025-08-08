package com.example.pagination_new.domain.modules

import com.example.pagination_new.data.RepositoryImpl
import com.example.pagination_new.domain.useCases.GetActorByIdUseCase
import com.example.pagination_new.domain.useCases.GetAllFilmsUseCase
import com.example.pagination_new.domain.useCases.GetFilmByIdUseCase
import com.example.pagination_new.domain.useCases.GetFilmsByGenreUseCase
import com.example.pagination_new.domain.useCases.GetFilmsByGenreWithPosterUseCase
import com.example.pagination_new.domain.useCases.GetFilmsWithPoster
import com.example.pagination_new.domain.useCases.GetGenresUseCase
import com.example.pagination_new.domain.useCases.SearchFilmsByTitleUseCase
//import com.example.pagination_new.domain.useCases.GetIdByNameUseCse
import com.example.pagination_new.domain.useCases.SearchPersonsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

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
    fun provideGetFilmsWithPoster(repository: RepositoryImpl) = GetFilmsWithPoster(repository)


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


}

// @Module
// @InstallIn(SingletonComponent::class)
//  object AppModule {
//     @Singleton
//     @Provides
// fun provideContext(@ApplicationContext context: Context) = context
//
//  }