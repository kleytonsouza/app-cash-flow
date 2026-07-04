package com.example.appcashflow.entity

data class Cadastro(
    val id: Int = 0,
    val nome: String,
    val valor: Double,
    val data: String,
    val despesa: Boolean
)