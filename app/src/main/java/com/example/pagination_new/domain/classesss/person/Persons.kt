package com.example.pagination_new.domain.classesss.person

data class Persons(
    val docs: List<Doc>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)

data class Doc(
    val age: Int?,
//    val birthday: String,
//    val death: String,
    val enName: String?,
//    val growth: Int,
    val id: Int,
    val name: String?,
    val photo: String?,
//    val sex: String
)