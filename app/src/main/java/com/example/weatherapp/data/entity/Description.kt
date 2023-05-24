package com.example.weatherapp.data.entity

data class Description(
    val mainTitle: String,
    val mainDesc: String,
    val weatherIcon: String
) {
    override fun toString(): String {
        return mainDesc
    }
}