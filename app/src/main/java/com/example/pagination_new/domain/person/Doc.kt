package com.example.pagination_new.domain.person

data class Doc(
    val age: Int,
    val birthPlace: List<BirthPlace>,
    val birthday: String,
    val death: String,
    val deathPlace: List<DeathPlace>,
    val enName: String,
    val growth: Int,
    val id: Int,
    val name: String,
    val photo: String,
    val profession: List<Profession>,
    val sex: String
)