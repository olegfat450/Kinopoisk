package com.example.pagination_new.data

interface Event



class getAllFilmsEvent(): Event
class getFilmByTitleEvent(val title: String): Event
class getFilmsWithPosterEvent(): Event
class getFilmsByGenreEvent(val genre: String): Event
class getFilmsByGenreWithPosterEvent(val genre: String): Event
class getFilmsByProfessionEvent(val profession: String,val id: Int): Event
class searchPersonsEvent(val name: String): Event