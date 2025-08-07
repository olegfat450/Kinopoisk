package com.example.pagination_new.domain.classesss.person

data class PersonItem(
    val age: Int?,
    val birthPlace: List<BirthPlace>?,
    val birthday: String?,
    val death: String?,
    val deathPlace: List<DeathPlace>,
    val enName: String?,
    val growth: Int,
    val id: Int,
    val name: String?,
    val photo: String?,
    val profession: List<Profession>,
    val sex: String,
    val facts: List<Facts>?,
    val movies: List<Movies>
)
data class Movies(val id: Int,val name: String?,val alternativeName: String?)
