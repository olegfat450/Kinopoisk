package com.example.pagination_new.data

import com.example.pagination_new.domain.person.Profession

interface Event



class getAllFilmsEvent(): Event
class getFilmByTitleEvent(val title: String): Event
class getFilmsWithPosterEvent(): Event
class getFilmsByGenreEvent(val genre: String): Event
class getFilmsByGenreWithPosterEvent(val genre: String): Event
class getFilmsByProfessionEvent(val profession: String,val id: Int): Event