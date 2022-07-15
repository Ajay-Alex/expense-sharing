package com.example.entities

data class Audit(
    val toId: Int,
    val fromId: Int,
    var amount: Int
)
