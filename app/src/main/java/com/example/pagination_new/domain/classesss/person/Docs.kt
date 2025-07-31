package com.example.pagination_new.domain.classesss.person

data class Docs(
    val docs: List<PersonItem>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)