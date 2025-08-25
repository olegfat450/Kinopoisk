package com.example.pagination_new.data

import android.content.Context
import com.example.pagination_new.data.retrofit.MainDb
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

interface Event



class getAllFilmsEvent(): Event
class getFilmByTitleEvent(val title: String): Event
class getFilmsWithPosterEvent(): Event
class getFilmsByGenreEvent(val genre: String): Event
class getFilmsByGenreWithPosterEvent(val genre: String): Event
class searchPersonsEvent(val name: String): Event
class getTop250FilmsEvent(): Event

class getFavoriteFilmsEvent(val db: MainDb): Event