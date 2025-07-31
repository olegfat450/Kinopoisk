package com.example.pagination_new.domain.classesss.film

data class Film(
    val docs: List<Doc>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)