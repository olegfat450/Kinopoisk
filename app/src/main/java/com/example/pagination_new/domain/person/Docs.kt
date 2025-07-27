package com.example.pagination_new.domain.person

data class Docs(
    val docs: List<Doc>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)